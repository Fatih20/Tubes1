package Bots;

import GameStateBetter.GameStateBetter;
import javafx.scene.control.Button;

abstract public class Bot {
    private GameStateBetter gameState;
    private final String playerType;

    public Bot(Button[][] buttons, String playerType) {
        String[][] state = new String[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                state[i][j] = buttons[i][j].getText();
            }
        }

        this.gameState = new GameStateBetter(state);
        this.playerType = playerType;
    }

    public Bot(GameStateBetter gameState, String playerType) {
        this.gameState = gameState;
        this.playerType = playerType;
    }



    abstract public int[] move();

    public GameStateBetter getGameState() {
        return gameState;
    }
}
