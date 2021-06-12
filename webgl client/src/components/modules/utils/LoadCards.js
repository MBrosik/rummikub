// import { STLLoader } from 'three/examples/jsm/loaders/STLLoader'
import { FBXLoader } from 'three/examples/jsm/loaders/FBXLoader';
import { BufferGeometry, Group } from 'three'

import black_1 from "../../../resources/models/Cards/FBX do zamiany/1-black.fbx"
import blue_1 from "../../../resources/models/Cards/FBX do zamiany/1-blue.fbx"
import red_1 from "../../../resources/models/Cards/FBX do zamiany/1-red.fbx"
import orange_1 from "../../../resources/models/Cards/FBX do zamiany/1-orange.fbx"
import black_2 from "../../../resources/models/Cards/FBX do zamiany/2-black.fbx"
import blue_2 from "../../../resources/models/Cards/FBX do zamiany/2-blue.fbx"
import red_2 from "../../../resources/models/Cards/FBX do zamiany/2-red.fbx"
import orange_2 from "../../../resources/models/Cards/FBX do zamiany/2-orange.fbx"
import black_3 from "../../../resources/models/Cards/FBX do zamiany/3-black.fbx"
import blue_3 from "../../../resources/models/Cards/FBX do zamiany/3-blue.fbx"
import red_3 from "../../../resources/models/Cards/FBX do zamiany/3-red.fbx"
import orange_3 from "../../../resources/models/Cards/FBX do zamiany/3-orange.fbx"
import black_4 from "../../../resources/models/Cards/FBX do zamiany/4-black.fbx"
import blue_4 from "../../../resources/models/Cards/FBX do zamiany/4-blue.fbx"
import red_4 from "../../../resources/models/Cards/FBX do zamiany/4-red.fbx"
import orange_4 from "../../../resources/models/Cards/FBX do zamiany/4-orange.fbx"
import black_5 from "../../../resources/models/Cards/FBX do zamiany/5-black.fbx"
import blue_5 from "../../../resources/models/Cards/FBX do zamiany/5-blue.fbx"
import red_5 from "../../../resources/models/Cards/FBX do zamiany/5-red.fbx"
import orange_5 from "../../../resources/models/Cards/FBX do zamiany/5-orange.fbx"
import black_6 from "../../../resources/models/Cards/FBX do zamiany/6-black.fbx"
import blue_6 from "../../../resources/models/Cards/FBX do zamiany/6-blue.fbx"
import red_6 from "../../../resources/models/Cards/FBX do zamiany/6-red.fbx"
import orange_6 from "../../../resources/models/Cards/FBX do zamiany/6-orange.fbx"
import black_7 from "../../../resources/models/Cards/FBX do zamiany/7-black.fbx"
import blue_7 from "../../../resources/models/Cards/FBX do zamiany/7-blue.fbx"
import red_7 from "../../../resources/models/Cards/FBX do zamiany/7-red.fbx"
import orange_7 from "../../../resources/models/Cards/FBX do zamiany/7-orange.fbx"
import black_8 from "../../../resources/models/Cards/FBX do zamiany/8-black.fbx"
import blue_8 from "../../../resources/models/Cards/FBX do zamiany/8-blue.fbx"
import red_8 from "../../../resources/models/Cards/FBX do zamiany/8-red.fbx"
import orange_8 from "../../../resources/models/Cards/FBX do zamiany/8-orange.fbx"
import black_9 from "../../../resources/models/Cards/FBX do zamiany/9-black.fbx"
import blue_9 from "../../../resources/models/Cards/FBX do zamiany/9-blue.fbx"
import red_9 from "../../../resources/models/Cards/FBX do zamiany/9-red.fbx"
import orange_9 from "../../../resources/models/Cards/FBX do zamiany/9-orange.fbx"
import black_10 from "../../../resources/models/Cards/FBX do zamiany/10-black.fbx"
import blue_10 from "../../../resources/models/Cards/FBX do zamiany/10-blue.fbx"
import red_10 from "../../../resources/models/Cards/FBX do zamiany/10-red.fbx"
import orange_10 from "../../../resources/models/Cards/FBX do zamiany/10-orange.fbx"
import black_11 from "../../../resources/models/Cards/FBX do zamiany/11-black.fbx"
import blue_11 from "../../../resources/models/Cards/FBX do zamiany/11-blue.fbx"
import red_11 from "../../../resources/models/Cards/FBX do zamiany/11-red.fbx"
import orange_11 from "../../../resources/models/Cards/FBX do zamiany/11-orange.fbx"
import black_12 from "../../../resources/models/Cards/FBX do zamiany/12-black.fbx"
import blue_12 from "../../../resources/models/Cards/FBX do zamiany/12-blue.fbx"
import red_12 from "../../../resources/models/Cards/FBX do zamiany/12-red.fbx"
import orange_12 from "../../../resources/models/Cards/FBX do zamiany/12-orange.fbx"
import black_13 from "../../../resources/models/Cards/FBX do zamiany/13-black.fbx"
import blue_13 from "../../../resources/models/Cards/FBX do zamiany/13-blue.fbx"
import red_13 from "../../../resources/models/Cards/FBX do zamiany/13-red.fbx"
import orange_13 from "../../../resources/models/Cards/FBX do zamiany/13-orange.fbx"
import black_joker from "../../../resources/models/Cards/FBX do zamiany/joker-black.fbx"
import red_joker from "../../../resources/models/Cards/FBX do zamiany/joker-red.fbx"

