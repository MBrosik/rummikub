export default class addIntoRooms {
    constructor(play) {
        this.startDiv = document.createElement('div');
        this.startDiv.id = "startDiv";
        document.body.appendChild(this.startDiv);
        this.createInputManager = document.createElement('div');
        this.createInputManager.id = "createInputManager";
        this.startDiv.appendChild(this.createInputManager);

        this.upperText = document.createElement('p');
        this.upperText.innerText = "Nowa gra";
        this.createInputManager.appendChild(this.upperText);
        this.inputNick = document.createElement('input');
        this.inputNick.id = "inputNick";
        this.inputNick.placeholder = "Podaj nick";
        this.createInputManager.appendChild(this.inputNick);
        this.newButton = document.createElement('button');
        this.newButton.id = "newButton";
        this.newButton.innerText = "Graj!"
        this.createInputManager.appendChild(this.newButton);

        this.newButton.onclick = () => {
            if (this.inputNick.value != null) {
                play(this.inputNick.value);
            }
        }
    }
}