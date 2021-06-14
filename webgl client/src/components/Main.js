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
import { BOARD_SIZE, BOARD_POSITION, FIELD } from './modules/settings/board_info';
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

      this.interface = new Interface(this.camera, this.container);

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
         console.log(JSON.parse(e.data));
      })
   }

   whileGame() {
      console.log("whilegame")
      this.card_move_manager = new CardMoveManager(this.camera, this.meshees, this.cards, this.renderer);
   }

   appendCards() {
      console.log("ee")
      let count = 1
      this.drawnCards.forEach(element => {
         let strCard = ""
         if (element.name == "Joker") {
            strCard = element.color + "_joker";
         } else {
            strCard = element.color + "_" + element.name;
         }
         let card = new Card(
            this.cards_resources[strCard].mesh,
            new Vector3(this.boardMap.map[13][count].xPos, BOARD_POSITION.y + (BOARD_SIZE.height / 2), this.boardMap.map[13][count].zPos),
            this.meshees
         );
         this.cards.push(card);
         this.scene.add(card)
         count++;
      });
      this.whileGame();
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
      console.log(this.boardMap.map)

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