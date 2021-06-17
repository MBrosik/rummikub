import { FBXLoader } from 'three/examples/jsm/loaders/FBXLoader';
import { BufferGeometry, Group } from 'three'

import cardHolder from "../../../resources/models/tabliczka.fbx"



export default async function loadCardHolder() {

   /**@type {Group} */
   let mesh;
   await new Promise(resolve => {
      const loader = new FBXLoader();
      loader.load(cardHolder, function (obj) {
         // cardObject[link] = { mesh }
         mesh = obj
         resolve(true);
      })
   })

   return mesh;
}