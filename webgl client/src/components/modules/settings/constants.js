export const cardSize = 500;

export const host = window.location.hostname == "localhost" 
   ? `http://${window.location.hostname}:5000` 
   : `https://${window.location.hostname}`