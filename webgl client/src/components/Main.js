import {
   Scene,
   AxesHelper,
   BufferGeometry
} from 'three';

import { Renderer } from './webgl/Renderer';
import Camera from './webgl/Camera';
import { Keyboard } from './webgl/utils/Keyboard_Manager';




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
      this.camera = new Camera(this.renderer.threeRenderer);
      this.keyboard = new Keyboard();



      [-1000, 1000].forEach(el => {
         var axes = new AxesHelper(el)
         this.scene.add(axes)
      });

      this.renderer.render(this.scene, this.camera.threeCamera)
      this.init()
   }
   async init() {
   }
}