package Bots;

import GameStateBetter.GameStateBetter;
import javafx.scene.control.Button;
import javafx.scene.control.Labeled;

import java.util.Arrays;

abstract public class Bot {
    private GameStateBetter gameState;
    private final String playerType;

    public Bot(Button[][] buttons, String playerType) {
        String[][] state = new String[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Button button = this.buttons[i][j];
                state[i][j] = button.getText();
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
