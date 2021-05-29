import Card from "../map_elements/Card";
import { board_size, fields_count } from "../settings/board_info";
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
    * @param {MouseEvent} e 
    */
   mousedown_ev(e) {
      if (this.selected_card != undefined) return

      console.log(this.cardsCameraColider.getIntersects(e));
      this.selected_card = this.cardsCameraColider.getIntersects(e)[0]?.object;

      if (this.selected_card != undefined) document.body.style.cursor = "grabbing"
   }


   /**
    * @param {MouseEvent} e 
    */
   mousemove_ev(e) {
      if (this.selected_card == undefined) return

      let { x, z } = this.game_board.position

      let startX = x - (board_size.width / 2);
      let startZ = z - (board_size.depth / 2);

      this.board_intersect = this.gameBoardCameraColider.getIntersects(e)[0]

      if (this.board_intersect == undefined) return;

      let pointX = this.board_intersect.point.x - startX;
      let pointZ = this.board_intersect.point.z - startZ;

      let field = {
         x: (board_size.width / fields_count.x) / 2,
         z: (board_size.depth / fields_count.z) / 2,
         width: board_size.width / fields_count.x,
         depth: board_size.depth / fields_count.z,
      }


      this.selected_card.position.x = startX + Math.floor((pointX / board_size.width) * fields_count.x - 0.01) * field.width + field.x
      this.selected_card.position.z = startZ + Math.floor((pointZ / board_size.depth) * fields_count.z - 0.01) * field.depth + field.z
   }



   /**
    * @param {MouseEvent} e 
    */
   mouseup_ev(e) {
      if (this.selected_card == undefined) return

      this.selected_card = undefined
      document.body.style.cursor = "grab"
   }
}