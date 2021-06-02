import Card from "../map_elements/Card";
import { BOARD_SIZE, FIELDS_COUNT } from "../settings/board_info";
import CameraColider from "../utils/CameraColider";


export default class CardMoveManager {
   /**
    * @param {import("../main_webgl_modules/Camera").default} this.camera
    * @param {Card[]} cards 
    * @param {import("../map_elements/Game_Board").default} game_board 
    */
   constructor(camera, game_board, cards) {
      this.camera = camera
      this.game_board = game_board;
      this.cards = cards;

      /**@type {Card} */
      this.selected_card;
      /**@type {import("three").Intersection} */
      this.board_intersect;


      // ------------------
      // camera Coliders
      // ------------------
      this.cardsCameraColider = new CameraColider(this.camera, ...this.cards)
      this.gameBoardCameraColider = new CameraColider(this.camera, this.game_board)


      // ------------------
      // event listeners
      // ------------------
      this.mousedown_ev_bind = this.mousedown_ev.bind(this)
      this.mousemove_ev_bind = this.mousemove_ev.bind(this)
      this.mouseup_ev_bind = this.mouseup_ev.bind(this)

      window.addEventListener("mousedown", this.mousedown_ev_bind)
      window.addEventListener("mousemove", this.mousemove_ev_bind)
      window.addEventListener("mouseup", this.mouseup_ev_bind)
   }



   /**
    * @description Funkcja wykonująca się na naciśnięcie myszy
    * @param {MouseEvent} e 
    */
   mousedown_ev(e) {
      if (this.selected_card != undefined) return

      console.log(this.cardsCameraColider.getIntersects(e));
      this.selected_card = this.cardsCameraColider.getIntersects(e)[0]?.object;

      if (this.selected_card != undefined) document.body.style.cursor = "grabbing"
   }




   /**
    * @description Funkcja wykonująca się na poruszenie się myszy    
    * @param {MouseEvent} e 
    */
   mousemove_ev(e) {
      if (this.selected_card == undefined) return

      let { x, z } = this.game_board.position

      let startX = x - (BOARD_SIZE.width / 2);
      let startZ = z - (BOARD_SIZE.depth / 2);

      this.board_intersect = this.gameBoardCameraColider.getIntersects(e)[0]

      if (this.board_intersect == undefined) return;

      let pointX = this.board_intersect.point.x - startX;
      let pointZ = this.board_intersect.point.z - startZ;

      let field = {
         x: (BOARD_SIZE.width / FIELDS_COUNT.x) / 2,
         z: (BOARD_SIZE.depth / FIELDS_COUNT.z) / 2,
         width: BOARD_SIZE.width / FIELDS_COUNT.x,
         depth: BOARD_SIZE.depth / FIELDS_COUNT.z,
      }

      let x_floor = Math.max(0, Math.floor((pointX / BOARD_SIZE.width) * FIELDS_COUNT.x - 0.01) * field.width);
      let y_floor = Math.max(0, Math.floor((pointZ / BOARD_SIZE.depth) * FIELDS_COUNT.z - 0.01) * field.depth);

      this.selected_card.position.x = startX + x_floor + field.x
      this.selected_card.position.z = startZ + y_floor + field.z
   }



   /**
    * @description Funkcja wykonująca się na podniesienie myszy    
    * @param {MouseEvent} e 
    */
   mouseup_ev(e) {
      if (this.selected_card == undefined) return

      this.selected_card = undefined
      document.body.style.cursor = "grab"
   }
}