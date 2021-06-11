export default class addIntoRooms {
    constructor() {
        this.forEachNick = document.createElement('div');
        this.forEachNick.id = "forEachNick";
        this.divs = []
        document.body.appendChild(this.forEachNick);
        for (let i = 0; i < 4; i++) {
            let nickDiv = document.createElement('div');
            nickDiv.className = "nickDiv";
            this.divs.push(nickDiv);
            this.forEachNick.appendChild(nickDiv);
        }
    }
    insertNicks(allNicks) {
        this.allNicks = allNicks;
        this.allNicks.forEach(element => {
            this.newText = document.createElement('p');
            this.newText.className = "newText";
            this.newText.innerText = element.nick;
            let id = this.allNicks.indexOf(element);
            this.divs[id].appendChild(this.newText);
        });
    }
}