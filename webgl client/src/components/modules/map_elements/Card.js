import {
   BufferGeometry,
   Mesh,
   MeshBasicMaterial,
   MeshPhongMaterial,
   Object3D,
   Vector3
} from "three"

export default class Card extends Mesh {
   /**    
    * @param {BufferGeometry} geometry 
    * @param {Vector3} position 
    * @param {import("./Game_Board").default} board        
    */
   constructor(geometry, position, board) {
      let material = new MeshPhongMaterial({})


      super(geometry, material)

      this.position.copy(position)
      this.rotateX(-Math.PI / 2)
      this.board = board;
   }
}