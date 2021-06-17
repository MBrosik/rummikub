import { Box3, Object3D } from "three";
import { BOARD_POSITION, BOARD_SIZE } from "../settings/board_info";

export default class CardHolder extends Object3D {
   constructor(fbx, x, z, rotation) {
      super();

      this.fbx = fbx.clone()
      this.add(this.fbx);

      let box = new Box3().setFromObject(this)

      this.position.set(x, BOARD_POSITION.y + BOARD_SIZE.height / 2, z);
      this.rotateY(rotation);




      let scale = (200 / box.getSize().x)
      this.scale.set(scale, scale, scale)
   }
}