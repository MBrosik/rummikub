import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js'
export default class addIntoRooms {
    constructor(camera, container, sendThruButton) {
        this.camera = camera;
        this.container = container;
        this.forEachNick = document.createElement('div');
        this.forEachNick.id = "forEachNick";
        this.divs = []
        this.timerDivsOut = []
        this.timerDivS = []
        this.toggled = false;
        document.body.appendChild(this.forEachNick);
        for (let i = 0; i < 4; i++) {
            let forNicksNow = document.createElement('div');
            forNicksNow.className = "forNicksNow";
            this.forEachNick.appendChild(forNicksNow);
            let nickDiv = document.createElement('div');
            nickDiv.className = "nickDiv";
            console.log("eee2")
            nickDiv.style.backgroundColor = "rgba(194, 194, 194, 0.5)";
            nickDiv.innerHTML = "nieaktywny...";
            this.divs.push(nickDiv);
            forNicksNow.appendChild(nickDiv);
            this.timerDivsOut.push(forNicksNow);
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
        //divForButton
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
    insertNicks(allNicks, readyDivs) {
        this.allNicks = allNicks;
        this.allNicks.forEach(element => {
            if (element != null) {
                let id = this.allNicks.indexOf(element);
                console.log("eee")
                this.divs[id].innerHTML = "";
                this.divs[id].style.backgroundColor = "rgba(2, 72, 224, 0.5)";
                this.newText = document.createElement('p');
                this.newText.className = "newText";
                this.newText.innerText = element.name;
                this.divs[id].appendChild(this.newText);
            } else {
                let id = this.allNicks.indexOf(element);
                this.divs[id].innerHTML = "";
                // this.divs[id].style.backgroundColor = "rgba(194, 194, 194, 0.5)";
                // this.newText = document.createElement('p');
                // this.newText.className = "newText";
                this.divs[id].innerHTML = "nieaktywny...";
                // this.divs[id].appendChild(this.newText);
            }
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
    addButton() {
        this.buttonSend.style.right = 0;
    }
    removeButton() {
        this.buttonSend.style.right = "-150px";
    }
    addTimer(turn) {
        this.timerDivS = [];
        this.timerDiv = document.createElement('div');
        this.timerDiv.className = "timerDiv";
        this.timerDiv.innerText = "";
        this.timerDivS.push(this.timerDiv)
        this.timerDivsOut[turn].appendChild(this.timerDiv);
    }
    removeTimer() {
        let allThem = document.querySelectorAll(".timerDiv");
        allThem.forEach(element => {
            element.remove();
        });
    }
    addWinnerDiv(text) {
        this.winnerDiv = document.createElement('div');
        this.winnerDiv.id = "winnerDiv";
        this.innerTextWin = document.createElement('p');
        this.innerTextWin.innerText = text;
        document.body.appendChild(this.winnerDiv);
        this.winnerDiv.appendChild(this.innerTextWin);
        this.buttonReset = document.createElement('div')
        this.buttonReset.id = "buttonReset";
        this.buttonReset.innerText = "Zagraj jeszczę raz"
        this.buttonReset.onclick = () => {
            window.location.reload();
        }
        this.winnerDiv.appendChild(this.buttonReset);
    }

}