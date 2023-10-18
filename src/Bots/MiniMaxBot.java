package Bots;

import GameStateBetter.GameStateBetter;
import javafx.util.Pair;

import java.util.ArrayList;

public class MiniMaxBot extends Bot {
    private final int MAX_DEPTH = 5;

    private int pruned = 0;

    public MiniMaxBot(GameStateBetter gameState, String playerType) {
        super(gameState, playerType);
    }


    protected int[] move() {
        int remainingDepth = Math.min(this.getGameState().getRemainingTurn(), this.MAX_DEPTH);

        Pair<Integer, Integer> nextMove = minimaxPlay(this.getGameState(), remainingDepth, this.isPlayerOne(), Integer.MIN_VALUE, Integer.MAX_VALUE).getKey();

        System.out.print("Pruning occurrence : ");
        System.out.println(this.pruned);
        this.pruned = 0;

        return new int[]{nextMove.getKey(), nextMove.getValue()};
    }

    /**
     * @param node           the state to start from
     * @param remainingDepth how many more recursion has to be checked
     * @param isPlayerOne    is this player x
     * @param alpha          alpha value of minimax algorithm
     * @param beta           beta value of minimax algorithm
     * @return Last executed move and the value that results from that move
     */
    private Pair<Pair<Integer, Integer>, Integer> minimaxPlay(GameStateBetter node, int remainingDepth, boolean isPlayerOne, int alpha, int beta) {
        if (remainingDepth == 0) {
            return new Pair<>(node.getLastPlay(), node.getScoreDifference());
        }

        ArrayList<GameStateBetter> nextStates = node.generateNextStates(isPlayerOne);

        int bestValue = isPlayerOne ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Pair<Integer, Integer> bestMove = new Pair<>(-1, -1);

        int i = nextStates.size();
        for (GameStateBetter nextState : nextStates) {
            Pair<Pair<Integer, Integer>, Integer> minimaxReturn = minimaxPlay(nextState, remainingDepth - 1, !isPlayerOne, alpha, beta);
            int value = minimaxReturn.getValue();

            if (isPlayerOne) {
                if (value > bestValue) {
                    bestMove = nextState.getLastPlay();
                }
                bestValue = Math.max(value, bestValue);
                alpha = Math.max(alpha, bestValue);
            } else {
                if (value < bestValue) {
                    bestMove = nextState.getLastPlay();
                }
                bestValue = Math.min(value, bestValue);
                beta = Math.min(beta, bestValue);
            }

            if (beta <= alpha) {
                this.pruned += i;
                break;
            }
            i--;
        }

        return new Pair<>(bestMove, bestValue);
    }
}