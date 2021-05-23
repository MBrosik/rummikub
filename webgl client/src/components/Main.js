import {
   Scene,
   AxesHelper,
   BufferGeometry
} from 'three';

import { Renderer } from './modules/main_webgl_modules/Renderer';
import Camera from './modules/main_webgl_modules/Camera';
import { Keyboard } from './modules/main_webgl_modules/Keyboard_Manager';
import { ws } from './modules/utils/WebSocket';



// import {STLLoader} from 'three/examples/jsm/loaders/STLLoader'

// const loader = new STLLoader();
// loader.load( './models/stl/ascii/slotted_disk.stl', function ( geometry ) {}


export default class Main {
   /**
    * @param {HTMLDivElement} container
    */
   constructor(container) {
      // -------------------
      // main WEBGL classes
      // -------------------
      this.container = container;
      this.scene = new Scene();
      this.renderer = new Renderer(this.scene, container);
      this.camera = new Camera(this.renderer);
      this.keyboard = new Keyboard();


      [-1000, 1000].forEach(el => {
         var axes = new AxesHelper(el)
         this.scene.add(axes)
      });

      this.renderer.render(this.scene, this.camera.threeCamera)


      /**
       * webSocket
       */

      ws = new WebSocket(`ws://${location.hostname}:4000`)

      ws.onopen = () => {
         ws.send(JSON.stringify(
            {

            }
         ))
      }


      this.init()
   }
   async init() {
   }
}