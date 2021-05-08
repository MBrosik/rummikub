import './style.scss';

import Main from './components/Main';

function init() {
   /**@type {HTMLDivElement}*/
   // @ts-ignore
   const container = document.getElementById('root');
   let main = new Main(container);

}

init();