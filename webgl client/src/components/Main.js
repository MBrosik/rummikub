import {
   Scene,
   AxesHelper,
   BufferGeometry,
   DirectionalLight,
   Vector3
} from 'three';

import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js'

import { Renderer } from './modules/main_webgl_modules/Renderer';
import Camera from './modules/main_webgl_modules/Camera';
import { Keyboard } from './modules/main_webgl_modules/Keyboard_Manager';
import { ws, WS_Class } from './modules/WebSocket';
import LoadCards from './modules/utils/LoadCards';
import Game_Board from './modules/map_elements/Game_Board';
import Card from './modules/map_elements/Card';
import { BOARD_SIZE } from './modules/settings/board_info';
import CardMoveManager from './modules/after_game/CardMoveManager';



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


      // -----------------------
      // orbit Controls
      // -----------------------
      // const controls = new OrbitControls(this.camera, this.renderer.domElement);

      // -------------------
      // webSocket
      // -------------------

      ws = new WS_Class();

      window.ws = ws;

      // this.ws.onopen = () => { console.log("siemka") }

      // let aa = new WebSocket(`ws://${location.hostname}:${5000}/rummikub`);

      // aa.onopen = () => { console.log("siemka") }


      // --------------------
      // game_board, cards
      // --------------------
      /**@type {import("./modules/utils/LoadCards").cardObject} */
      this.cards_resources;
      /**@type {Card[]} */
      this.cards = []
      /**@type {Game_Board} */
      this.game_board;


      // --------------------
      // Card_move_manager
      // --------------------
      /**@type {CardMoveManager} */
      this.card_move_manager = undefined;


      this.init()
   }
   async init() {
      this.cards_resources = await LoadCards();
      console.log(this.cards_resources)

      await this.createMap();

      this.afterGame();
   }


   afterGame() {

      this.card_move_manager = new CardMoveManager(this.camera, this.game_board, this.cards);
   }


   async createMap() {


      // ------------
      // light
      // ------------
      this.light = new DirectionalLight(0xffff00, 10);
      this.light.position.set(50, 50, 50);
      this.scene.add(this.light)


      // ----------------------
      // append Cards
      // ----------------------
      let card = new Card(
         this.cards_resources["Card1"].geometry,
         new Vector3(100, (BOARD_SIZE.height / 2), 100),
         this.game_board
      );
      this.cards.push(card);
      this.scene.add(card)

      {
         let card = new Card(
            this.cards_resources["Card1"].geometry,
            new Vector3(0, (BOARD_SIZE.height / 2), 0),
            this.game_board
         );
         this.cards.push(card);
         this.scene.add(card)
      }


      console.log("siemka")
      this.game_board = new Game_Board();
      this.scene.add(this.game_board)
   }
}