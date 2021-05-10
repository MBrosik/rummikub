export class My_Fetch {


   /**
    * @param {RequestInfo} link
    */
   static async get(link) {
      return await fetch(link).then(res => res.json())
   }



   /**
    * @param {RequestInfo} link
    * @param {any} data
    */
   static async post(link, data) {
      return await fetch(link, {
         method: "POST",
         headers: {
            "Content-Type": "application/json"
         },
         body: JSON.stringify(data)
      }).then(res => res.json())
   }
}