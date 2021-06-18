import {
   BackSide,
   Box3,
   BufferGeometry,
   Group,
   Mesh,
   MeshBasicMaterial,
   MeshPhongMaterial,
   Object3D,
   Vector3,
} from "three"

export default class Card extends Object3D {
   // export default class Card extends Mesh {
   /**    
    * @param {Group} mesh 
    * @param {Vector3} position 
    * @param {import("./Game_Board").default} board        
    */
   constructor(mesh, position, board) {

      // let {geometry, material} = mesh
      // super(geometry, material)
      super();
      this.mesh = mesh.clone();
      // console.log(this.mesh.children)
      this.mesh.children.forEach(element => {
         element.material.forEach(( /**@type {MeshPhongMaterial} */ el) => {
            // el.color.setHex(0x0388fc);
            el.shininess = 50;
            // el.side = BackSide;
            // el.emissive = 0x1a0559;
            // el.side = BackSide;
            // el.wireframe = true;

         });
      });
      // this.mesh.children[0].material[0].color.setHex(0xff0000);
      // this.mesh.children[0].color.setHex(0xff0000);
      this.add(this.mesh);

      let box = new Box3().setFromObject(this)

      let scale = (20 / box.getSize().x)
      this.scale.set(scale, scale, scale)
      this.position.copy(position)
      // this.rotateX(-Math.PI / 2)
      this.board = board;
   }
}