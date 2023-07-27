/**
 * @type {{[x:string]:(data)=>void}} 
 */
export const messageFunctions = {};

export class WS_Class extends WebSocket {
   constructor() {
      super(
         window.location.hostname == "localhost"
            ? `ws://${window.location.hostname}:5000/rummikub`
            : `wss://${window.location.hostname}/rummikub`
      )      


      this.onopen = this.onopen_ev.bind(this)
      // this.onmessage = this.onmessage_ev.bind(this)
      this.onerror = this.onerror_ev.bind(this)
      this.onclose = this.onclose_ev.bind(this)
      this.addEventListener("message", (e) => {
         console.log("-----------------");
         console.log("Test");
         console.log("-----------------");

         console.log(JSON.parse(e.data));

         console.log("-----------------");
         console.log("End");
         console.log("-----------------");
      })
   }


   /**
    * The function that executes when you connect to the websocket
    */
   onopen_ev() {
      console.log("onopen")
      setTimeout(() => {
         this.mySend("not idle", {})
         this.onopen_ev()
      }, 10000)
   }

   /**
    * Close connection
    * @param {CloseEvent} e 
    */
   onclose_ev(e) {
      console.log("onclose");
      console.log(e.code, e.reason);
   }

   /**
    * receiving data from the server
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
    * error handling   
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
 * @type {WS_Class} 
 */
export let my_WS = new WS_Class();