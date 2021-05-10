import { DoubleSide, Mesh, MeshNormalMaterial, MeshPhongMaterial, PerspectiveCamera, SphereGeometry, Vector3 } from 'three';


export default class Camera {
   /**
    * @param {import("three").WebGLRenderer} threeRenderer
    */
   constructor(threeRenderer) {
      const width = threeRenderer.domElement.width;
      const height = threeRenderer.domElement.height;

      this.threeRenderer = threeRenderer;

      this.threeCamera = new PerspectiveCamera(75, width / height, 0.1, 10000);
      this.threeCamera.lookAt(new Vector3(0, 0, 0))
      this.threeCamera.rotation.y = 50 * (Math.PI / 180)

      this.updateSize(threeRenderer);
      window.addEventListener('resize', () => this.updateSize(threeRenderer), false);
   }

   /**
    * @param {import("three").WebGLRenderer} renderer
    */
   updateSize(renderer = this.threeRenderer) {

      this.threeCamera.aspect = renderer.domElement.width / renderer.domElement.height;
      this.threeCamera.updateProjectionMatrix();
   }
}