
export const Keys = {
   KeyA: false,
   KeyD: false,
   KeyW: false,
   KeyS: false,
   KeyQ: false,
   KeyE: false,
   Numpad8: false,
   Numpad5: false,

   Numpad4: false,
   Numpad6: false,

   Numpad7: false,
   Numpad9: false,
}

export class Keyboard {
   constructor() {

      window.onkeydown = (e) => {
         Keys[e.code] = true
      }

      window.onkeyup = (e) => {
         Keys[e.code] = false;
      }
   }
}