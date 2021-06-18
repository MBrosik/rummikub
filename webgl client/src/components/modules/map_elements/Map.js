import { FIELDS_COUNT, BOARD_SIZE, FIELD, BOARD_POSITION } from '../settings/board_info';
export default class Map {
    constructor() {
        this.map = []
        for (let i = 0; i < FIELDS_COUNT.z; i++) {
            this.map[i] = [];
        }
        for (let i = 0; i < FIELDS_COUNT.z; i++) {
            for (let n = 0; n < FIELDS_COUNT.x; n++) {
                this.map[i][n] = { x: n, z: i, xPos: -1 * (BOARD_SIZE.width / 2) + FIELD.x + FIELD.width * n, yPos: BOARD_POSITION.y + (BOARD_SIZE.height / 2), zPos: -1 * (BOARD_SIZE.depth / 2) + FIELD.z + FIELD.depth * i, card: "", color: "", ID: "", out: "" };
            }
        }
    }
}
