import { STLLoader } from 'three/examples/jsm/loaders/STLLoader'
import { BufferGeometry } from 'three'

import Card1 from "../../../resources/models/Cards/rummikub 1_fixed.stl"
import Card2 from "../../../resources/models/Cards/rummikub 2_fixed.stl"
import Card3 from "../../../resources/models/Cards/rummikub 3_fixed.stl"
import Card4 from "../../../resources/models/Cards/rummikub 4_fixed.stl"
import Card5 from "../../../resources/models/Cards/rummikub 5_fixed.stl"
import Card6 from "../../../resources/models/Cards/rummikub 6_fixed.stl"
import Card7 from "../../../resources/models/Cards/rummikub 7_fixed.stl"
import Card8 from "../../../resources/models/Cards/rummikub 8_fixed.stl"
import Card9 from "../../../resources/models/Cards/rummikub 9_fixed.stl"
import Card10 from "../../../resources/models/Cards/rummikub 10_fixed.stl"
import Card11 from "../../../resources/models/Cards/rummikub 11_fixed.stl"
import Card12 from "../../../resources/models/Cards/rummikub 12_fixed.stl"
import Card13 from "../../../resources/models/Cards/rummikub 13_fixed.stl"
import Joker from "../../../resources/models/Cards/rummikub zon_fixed.stl"

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
   let cardsLinks = { Card1, Card2, Card3, Card4, Card5, Card6, Card7, Card8, Card9, Card10, Card11, Card12, Card13, Joker }
   /**@type {cardObject} */
   let cardObject = {};
   /**@type {Promise<any>[]} */
   let promiseTable = [];


   for (const link in cardsLinks) {

      promiseTable.push(
         new Promise(resolve => {

            const loader = new STLLoader();
            loader.load(cardsLinks[link], function (geometry) {
               cardObject[link] = { geometry }
               resolve(true);
            })
         })
      )
   }


   await Promise.all(promiseTable)

   return cardObject
}