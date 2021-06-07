/**
 * Funkcje wykonujące się podczas odbierania wiadomości 
 * @type {{[x:string]:(data)=>void}} 
 */
// export const messageFunctions = {};

export class WS_Class extends WebSocket {
   constructor() {

      /**
       * Wywołanie konstructora 
       */
      super(`ws://${location.hostname}:${5000}/rummikub`)
      // super(`wss://rumikub.herokuapp.com/rummikub`)      


      /**
       * Funkcje odpowiadające za nasłuch
       */

      this.onopen = this.onopen_ev.bind(this)
      // this.onmessage = this.onmessage_ev.bind(this)
      this.onerror = this.onerror_ev.bind(this)
      this.onclose = this.onclose_ev.bind(this)
   }


   /**
    * Funkcja, która wykonuje się po połączeniu się z websocketem
    */
   onopen_ev() {
      console.log("onopen")
   }

   /**
    * Zamknięcie połączenia
    * @param {CloseEvent} e 
    */
   onclose_ev(e) {
      console.log("onclose");
      console.log(e.code, e.reason);
   }

   /**
    * odbieranie danych z serwera
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
    * obsługa błędów    
    * @param {Event} e 
    */
   onerror_ev(e) {
      console.log(e.message)
   }



   mySend(type, data) {
      this.send(JSON.stringify({ type, data }))
   }

}



/**
 * Tutaj znajdzie się instacja klasy
 * @type {WS_Class} 
 */
// export let my_WS = null;
export let my_WS = new WS_Class();