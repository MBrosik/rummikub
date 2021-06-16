export const FIELDS_COUNT = {
   x: 13,
   z: 15,
}


export const BOARD_POSITION = {
   x: 0,
   y: 200,
   z: 0,
}

export const BOARD_SIZE = {
   width: 400,
   height: 50,
   depth: 400,
}

export const SMALLER_SIZE_X = {
   width: BOARD_SIZE.width,
   height: BOARD_SIZE.height,
   depth: BOARD_SIZE.depth / 4,
}

export const SMALLER_SIZE_Y = {
   width: BOARD_SIZE.width / 4,
   height: BOARD_SIZE.height,
   depth: BOARD_SIZE.depth + BOARD_SIZE.depth / 2,
}

export const SMALLER_POSITION = [
   {
      x: 0,
      y: BOARD_POSITION.y,
      z: BOARD_SIZE.width / 2 + SMALLER_SIZE_X.depth / 2,
   },
   {
      x: BOARD_SIZE.width / 2 + SMALLER_SIZE_Y.width / 2,
      y: BOARD_POSITION.y,
      z: ((BOARD_SIZE.width / 2 - SMALLER_SIZE_Y.depth / 2) * -1) - SMALLER_SIZE_Y.width,
   },
   {
      x: (BOARD_SIZE.width / 2 - SMALLER_SIZE_X.width / 2) * -1,
      y: BOARD_POSITION.y,
      z: (BOARD_SIZE.width / 2 + SMALLER_SIZE_X.depth / 2) * -1,
   },
   {
      x: (BOARD_SIZE.width / 2 + SMALLER_SIZE_Y.width / 2) * -1,
      y: BOARD_POSITION.y,
      z: ((BOARD_SIZE.width / 2 - SMALLER_SIZE_Y.depth / 2) * -1) - SMALLER_SIZE_Y.width,
   }
]

export const FIELD = {
   x: (BOARD_SIZE.width / FIELDS_COUNT.x) / 2,  // względny środek prostokąta
   z: ((BOARD_SIZE.depth + SMALLER_SIZE_X.depth) / FIELDS_COUNT.z) / 2,  // względny środek prostokąta
   width: BOARD_SIZE.width / FIELDS_COUNT.x,
   depth: (BOARD_SIZE.depth + SMALLER_SIZE_X.depth) / FIELDS_COUNT.z,
}

