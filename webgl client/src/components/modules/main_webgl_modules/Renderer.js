import { PCFSoftShadowMap, WebGLRenderer } from 'three';
import Stats from 'three/examples/jsm/libs/stats.module.js';



// container for functions that will be performed during requestAnimationFrame

/** @type {{ [x:string]: ()=>void }} */
export let rendererFunctions = {}



export class Renderer extends WebGLRenderer {
   /**
    * @param {import("three").Scene} scene
    * @param {HTMLDivElement} container    
    */
   constructor(scene, container) {


      super({ antialias: true });
      this.setClearColor(0x000000);
      this.shadowMap.enabled = true
      this.shadowMap.type = PCFSoftShadowMap;


      this.scene = scene;
      this.container = container;

      this.container.appendChild(this.domElement);

      this.updateSize();
      document.addEventListener('DOMContentLoaded', () => this.updateSize(), false);
      window.addEventListener('resize', () => this.updateSize(), false);


      this.stats = Stats();
      this.stats.showPanel(0); // 0: fps, 1: ms, 2: mb

      document.body.appendChild(this.stats.dom);

   }

   updateSize() {
      this.setSize(window.innerWidth, window.innerHeight);
   }



   /**
    * @param {import("three").Scene} scene
    * @param {import("three").Camera} camera
    */
   render_update(scene, camera) {
      this.stats.begin()

      this.render(scene, camera);

      for (const key in rendererFunctions) {
         rendererFunctions[key]()
      }

      this.stats.end()
      requestAnimationFrame(() => { this.render_update(scene, camera) });
   }
}


// export class Renderer {
//    /**
//     * @param {import("three").Scene} scene
//     * @param {HTMLDivElement} container    
//     */
//    constructor(scene, container) {
//       this.scene = scene;
//       this.container = container;

//       this.threeRenderer = new WebGLRenderer({ antialias: true });
//       // this.threeRenderer.setClearColor(0x393939);
//       this.threeRenderer.setClearColor(0x000000);
//       this.threeRenderer.shadowMap.enabled = true
//       this.threeRenderer.shadowMap.type = PCFSoftShadowMap;

//       this.container.appendChild(this.threeRenderer.domElement);

//       this.updateSize();
//       document.addEventListener('DOMContentLoaded', () => this.updateSize(), false);
//       window.addEventListener('resize', () => this.updateSize(), false);


//       this.stats = Stats();
//       this.stats.showPanel(0); // 0: fps, 1: ms, 2: mb

//       document.body.appendChild(this.stats.dom);

//    }

//    updateSize() {
//       this.threeRenderer.setSize(window.innerWidth, window.innerHeight);
//    }



//    /**
//     * @param {import("three").Scene} scene
//     * @param {import("three").Camera} camera
//     */
//    render(scene, camera) {
//       this.stats.begin()


//       this.threeRenderer.render(scene, camera);

//       for (const key in rendererFunctions) {
//          rendererFunctions[key]()
//       }

//       this.stats.end()
//       requestAnimationFrame(() => { this.render(scene, camera) });
//    }
// }