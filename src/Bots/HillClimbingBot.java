package Bots;

import GameStateBetter.GameStateException;
import javafx.scene.control.Button;

public class HillClimbingBot extends Bot {

    public HillClimbingBot(Button[][] buttons, String playerType) {
        super(buttons, playerType);
    }

    public int[] move() {
        // find first empty tile
        var state = this.getGameState();

        int i = 0;
        int j = 0;

        while (i < 8) {
            if (state.getGameBoardMatrix()[i][j] == 0) {
                try {
                    this.getGameState().play(i, j, isPlayerOne());
                    return new int[]{i, j};
                } catch (GameStateException.IllegalMove | GameStateException.RowColumnOverFlow ignored) {
                }
            }

            j++;

            if (j == 8) {
                j = 0;
                i++;
            }
        }

        return new int[]{0, 0};
    }
}
