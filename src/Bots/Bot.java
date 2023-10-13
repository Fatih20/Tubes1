package Bots;

import javafx.scene.control.Button;
import javafx.scene.control.Labeled;

import java.util.Arrays;

abstract public class Bot {
    private final Button[][] buttons;
    private final String playerType;

    public Bot(Button[][] buttons, String playerType) {
        this.buttons = buttons;
        this.playerType = playerType;
    }

    protected String[][] getState() {
        /*
         * Three possible value:
         * Empty, X, and O
         */
        String[][] state = new String[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Button button = this.buttons[i][j];
                state[i][j] = button.getText();
            }
        }

        return state;
    }

    abstract public int[] move();
}
