import {
   Mesh,
   Raycaster,
   Vector2,
} from "three";

export default class CameraColider extends Raycaster {
   /**
    * 
    * @param {import("../main_webgl_modules/Camera").default} camera
    * @param {Mesh[]} meshes 
    */
   constructor(camera, ...meshes) {
      super()
      this.camera = camera;
      this.meshes = meshes;

      this.mouseVector = new Vector2()

   }
   /**
    * @param {MouseEvent} e 
    */
   getIntersects(e) {
      let x = (e.pageX / window.innerWidth) * 2 - 1;
      let y = -(e.pageY / window.innerHeight) * 2 + 1;

      this.mouseVector.set(x, y)
      this.setFromCamera(this.mouseVector, this.camera);

      return this.intersectObjects(this.meshes);

      // let aa = this.intersectObjects(this.meshes)[0];
   }
}