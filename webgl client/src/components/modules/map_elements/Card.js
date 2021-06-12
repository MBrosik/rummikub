import {
   Box3,
   BufferGeometry,
   Group,
   Mesh,
   MeshBasicMaterial,
   MeshPhongMaterial,
   Object3D,
   Vector3
} from "three"

export default class Card extends Object3D {
   // export default class Card extends Mesh {
   /**    
    * @param {Group} geometry 
    * @param {Vector3} position 
    * @param {import("./Game_Board").default} board        
    */
   constructor(mesh, position, board) {

      // let {geometry, material} = mesh
      // super(geometry, material)
      super();
      this.add(mesh.clone());
      // this.cop

      let box = new Box3().setFromObject(this)

      let scale = (20 / box.getSize().x)
      this.scale.set(scale, scale, scale)
      this.position.copy(position)
      // this.rotateX(-Math.PI / 2)
      this.board = board;
   }
}