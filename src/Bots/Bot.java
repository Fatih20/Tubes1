package Bots;

import GameStateBetter.GameStateBetter;

abstract public class Bot implements Runnable {
    private GameStateBetter gameState;
    private final boolean isPlayerOne;
    private int[] lastMove;

    public Bot(String playerType) {
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

    public void setGameState(GameStateBetter gameState) {
        this.gameState = gameState;
    }

    @Override
    public void run() {
        System.out.println("Peforming move");
        this.lastMove = this.move();
    }

    public boolean isPlayerOne() {
        return isPlayerOne;
    }

    public void emergencyMove() {
        System.out.println("Bot failed to get move after a given time. Performing emergency move");
        // find first empty tile
        var state = this.getGameState();

        int i = 0;
        int j = 0;

        while (i < 8) {
            if (state.getGameBoardMatrix()[i][j] == 0) {
                this.lastMove = new int[]{i, j};
            }

            j++;

            if (j == 8) {
                j = 0;
                i++;
            }
        }
    }
}
