package Bots;

import GameStateBetter.GameStateBetter;
import GameStateBetter.GameStateException;
import javafx.scene.control.Button;

abstract public class Bot implements Runnable {
    private final GameStateBetter gameState;


    public boolean isPlayerOne() {
        return isPlayerOne;
    }

    private boolean isPlayerOne;

    private int[] lastMove;

    public Bot(Button[][] buttons, String playerType) {
        String[][] state = new String[8][8];

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                state[i][j] = buttons[i][j].getText();
            }
        }

        this.gameState = new GameStateBetter(state);
        this.isPlayerOne = playerType.equalsIgnoreCase("x");
    }

    public Bot(GameStateBetter gameState, String playerType) {
        this.gameState = gameState;
        this.isPlayerOne = playerType.equalsIgnoreCase("x");
    }


    public int[] getLastMove() {
        return this.lastMove;
    }

    abstract public int[] move();

    public GameStateBetter getGameState() {
        return gameState;
    }

    @Override
    public void run() {
        System.out.println("Peforming move");
        this.lastMove = this.move();
    }

    public void emergencyMove() {
        System.out.println("Bot failed to get move after a given time. Performing emergency move");
        // find first empty tile
        var state = this.getGameState();

        int i = 0;
        int j = 0;

        while (i < 8) {
            if (state.getGameBoardMatrix()[i][j] == 0) {
                try {
                    this.getGameState().play(i, j, isPlayerOne());
                    this.lastMove = new int[]{i, j};
                } catch (GameStateException.IllegalMove | GameStateException.RowColumnOverFlow ignored) {
                }
            }

            j++;

            if (j == 8) {
                j = 0;
                i++;
            }
        }
    }
}
