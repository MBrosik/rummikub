# Rummikub

## About
Clone of the 2d multiplayer game called rummikub

## Interface

![image](https://user-images.githubusercontent.com/63966121/172072177-ef809439-4754-4904-a5e3-f3396550f997.png)


Play [here](https://rummikub.fly.dev/game/index.html)


## Rules
- There are 108 tiles numbered from 1 to 13 in four colors: red, blue, yellow, and black. Each number has two copies in each color, making a total of 4 sets of tiles per number. There are also 4 Jokers.
- The game is played by 4 players.
- At the beginning of the game, each player draws 10 tiles from the pool.
- The objective of the game is to be the first player to get rid of all their tiles by forming valid sets or runs.
- A set consists of three or four tiles of the same number but different colors, while a run consists of three or more consecutive numbers of the same color.
- On the first laying out, the player must form set or run with a minimum value of 30. He cannot manipulate the board and using Jokers right now.
- Starting from the next turn, the player can add tiles from his hands to the layouts lying on the table, as well as perform manipulations, i.e. rebuild layouts lying on the table. However, he must put out at least one tile from his hands in each move. If he cannot do so, he takes one tile from the pool.
- Card in the set or run can be replaced by Joker.
- Time limit per turn is 60 seconds.


## Technologies

### Frontend

![Three.js](https://img.shields.io/badge/three.js-000000?style=for-the-badge&logo=three.js&logoColor=FFFFFF)
![Javascript](https://img.shields.io/badge/JavaScript-F7DF1E?logo=JavaScript&logoColor=black&style=for-the-badge)
![html](https://img.shields.io/badge/HTML5-E34F26?style=for-the-badge&logo=html5&logoColor=white)
![sass](https://img.shields.io/badge/Sass-CC6699?style=for-the-badge&logo=sass&logoColor=white)
![webpack](https://img.shields.io/badge/webpack%20-%238DD6F9.svg?&style=for-the-badge&logo=webpack&logoColor=black)

### Backend
![kotlin](https://img.shields.io/badge/kotlin-black.svg?&style=for-the-badge&logo=kotlin&logoColor=B322E9)
![h2](https://img.shields.io/badge/h2-001AE1.svg?&style=for-the-badge&logo=h2&logoColor=B322E9)

### Other
![Docker Badge](https://img.shields.io/badge/Docker-2496ED?logo=docker&logoColor=fff&style=for-the-badge)

## Warnings
- Game has no loading screen. You have to wait as the black screen disappears
- Works with IntelliJ 2021.2.3
