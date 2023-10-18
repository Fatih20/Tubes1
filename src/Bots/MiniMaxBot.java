package Bots;

import GameStateBetter.GameStateBetter;
import GameStateBetter.GameStateException;
import javafx.util.Pair;

public class MiniMaxBot extends Bot {
    private final int MAX_DEPTH = 6;

    public MiniMaxBot(GameStateBetter gameState, String playerType) {
        super(gameState, playerType);
    }


    protected int[] move() {
        int remainingDepth = Math.min(this.getGameState().getRemainingTurn(), this.MAX_DEPTH);

        return minimaxPlay(this.getGameState(), remainingDepth, this.isPlayerOne(), Integer.MIN_VALUE, Integer.MAX_VALUE);
    }

    /**
     * @param node           the state to start from
     * @param remainingDepth how many more recursion has to be checked
     * @param isPlayerOne    is this player x
     * @param alpha          alpha value of minimax algorithm
     * @param beta           beta value of minimax algorithm
     * @return Last executed move and the value that results from that move
     */
    private int[] minimaxPlay(GameStateBetter node, int remainingDepth, boolean isPlayerOne, int alpha, int beta) {
        if (remainingDepth == 0) {
            return new int[]{node.getLastPlay()[0], node.getLastPlay()[1], node.getScoreDifference()};
        }

        int[][] nextMoves = node.getEmptyBoxes();

        int bestValue = isPlayerOne ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int[] bestMove = new int[]{-1, -1};
        try {
            for (int[] nextMove : nextMoves) {
                GameStateBetter newState = (GameStateBetter) node.clone();
                newState.play(nextMove[0], nextMove[1], isPlayerOne, false);

                int[] minimaxReturn = minimaxPlay(newState, remainingDepth - 1, !isPlayerOne, alpha, beta);
                int value = minimaxReturn[2];

                if (isPlayerOne) {
                    if (value > bestValue) {
                        bestMove = newState.getLastPlay();
                    }
                    bestValue = Math.max(value, bestValue);
                    alpha = Math.max(alpha, bestValue);
                } else {
                    if (value < bestValue) {
                        bestMove = newState.getLastPlay();
                    }
                    bestValue = Math.min(value, bestValue);
                    beta = Math.min(beta, bestValue);
                }

                if (beta <= alpha) {
                    break;
                }
            }
        } catch (CloneNotSupportedException | GameStateException.IllegalMove | GameStateException.RowColumnOverFlow ignored) {

        }

        return new int[]{bestMove[0], bestMove[1], bestValue};
    }
}