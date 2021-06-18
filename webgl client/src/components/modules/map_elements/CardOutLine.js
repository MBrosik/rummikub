import {
    BackSide,
    Box3,
    BufferGeometry,
    Group,
    Mesh,
    MeshBasicMaterial,
    MeshPhongMaterial,
    Object3D,
    Vector3,
} from "three"

export default class CardOutLine extends Object3D {
    // export default class Card extends Mesh {
    /**    
     * @param {Group} mesh 
     * @param {Vector3} position 
     * @param {import("./Game_Board").default} board        
     */
    constructor(mesh, position, board) {

        // let {geometry, material} = mesh
        // super(geometry, material)
        super();
        let newMesh = mesh.clone();
        // console.log(newMesh)
        newMesh.children.forEach(element => {
            element.material.forEach(( /**@type {MeshPhongMaterial} */ el, ind, arr) => {
                // let clone = el.clone()

                el.color.setHex(0xffffff);
                el.shininess = 0;
                el.transparent = true;
                el.opacity = 0.4;
                // el.side = BackSide;
                // el.specular = 0xbc06ea;
                // // el.emissive = 0x1a0559;
                // // el.side = BackSide;
                // // el.wireframe = true;
                // arr[ind] = clone;
            });
        });
        // this.mesh.children[0].material[0].color.setHex(0xff0000);
        // this.mesh.children[0].color.setHex(0xff0000);
        // newMesh.scale.multiplyScalar(0.5);
        this.add(newMesh);

        let box = new Box3().setFromObject(this)

        let scale = (20 / box.getSize().x)
        this.scale.set(scale * 1.05, scale * 0.9, scale * 1.05)
        this.position.copy(position)
        // this.rotateX(-Math.PI / 2)
        this.board = board;
    }
}