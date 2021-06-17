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
import CardOutLine from './modules/map_elements/CardOutLine';
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
      this.outlineCards = [];
      this.readyDivs = [];
      console.warn = function () { };

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
      this.allNicks = [{ name: null }, { name: null }, { name: null }, { name: null }]
      this.init();
   }
   async init() {
      console.log(this.nick)
      // ----------------------
      // Get Card resources
      // ----------------------

      this.cards_resources = await LoadCards();
      this.cards_resourcesv2 = await LoadCards();

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

      // new Promise(res => {

      messageFunc = (ev) => {
         /**@type {{type:String, data:String}} */
         let parsedData = JSON.parse(ev.data);

         // if (parsedData.type == "onAddedToRoom") {
         //    this.interface.insertNicks(this.allNicks)
         //    console.log(parsedData.data)

         // }
         if (parsedData.type == "players_list") {
            console.log(parsedData.data)
            this.allNicks = parsedData.data.playerList;
            console.log(this.allNicks)
            this.interface.insertNicks(this.allNicks);
            // if (this.allNicks.every(el => el != null)) {
            // my_WS.removeEventListener("message", messageFunc)
            // }
            // res();
         }
      }

      my_WS.addEventListener("message", messageFunc)
      // }).then(() => {

      // })



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
               // this.allNicks = parsedData.data.playerList;
               this.drawnCards = parsedData.data.drawnCard;
               this.appendCards()
               res();
            }
         }

         my_WS.addEventListener("message", messageFunc1)
      })


      my_WS.removeEventListener("message", messageFunc1)
      my_WS.addEventListener("message", (e) => {
         // console.log(JSON.parse(e.data));
         let parsedData = JSON.parse(e.data);
         if (parsedData.type == "WhileGame") {
            this.whileGame(JSON.parse(e.data));
         } else if (parsedData.type == "GameEnded") {
            if (parsedData.data.youAreWinner == true) {
               console.log("You won: " + parsedData.data.winnerName)
               this.interface.addWinnerDiv("Brawo! wygrywasz grę: " + parsedData.data.winnerName + " !!!")
            } else {
               console.log("You lose, the winner is: " + parsedData.data.winnerName + " :(")
               this.interface.addWinnerDiv("Przegrałeś :( wygrał: " + parsedData.data.winnerName + ", jestem pewien, że następnym razem ci się uda ;P")
            }
         }

      })
   }
   timer() {
      this.timeCounter = Math.round((60000 - (Date.now() - this.firstTime)) / 1000)
      // console.log(this.timeCounter)
      if (this.interface.timerDiv != undefined) {
         this.interface.timerDiv.innerText = this.timeCounter;
      }
   }

   whileGame(dataPlayers) {
      this.firstTime = Date.now();
      if (this.interV != undefined) {
         clearInterval(this.interV);
      }
      this.interV = setInterval(() => {
         this.timer();
      }, 1000);

      this.dataPlayers = dataPlayers;
      console.log(this.dataPlayers)

      this.allCards.forEach(element => {
         this.scene.remove(element);
      });



      this.allCards = []
      this.cards.splice(0)
      this.outlineCards.splice(0)


      this.card_move_manager.boardMap.map.forEach(el => {
         el.forEach(element => {
            if (element.card != "") {
               element.card = "";
               element.color = "";
               element.ID = "";
               element.out = "";
            }
         });
      });
      console.log(this.card_move_manager.boardMap.map)

      this.dataPlayers.data.boardCards.forEach(element => {
         let strCard = ""
         if (element.name == "Joker") {
            strCard = element.color + "_joker";
         } else {
            strCard = element.color + "_" + element.name;
         }
         if (element.x != undefined && element.y != undefined) {
            let card = new Card(
               this.cards_resources[strCard].mesh.clone(true),
               new Vector3(this.card_move_manager.boardMap.map[element.y][element.x].xPos, BOARD_POSITION.y + (BOARD_SIZE.height / 2), this.card_move_manager.boardMap.map[element.y][element.x].zPos),
               this.meshees
            );
            this.scene.add(card);
            let cardOutLine = new CardOutLine(
               this.cards_resourcesv2[strCard].mesh.clone(true),
               new Vector3(this.card_move_manager.boardMap.map[element.y][element.x].xPos, BOARD_POSITION.y + (BOARD_SIZE.height / 2), this.card_move_manager.boardMap.map[element.y][element.x].zPos),
               this.meshees
            );
            this.cards.push(card);
            this.allCards.push(card);
            this.allCards.push(cardOutLine);
            this.outlineCards.push(cardOutLine)
            this.scene.add(cardOutLine);
            this.card_move_manager.boardMap.map[element.y][element.x].card = card;
            this.card_move_manager.boardMap.map[element.y][element.x].color = strCard;
            this.card_move_manager.boardMap.map[element.y][element.x].ID = element.ID;
            this.card_move_manager.boardMap.map[element.y][element.x].out = cardOutLine;
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
               this.cards_resources[strCard].mesh.clone(true),
               new Vector3(this.card_move_manager.boardMap.map[element.y][element.x].xPos, BOARD_POSITION.y + (BOARD_SIZE.height / 2), this.card_move_manager.boardMap.map[element.y][element.x].zPos),
               this.meshees
            );
            this.scene.add(card);
            let cardOutLine = new CardOutLine(
               this.cards_resourcesv2[strCard].mesh.clone(true),
               new Vector3(this.card_move_manager.boardMap.map[element.y][element.x].xPos, BOARD_POSITION.y + (BOARD_SIZE.height / 2), this.card_move_manager.boardMap.map[element.y][element.x].zPos),
               this.meshees
            );
            this.cards.push(card);
            this.allCards.push(card);
            this.allCards.push(cardOutLine);
            this.outlineCards.push(cardOutLine)
            this.scene.add(cardOutLine);
            this.card_move_manager.boardMap.map[element.y][element.x].card = card;
            this.card_move_manager.boardMap.map[element.y][element.x].color = strCard;
            this.card_move_manager.boardMap.map[element.y][element.x].ID = element.ID;
            this.card_move_manager.boardMap.map[element.y][element.x].out = cardOutLine;
         }

      });

      //tura
      this.interface.divs.forEach(element => {
         element.style.backgroundColor = "rgba(2, 72, 224, 0.5)";
      });
      this.interface.divs[this.dataPlayers.data.whoseTurn].style.backgroundColor = "rgba(2, 224, 2, 0.5)";

      this.interface.divs[this.dataPlayers.data.YourIndex].style.border = "3px solid rgb(185, 43, 251)";
      this.interface.removeTimer();
      this.interface.addTimer(this.dataPlayers.data.whoseTurn);

      if (this.dataPlayers.data.turn == true) {
         this.interface.addButton();
         this.turn = true;
         this.card_move_manager.listenersAdd();
      } else {
         if (this.turn == true) {
            console.log("done")
            this.interface.removeButton();
            // console.log(this.boardMapGet);
         }
         this.turn = false;
         this.card_move_manager.listenersRemove();
      }

      // this.card_move_manager.boardMap.map = this.boardMapGet;
      // this.card_move_manager.cards = this.cards;
      // this.card_move_manager.cardsCameraColider.cards = this.cards;

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
                  this.objectToSend.inHandCards.push({ ID: el.ID, name: splitter[1], color: splitter[0], x: el.x, y: el.z })
               }
            } else {
               if (splitter[1] != undefined) {
                  this.objectToSend.boardCards.push({ ID: el.ID, name: splitter[1], color: splitter[0], x: el.x, y: el.z })
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
      this.card_move_manager = new CardMoveManager(this.camera, this.meshees, this.cards, this.renderer, this.boardMap, this.turn, this.outlineCards);
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
      // this.light = new DirectionalLight(0xffffee, 10);
      // this.light.intensity = 0.7;
      // this.light.position.set(0, 1200, 0);
      // this.scene.add(this.light)


      [
         [-1000, -1000],
         [-1000, 1000],
         [1000, -1000],
         [1000, 1000],
         // [-1000, 0],
         // [1000, 0],
         // [0, -1000],
         // [0, 1000],
      ].forEach(el => {
         this.light = new DirectionalLight(0xffffee, 10);
         this.light.intensity = 0.5;
         this.light.position.set(el[0], 1200, el[1]);
         this.scene.add(this.light)
      });


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