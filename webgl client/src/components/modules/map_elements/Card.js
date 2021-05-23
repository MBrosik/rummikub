import { Mesh, MeshPhongMaterial } from "three"

export default class Card extends Mesh {
   constructor(geometry) {
      let material = new MeshPhongMaterial({

      })

      super(geometry)
   }
}