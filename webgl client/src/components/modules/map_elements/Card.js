import {
   BufferGeometry,
   Group,
   Mesh,
   MeshBasicMaterial,
   MeshPhongMaterial,
   Object3D,
   Vector3
} from "three"

export default class Card extends Object3D {
   /**    
    * @param {Group} geometry 
    * @param {Vector3} position 
    * @param {import("./Game_Board").default} board        
    */
   constructor(mesh, position, board) {


      // super(geometry, material)
      super();
      this.add(mesh);

      this.position.copy(position)
      this.rotateX(-Math.PI / 2)
      this.board = board;
   }
}