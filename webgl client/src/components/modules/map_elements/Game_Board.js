import {
   BoxGeometry,
   Mesh,
   MeshPhongMaterial,
   TextureLoader
} from "three";
import { BOARD_SIZE } from "../settings/board_info";
import board1 from "../../../resources/images/board/Wood026_2K_Color.jpg";
import board2 from "../../../resources/images/board/Wood026_2K_Displacement.jpg"
import board3 from "../../../resources/images/board/Wood026_2K_Normal.jpg"
import board4 from "../../../resources/images/board/Wood026_2K_Roughness.jpg"

export default class Game_Board extends Mesh {
   constructor() {

      let { width, height, depth } = BOARD_SIZE
      let geometry = new BoxGeometry(width, height, depth)
      let material = new MeshPhongMaterial({
         color: 0xffffff,
         shininess: 100,
         normalMap: new TextureLoader().load(board3),
         displacementMap: new TextureLoader().load(board2),
         // aoMap: new TextureLoader().load(board4),
         map: new TextureLoader().load(board1),
         bumpMap: new TextureLoader().load(board4)
      })

      super(geometry, material)


      let wireframe = new Mesh(geometry.clone(), material.clone())
      wireframe.material.wireframe = true;
      wireframe.material.color.set(0x005f9e)

      this.add(wireframe);
   }
}