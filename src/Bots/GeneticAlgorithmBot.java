package Bots;

import javafx.scene.control.Button;

public class GeneticAlgorithmBot extends Bot {
    public GeneticAlgorithmBot(Button[][] buttons, String playerType) {
        super(buttons, playerType);
    }

    public int[] move() {
        // find first empty tile
        var state = this.getGameState();


        int i = 0;
        int j = 0;

        while (i < 8) {
            if (state.getGameBoardMatrix()[i][j] == 0) {
                return new int[]{i, j};
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
