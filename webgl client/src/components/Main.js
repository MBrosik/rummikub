import {
   Scene,
   AxesHelper,
   BufferGeometry,
   DirectionalLight,
   Vector3
} from 'three';

import { Renderer } from './modules/main_webgl_modules/Renderer';
import Camera from './modules/main_webgl_modules/Camera';
import { Keyboard } from './modules/main_webgl_modules/Keyboard_Manager';
import { my_WS, WS_Class } from './modules/WebSocket';
import LoadCards from './modules/utils/LoadCards';
import Game_Board from './modules/map_elements/Game_Board';
import Smaller_Board from './modules/map_elements/Smaller_Board';
import Card from './modules/map_elements/Card';
import { BOARD_SIZE, BOARD_POSITION, FIELD, FIELDS_COUNT } from './modules/settings/board_info';
import Map from './modules/map_elements/Map';
import CardMoveManager from './modules/after_game/CardMoveManager';
import AddIntoRooms from './modules/initial/AddIntoRooms';
import Interface from './modules/initial/Interface';



// import {STLLoader} from 'three/examples/jsm/loaders/STLLoader'

// const loader = new STLLoader();
// loader.load( './models/stl/ascii/slotted_disk.stl', function ( geometry ) {}


export default class Main {
   /**
    * @param {HTMLDivElement} container
    */
   constructor(container) {
      // -------------------
      // main WEBGL classes
      // -------------------
      this.container = container;
      this.scene = new Scene();
      this.renderer = new Renderer(this.scene, container);
      this.camera = new Camera(this.renderer);
      this.keyboard = new Keyboard();
      this.turn = false;
      this.allCards = [];

      this.renderer.render_update(this.scene, this.camera);

      // -------------------
      // axis
      // -------------------
      [-1000, 1000].forEach(el => {
         var axes = new AxesHelper(el)
         this.scene.add(axes)
      });

      // -------------------
      // webSocket
      // -------------------

      // my_WS = new WS_Class();

      window.ws = my_WS;

      // --------------------
      // cards
      // --------------------
      /**@type {Card[]} */
      this.cards = []


      // --------------------
      // Card_move_manager
      // --------------------
      /**@type {CardMoveManager} */
      this.card_move_manager = undefined;

      //nick
      this.nick = "halo";



      this.initial();
      // this.init();
   }
   initial() {
      this.addIntoRooms = new AddIntoRooms(this.play.bind(this));
   }
   play(nick) {
      this.nick = nick;
      this.divToClose = document.getElementById("startDiv");
      this.divToClose.style.display = "none";
      this.allNicks = [{ nick: this.nick }, { nick: "user2" }, { nick: "user3" }, { nick: "user4" }]
      this.init();
   }
   async init() {
      console.log(this.nick)
      // ----------------------
      // Get Card resources
      // ----------------------

      this.cards_resources = await LoadCards();

      console.log(this.cards_resources)

      // ----------------------
      // Function timeline
      // ----------------------

      await this.createMap();
      await this.roomsAdd();
   }
   async roomsAdd() {

      this.interface = new Interface(this.camera, this.container, this.sendThruButton.bind(this));

      my_WS.mySend("joinRoom", { name: this.nick })

      // ---------------------------------
      // Nasłuch wiadomości od serwera
      // o dodaniu do pokoju
      // ---------------------------------

      let messageFunc = null;

      new Promise(res => {

         messageFunc = (ev) => {
            /**@type {{type:String, data:String}} */
            let parsedData = JSON.parse(ev.data);

            if (parsedData.type == "onAddedToRoom") {
               console.log(parsedData.data)
               res();
            }
         }

         my_WS.addEventListener("message", messageFunc)
      }).then(() => {
         my_WS.removeEventListener("message", messageFunc)
      })



      // ---------------------
      // check for game begin
      // ---------------------

      let messageFunc1 = null;

      await new Promise(res => {

         messageFunc1 = (ev) => {
            /**@type {{type:String, data:String}} */
            let parsedData = JSON.parse(ev.data);
            if (parsedData.type == "GameStarted") {
               console.log(parsedData.data)
               this.allNicks = parsedData.data.playerList;
               this.drawnCards = parsedData.data.drawnCard;
               this.interface.insertNicks(this.allNicks)
               this.appendCards()
               res();
            }
         }

         my_WS.addEventListener("message", messageFunc1)
      })


      my_WS.removeEventListener("message", messageFunc1)
      my_WS.addEventListener("message", (e) => {
         // console.log(JSON.parse(e.data));
         this.whileGame(JSON.parse(e.data));
      })
   }

