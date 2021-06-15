import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js'
export default class addIntoRooms {
    constructor(camera, container, sendThruButton) {
        this.camera = camera;
        this.container = container;
        this.forEachNick = document.createElement('div');
        this.forEachNick.id = "forEachNick";
        this.divs = []
        this.toggled = false;
        document.body.appendChild(this.forEachNick);
        for (let i = 0; i < 4; i++) {
            let nickDiv = document.createElement('div');
            nickDiv.className = "nickDiv";
            this.divs.push(nickDiv);
            this.forEachNick.appendChild(nickDiv);
        }
        //add menu
        this.menu = document.createElement('div');
        this.menu.id = "menu";
        document.body.appendChild(this.menu);
        this.menuText = document.createElement('p');
        this.menuText.className = "menuText";
        this.menuText.innerText = "MENU";
        this.menu.appendChild(this.menuText);
        this.innerContent = document.createElement('div');
        this.innerContent.id = "innerContent";
        this.menu.appendChild(this.innerContent);
        //text
        this.innerContentText = document.createElement('span');
        this.innerContentText.id = "innerContentText";
        this.innerContent.appendChild(this.innerContentText);
        // this.innerContentText.innerText = "Orbit Controls: "
        this.menu.onmouseover = () => {
            this.innerContentText.innerText = "Orbit Controls: "
        }
        this.menu.onmouseout = () => {
            this.innerContentText.innerText = ""
        }

        this.toggleControls = document.createElement('div');
        this.toggleControls.id = "toggleControls";
        this.innerContent.appendChild(this.toggleControls);
        this.toggleHandle = document.createElement('div');
        this.toggleHandle.id = "toggleHandle";
        this.toggleControls.appendChild(this.toggleHandle);
        this.toggleControls.onclick = () => {
            this.toggle();
        }

        this.divForButton = document.createElement('div')
        this.divForButton.id = "divForButton";
        document.body.appendChild(this.divForButton);
        this.buttonSend = document.createElement('button');
        this.buttonSend.id = "buttonSend";
        this.buttonSend.innerText = "Send";
        this.divForButton.appendChild(this.buttonSend);
        this.buttonSend.onclick = () => {
            sendThruButton();
        }
    }
    insertNicks(allNicks) {
        this.allNicks = allNicks;
        this.allNicks.forEach(element => {
            this.newText = document.createElement('p');
            this.newText.className = "newText";
            this.newText.innerText = element.name;
            let id = this.allNicks.indexOf(element);
            this.divs[id].appendChild(this.newText);
        });
    }
    toggle() {
        if (this.toggled == false) {
            this.toggleHandle.style.marginLeft = "50px";
            this.toggleControls.style.backgroundColor = "green";
            this.toggled = !this.toggled;
            this.cameraControls = new OrbitControls(this.camera, this.container);
        } else {
            this.toggled = !this.toggled;
            this.toggleHandle.style.marginLeft = "4px";
            this.toggleControls.style.backgroundColor = "red";
            this.cameraControls.enabled = false;
        }
    }

}