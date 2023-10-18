package Bots;

import GameStateBetter.GameStateBetter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

abstract public class Bot implements Runnable {
    private final GameStateBetter gameState;
    private final boolean isPlayerOne;
    private int[] lastMove;

    public Bot(GameStateBetter gameState, String playerType) {
        this.gameState = gameState;
        this.isPlayerOne = playerType.equalsIgnoreCase("x");
    }

    public int[] getLastMove() {
        var lastMove = this.lastMove;
        this.lastMove = null;

        return lastMove;
    }

    abstract protected int[] move();

    public GameStateBetter getGameState() {
        return gameState;
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

        List<int[]> candidate = new ArrayList<>();

        int i = 0;
        int j = 0;

        while (i < 8) {
            if (state.getGameBoardMatrix()[i][j] == 0) {
                candidate.add(new int[]{i, j});
            }

            j++;

            if (j == 8) {
                j = 0;
                i++;
            }
        }

        // select random
        int rnd = new Random().nextInt(candidate.size());

        this.lastMove = candidate.get(rnd);
    }
}