   whileGame(dataPlayers) {
      this.dataPlayers = dataPlayers;
      console.log(this.dataPlayers)

      this.allCards.forEach(element => {
         this.scene.remove(element);
      });

      this.allCards = []
      this.cards.splice(0)

      this.boardMapGet = this.card_move_manager.boardMap.map;

      this.boardMapGet.forEach(el => {
         el.forEach(element => {
            if (element.card != "") {
               element.card = "";
            }
         });
      });

      this.dataPlayers.data.boardCards.forEach(element => {
         let strCard = ""
         if (element.name == "Joker") {
            strCard = element.color + "_joker";
         } else {
            strCard = element.color + "_" + element.name;
         }
         if (element.x != undefined && element.y != undefined) {
            let card = new Card(
               this.cards_resources[strCard].mesh,
               new Vector3(this.boardMapGet[element.y][element.x].xPos, BOARD_POSITION.y + (BOARD_SIZE.height / 2), this.boardMapGet[element.y][element.x].zPos),
               this.meshees
            );
            this.cards.push(card);
            this.allCards.push(card);
            this.scene.add(card);
            this.boardMap.map[element.y][element.x].card = card;
            this.boardMap.map[element.y][element.x].color = strCard;
         }
      });

      this.dataPlayers.data.inHandCards.forEach(element => {
         let strCard = ""
         if (element.name == "Joker") {
            strCard = element.color + "_joker";
         } else {
            strCard = element.color + "_" + element.name;
         }
         if (element.x != undefined && element.y != undefined) {
            let card = new Card(
               this.cards_resources[strCard].mesh,
               new Vector3(this.boardMapGet[FIELDS_COUNT.z - 3][element.x].xPos, BOARD_POSITION.y + (BOARD_SIZE.height / 2), this.boardMapGet[FIELDS_COUNT.z - 3][element.x].zPos),
               this.meshees
            );
            this.cards.push(card);
            this.allCards.push(card);
            this.scene.add(card);
            this.boardMap.map[FIELDS_COUNT.z - 3][element.x].card = card;
            this.boardMap.map[FIELDS_COUNT.z - 3][element.x].color = strCard;
         }

      });

      if (this.dataPlayers.data.turn == true) {
         this.turn = true;
         this.card_move_manager.listenersAdd();
      } else {
         if (this.turn == true) {
            console.log("done")

            // console.log(this.boardMapGet);
         }
         this.turn = false;
         this.card_move_manager.listenersRemove();
      }

      this.card_move_manager.boardMap.map = this.boardMapGet;
      this.card_move_manager.cards = this.cards;
      this.card_move_manager.cardsCameraColider.cards = this.cards;

      console.log("whilegame")
   }
   sendThruButton() {
      this.objectToSend = {
         boardCards: [],
         inHandCards: []
      }
      this.boardMapGet = this.card_move_manager.boardMap.map;
      this.boardMapGet.forEach(element => {
         element.forEach(el => {
            let splitter = el.color.split("_")
            if (el.z >= FIELDS_COUNT.z - 3) {
               if (splitter[1] != undefined) {
                  this.objectToSend.inHandCards.push({ name: splitter[1], color: splitter[0], x: el.x, y: el.z })
               }
            } else {
               if (splitter[1] != undefined) {
                  this.objectToSend.boardCards.push({ name: splitter[1], color: splitter[0], x: el.x, y: el.z })
               }
            }
         });
      });
      console.log(this.objectToSend)
      my_WS.mySend("playerTurn", this.objectToSend);
   }
   listeners() {
      window.addEventListener("mousedown", this.mousedown_ev_bind)
      window.addEventListener("mousemove", this.mousemove_ev_bind)
      window.addEventListener("mouseup", this.mouseup_ev_bind)
   }

   refreshHand(drawnCardsNow) {
      // this.drawnCardsNow = drawnCardsNow
      // let count = 0
      // console.log(this.drawnCardsNow.length % 13)
      // this.drawnCardsNow.forEach(element => {
      //    let strCard = ""
      //    if (element.name == "Joker") {
      //       strCard = element.color + "_joker";
      //    } else {
      //       strCard = element.color + "_" + element.name;
      //    }
      //    let card = new Card(
      //       this.cards_resources[strCard].mesh,
      //       new Vector3(this.boardMap.map[FIELDS_COUNT.z - 3][count].xPos, BOARD_POSITION.y + (BOARD_SIZE.height / 2), this.boardMap.map[FIELDS_COUNT.z - 3][count].zPos),
      //       this.meshees
      //    );
      //    this.cards.push(card);
      //    this.allCards.push(card);
      //    this.scene.add(card);
      //    this.boardMap.map[FIELDS_COUNT.z - 3][count].card = card;
      //    this.boardMap.map[FIELDS_COUNT.z - 3][count].color = strCard;
      //    count++;
      // });
   }

   appendCards() {

      console.log("ee")
      this.refreshHand(this.drawnCards);
      this.card_move_manager = new CardMoveManager(this.camera, this.meshees, this.cards, this.renderer, this.boardMap, this.turn);
      // this.whileGame();
   }

   async createMap() {
      console.log("siemka")
      this.meshees = []
      this.game_board = new Game_Board();
      this.scene.add(this.game_board)
      this.meshees.push(this.game_board)

      for (let i = 0; i < 4; i++) {
         this.smaller_board = new Smaller_Board(i);
         this.scene.add(this.smaller_board);
         if (i == 0) {
            this.meshees.push(this.smaller_board);
         }
      }

      // ------------
      // light
      // ------------
      this.light = new DirectionalLight(0xffffee, 10);
      this.light.intensity = 0.7;
      this.light.position.set(0, 1200, 0);
      this.scene.add(this.light)


      // ----------------------
      // append Cards
      // ----------------------
      this.boardMap = new Map();
      // console.log(this.boardMap.map)

      // let card = new Card(
      //    this.cards_resources["black_1"].mesh,
      //    new Vector3(-200 + FIELD.x, BOARD_POSITION.y + (BOARD_SIZE.height / 2), -200 + FIELD.z + FIELD.depth),
      //    this.meshees
      // );
      // this.cards.push(card);
      // this.scene.add(card)

      // {
      //    let card = new Card(
      //       this.cards_resources["red_joker"].mesh,
      //       new Vector3(0, BOARD_POSITION.y + (BOARD_SIZE.height / 2), 0),
      //       this.meshees
      //    );
      //    this.cards.push(card);
      //    this.scene.add(card)
      // }
   }
}