// import Card1 from "../../../resources/models/Cards/rummikub 1_fixed.stl"
// import Card2 from "../../../resources/models/Cards/rummikub 2_fixed.stl"
// import Card3 from "../../../resources/models/Cards/rummikub 3_fixed.stl"
// import Card4 from "../../../resources/models/Cards/rummikub 4_fixed.stl"
// import Card5 from "../../../resources/models/Cards/rummikub 5_fixed.stl"
// import Card6 from "../../../resources/models/Cards/rummikub 6_fixed.stl"
// import Card7 from "../../../resources/models/Cards/rummikub 7_fixed.stl"
// import Card8 from "../../../resources/models/Cards/rummikub 8_fixed.stl"
// import Card9 from "../../../resources/models/Cards/rummikub 9_fixed.stl"
// import Card10 from "../../../resources/models/Cards/rummikub 10_fixed.stl"
// import Card11 from "../../../resources/models/Cards/rummikub 11_fixed.stl"
// import Card12 from "../../../resources/models/Cards/rummikub 12_fixed.stl"
// import Card13 from "../../../resources/models/Cards/rummikub 13_fixed.stl"
// import Joker from "../../../resources/models/Cards/rummikub zon_fixed.stl"
// let colors = ["black", "blue", "orange", "red"]
// let count = 0;
// for (let i = 1; i <= 13; i++) {
//    colors.forEach(element => {
//       count++;
//       str = "../../../resources/models/Cards/FBX do zamiany/" + i + element + ".fbx"
//       str2 = card["c" + count]
//       import str2 from str
//    });
// }
// importALL(require.context('../../../resources/models/Cards/FBX do zamiany', true, /([1-13]|joker)-(black|blue|orange|red).fbx$/))


/**@typedef {{geometry:BufferGeometry}} card_resource */

/**
 * @typedef {Object} cardObject
 * @property {card_resource} Card1
 * @property {card_resource} Card2
 * @property {card_resource} Card3
 * @property {card_resource} Card4
 * @property {card_resource} Card5
 * @property {card_resource} Card6
 * @property {card_resource} Card7
 * @property {card_resource} Card8
 * @property {card_resource} Card9
 * @property {card_resource} Card10
 * @property {card_resource} Card11
 * @property {card_resource} Card12
 * @property {card_resource} Card13
 * @property {card_resource} Joker
*/

export default async function LoadCards() {
   let cardsLinks = {
      black_1, blue_1, red_1, orange_1, black_2, blue_2, red_2, orange_2, black_3, blue_3, red_3, orange_3, black_4, blue_4, red_4, orange_4, black_5, blue_5, red_5, orange_5, black_6, blue_6, red_6, orange_6, black_7, blue_7, red_7,
      orange_7, black_8, blue_8, red_8, orange_8, black_9, blue_9, red_9, orange_9, black_10, blue_10, red_10, orange_10, black_11, blue_11, red_11, orange_11, black_12, blue_12, red_12, orange_12, black_13, blue_13, red_13, orange_13,
      black_joker, red_joker,
   }
   /**@type {{[x:string]: {mesh:Group}}} */
   let cardObject = {};
   /**@type {Promise<any>[]} */
   let promiseTable = [];


   for (const link in cardsLinks) {

      promiseTable.push(
         new Promise(resolve => {

            const loader = new FBXLoader();
            loader.load(cardsLinks[link], function (mesh) {
               cardObject[link] = { mesh }
               resolve(true);
            })
         })
      )
   }


   await Promise.all(promiseTable)

   return cardObject
}