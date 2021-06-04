/**
 * @description Funckje wykonujące się podczas odbierania wiadomości 
 * @type {{[x:string]:(data)=>void}} 
 */
export const messageFunctions = {};


export const my_WS = {
   /**@type {WS_Class} */
   el: null
}


export class WS_Class extends WebSocket {
   constructor() {
      // super(`ws://${location.hostname}:${location.port}/rummikub`)
      super(`ws://${location.hostname}:${5000}/rummikub`)
      // super(`wss://rumikub.herokuapp.com/rummikub`)
      console.log("siemka");

      this.onopen = this.onopen_ev.bind(this)
      this.onmessage = this.onmessage_ev.bind(this)
      this.onerror = this.onerror_ev.bind(this)
      this.onclose = this.onclose_ev.bind(this)
   }


   /**
    * @description Funkcja, która wykonuje się po połączeniu się z websocketem
    */
   onopen_ev() {
      console.log("onopen")
      this.send(JSON.stringify({
         type: "Pokoje",
         info: {
            message: "siemka",
         }
      }))

   }



   /**
    * @description odbieranie danych z serwera
    * @param {MessageEvent} e 
    */
   onmessage_ev(e) {
      console.log("onmessage");
      console.log(e.data)

      for (const key in messageFunctions) {
         messageFunctions[key](e.data);
      }

   }



   /**
    * @description obsługa błędów    
    * @param {Event} e 
    */
   onerror_ev(e) {
      console.log(e.message)
   }



   /**
    * @description Zamknięcie połączenia    
    * @param {CloseEvent} e 
    */
   onclose_ev(e) {
      console.log("onclose");
      console.log(e.code, e.reason);
   }
}
