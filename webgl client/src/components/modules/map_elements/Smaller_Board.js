import {
    Box3,
    BoxGeometry,
    Mesh,
    MeshPhongMaterial,
    TextureLoader
} from "three";
import { BOARD_SIZE, BOARD_POSITION, SMALLER_SIZE_X, SMALLER_SIZE_Y, SMALLER_POSITION } from "../settings/board_info";
import board1 from "../../../resources/images/boardv2/Metal030_1K_Color.jpg";
import board2 from "../../../resources/images/boardv2/Metal030_1K_Displacement.jpg"
import board3 from "../../../resources/images/boardv2/Metal030_1K_Normal.jpg"
import board4 from "../../../resources/images/boardv2/Metal030_1K_Roughness.jpg"
import board5 from "../../../resources/images/boardv2/Metal030_1K_Metalness.jpg"
export default class Game_Board extends Mesh {
    constructor(index) {
        // let { width, height, depth } = SMALLER_SIZE_X
        // let { width, height, depth } = SMALLER_SIZE_Y
        let geometry = null;
        if (index == 0 || index == 2) {
            geometry = new BoxGeometry(SMALLER_SIZE_X.width, SMALLER_SIZE_X.height, SMALLER_SIZE_X.depth)
        } else {
            geometry = new BoxGeometry(SMALLER_SIZE_Y.width, SMALLER_SIZE_Y.height, SMALLER_SIZE_Y.depth)
        }
        let material = null;
        if (index == 1 || index == 3) {
            let txt1 = new TextureLoader().load(board3)
            let txt2 = new TextureLoader().load(board2)
            let txt3 = new TextureLoader().load(board1)
            let txt4 = new TextureLoader().load(board4)
            let txt5 = new TextureLoader().load(board5)
            txt1.rotation = Math.PI / 2
            txt2.rotation = Math.PI / 2
            txt3.rotation = Math.PI / 2
            txt4.rotation = Math.PI / 2
            txt5.rotation = Math.PI / 2
            material = new MeshPhongMaterial({
                color: 0x333333,
                shininess: 60,
                normalMap: txt1,
                displacementMap: txt2,
                // aoMap: new TextureLoader().load(board4),
                map: txt3,
                bumpMap: txt4,
                metalness: txt5
            })
        } else {
            material = new MeshPhongMaterial({
                color: 0x333333,
                shininess: 60,
                normalMap: new TextureLoader().load(board3),
                displacementMap: new TextureLoader().load(board2),
                // aoMap: new TextureLoader().load(board4),
                map: new TextureLoader().load(board1),
                bumpMap: new TextureLoader().load(board4),
                metalness: new TextureLoader().load(board5)
            })
        }
        super(geometry, material)
        this.position.set(SMALLER_POSITION[index].x, SMALLER_POSITION[index].y, SMALLER_POSITION[index].z);

        let wireframe = new Mesh(geometry.clone(), material.clone())
        // wireframe.material.wireframe = true;
        // wireframe.material.color.set(0x005f9e)

        // this.add(wireframe);
        let box = new Box3().setFromObject(this);
        console.log(box.getSize())
    }
}