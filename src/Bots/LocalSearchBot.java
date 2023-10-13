package Bots;

import javafx.scene.control.Button;

public class LocalSearchBot extends Bot{
    public LocalSearchBot(Button[][] buttons, String playerType) {
        super(buttons, playerType);
    }

    public int[] move() {
        // find first empty tile
        var state = this.getState();

        int i = 0;
        int j = 0;

        while (i < 8) {
            if (state[i][j].equals("")) {
                return new int[]{i, j};
            }

            j++;

            if (j == 8) {
                j = 0;
                i++;
            }
        }

        return new int[]{0,0};
    }
}
