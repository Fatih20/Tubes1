package Bots;

import GameStateBetter.GameStateBetter;
import javafx.util.Pair;

import java.util.ArrayList;

public class MiniMaxBot extends Bot {
    private final int MAX_DEPTH = 5;

    public MiniMaxBot(String playerType) {
        super(playerType);
    }


    public int[] move() {
        int remainingDepth = Math.min(this.getGameState().getRemainingTurn(), this.MAX_DEPTH);

        Pair<Integer, Integer> nextMove = minimaxPlay(this.getGameState(), remainingDepth, this.isPlayerOne(), Integer.MIN_VALUE, Integer.MAX_VALUE).getKey();

        System.out.print("i : ");
        System.out.println(nextMove.getKey());

        System.out.print("j : ");
        System.out.println(nextMove.getValue());

        return new int[]{nextMove.getKey(), nextMove.getValue()};
    }

    private Pair<Pair<Integer, Integer>, Integer> minimaxPlay(GameStateBetter node, int remainingDepth, boolean isPlayerOne, int alpha, int beta) {
        if (remainingDepth == 0) {
            return new Pair<>(node.getLastPlay(), node.getScoreDifference());
        }

        ArrayList<GameStateBetter> nextStates = node.generateNextStates(isPlayerOne);

        int bestValue = isPlayerOne ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        Pair<Integer, Integer> bestMove = new Pair<>(-1, -1);

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
                break;
            }
        }

        return new Pair<>(bestMove, bestValue);
    }
}