package Bots;

import GameStateBetter.GameStateBetter;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

public class HillClimbingBot extends Bot {

    public HillClimbingBot(String playerType) {
        super(playerType);
    }

    public int[] move() {
        GameStateBetter state = this.getGameState();

        List<Pair<Integer, Integer>> candidateCells = state.heuristicFiltered(isPlayerOne());

        Pair<Integer, Integer> maxValueCell = candidateCells.get(0);
        int maxValue = value(state, candidateCells.get(0).getKey(), candidateCells.get(0).getValue());

        for (int i = 1; i < candidateCells.size(); i++) {
            Pair<Integer, Integer> candidateCell = candidateCells.get(i);
            int value = value(state, candidateCell.getKey(), candidateCell.getValue());

            if (value > maxValue) {
                maxValue = value;
                maxValueCell = candidateCell;
            }
        }

        return new int[]{
                maxValueCell.getKey(),
                maxValueCell.getValue()
        };
    }

    /**
     * The objective function. Evaluates the value of the current game state.
     *
     * @param g The current game state
     * @param i The outer index of the candidate move
     * @param j The inner index of the candidate move
     * @return The value of the current game state
     */
    private int value(GameStateBetter g, int i, int j) {
        int[][] matrix = g.getGameBoardMatrix();

        int[][] candidateNeighbors = {
                {i - 1, j},
                {i + 1, j},
                {i, j - 1},
                {i, j + 1}
        };

        ArrayList<int[]> validNeighbors = new ArrayList<>();

        // Populate valid neighbors with candidate neighbors that have valid indices
        for (int[] candidateNeighbor : candidateNeighbors) {
            int candidateNeighborRow = candidateNeighbor[0];
            int candidateNeighborColumn = candidateNeighbor[1];

            if (candidateNeighborRow >= 0 && candidateNeighborRow < 8 &&
                    candidateNeighborColumn >= 0 && candidateNeighborColumn < 8) {
                validNeighbors.add(new int[]{candidateNeighbor[0], candidateNeighbor[1]});
            }
        }

        int opponentValue = !(isPlayerOne()) ? 1 : 2;

        int playerScore = isPlayerOne() ? g.getxScore() : g.getoScore();
        int opponentScore = !(isPlayerOne()) ? g.getxScore() : g.getoScore();

        for (int[] validNeighbor : validNeighbors) {

            int neighborValue = matrix[validNeighbor[0]][validNeighbor[1]];

            if (neighborValue == opponentValue) {
                playerScore++;
                opponentScore--;
            } /* else if (neighborValue == 0) {
                 playerScore++;
            } */
        }

        return playerScore - opponentScore;
    }
}
