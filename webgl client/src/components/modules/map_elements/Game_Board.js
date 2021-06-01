import {
   BoxGeometry,
   Mesh,
   MeshBasicMaterial
} from "three";
import { BOARD_SIZE } from "../settings/board_info";


export default class Game_Board extends Mesh {
   constructor() {

      let { width, height, depth } = BOARD_SIZE
      let geometry = new BoxGeometry(width, height, depth)
      let material = new MeshBasicMaterial({ color: 0xffffff })

      super(geometry, material)


      let wireframe = new Mesh(geometry.clone(), material.clone())
      wireframe.material.wireframe = true;
      wireframe.material.color.set(0xff00ff)

      this.add(wireframe);
   }
}