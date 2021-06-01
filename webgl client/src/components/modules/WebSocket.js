/**
 * @description Funckje wykonujące się podczas odbierania wiadomości 
 * @type {{[x:string]:(data)=>void}} 
 */
export const messageFunctions = {};



export class WS extends WebSocket {
   constructor() {
      // super(`ws://${location.hostname}:${location.port}/rummikub`)
      super(`ws://${location.hostname}:${5000}/rummikub`)
   }


   /**
    * @description Funkcja, która wykonuje się po połączeniu się z websocketem
    */
   onopen() {
      this.send(JSON.stringify({
         type: "OnOpen",
         info: {
            message: "siemka",
         }
      }))
   }



   /**
    * @description odbieranie danych z serwera
    * @param {MessageEvent} e 
    */
   onmessage(e) {
      console.log(e.data)

      for (const key in messageFunctions) {
         messageFunctions[key](e.data);
      }
   }



   /**
    * @description obsługa błędów    
    * @param {Event} e 
    */
   onerror(e) {
      console.log(e.message)
   }



   /**
    * @description Zamknięcie połączenia    
    * @param {CloseEvent} e 
    */
   onclose(e) {
      console.log(e.code, e.reason);
   }
}
