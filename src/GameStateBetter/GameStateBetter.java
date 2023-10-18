package GameStateBetter;

import javafx.scene.control.Button;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
interface scoreIncrementor {
    void execute();
}

public class GameStateBetter implements Cloneable {
    private final int emptyBox = 64;
    private int oScore = 0;
    private int xScore = 0;
    private boolean playerOneFirst;
    private int[][] gameBoardMatrix;
    private final Button[][] buttons;

    private int totalTurn = 56;
    private int currentTurn = 0;

    private Pair<Integer, Integer> lastPlay;

    public GameStateBetter(Button[][] buttonMatrix) {
        this.buttons = buttonMatrix;
        gameBoardMatrix = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String content = buttonMatrix[i][j].getText();
                if (content.equalsIgnoreCase("X")) {
                    gameBoardMatrix[i][j] = 1;
                } else if (content.equalsIgnoreCase("O")) {
                    gameBoardMatrix[i][j] = 2;
                } else {
                    gameBoardMatrix[i][j] = 0;
                }
            }
        }
    }

    public void addTurn () {
        this.currentTurn++;
    }

    public void setTotalTurn(int totalTurn) {
        this.totalTurn = totalTurn;
    }

    public boolean isPlayerOneFirst() {
        return playerOneFirst;
    }

    public void setPlayerOneFirst(boolean playerOneFirst) {
        this.playerOneFirst = playerOneFirst;
    }

    public int getRemainingRound() {
        return (totalTurn - currentTurn + 2 - 1) / 2;
    }

    public boolean isPlayerOneTurn() {
        return (playerOneFirst && (currentTurn % 2 == 0)) || (!playerOneFirst && !(currentTurn % 2 == 0));
    }

    public Pair<Integer, Integer> getLastPlay() {
        return lastPlay;
    }

    public int getEmptyBox() {
        return emptyBox;
    }

    public int getRemainingTurn() {
        return totalTurn - currentTurn;
    }

    public int getoScore() {
        return oScore;
    }

    public int getxScore() {
        return xScore;
    }

    public void incrementxScore() {
        xScore++;
    }

    public void decrementxScore() {
        xScore--;
    }

    public void incrementoScore() {
        oScore++;
    }

    public void decrementoScore() {
        oScore--;
    }

    public int[][] getGameBoardMatrix() {
        return gameBoardMatrix;
    }

    private void validateMove(int row, int column) throws GameStateException.RowColumnOverFlow, GameStateException.IllegalMove {
        if (row >= 8 || row < 0) {
            throw new GameStateException.RowColumnOverFlow();
        }

        if (column >= 8 || column < 0) {
            throw new GameStateException.RowColumnOverFlow();
        }

        if (gameBoardMatrix[row][column] != 0) {
            throw new GameStateException.IllegalMove();
        }
    }

    /**
     * Generate all possible states that could result from this state
     * @param isPlayerOne is the turn made as player one?
     * @return all of the nextStates
     */
    public ArrayList<GameStateBetter> generateNextStates(boolean isPlayerOne) {
        ArrayList<GameStateBetter> nextStates = new ArrayList<>();

        int playerValue = isPlayerOne ? 1 : 2;
        int opponentValue = !(isPlayerOne) ? 1 : 2;

        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (this.gameBoardMatrix[i][j] == 0) {
                    try {
                        GameStateBetter nextState = (GameStateBetter) this.clone();
                        nextState.play(i, j, isPlayerOne, false);
                        nextStates.add(nextState);
                    } catch (CloneNotSupportedException | GameStateException.RowColumnOverFlow |
                             GameStateException.IllegalMove ignored) {
                    }

                }
            }
        }
        return nextStates;
    }

    public Object clone () throws CloneNotSupportedException {
        GameStateBetter cloneState = (GameStateBetter) super.clone();

        int[][] cloneGameBoardMatrix = new int[8][8];

        for (int i = 0; i < 8; i++) {
            System.arraycopy(this.gameBoardMatrix[i], 0, cloneGameBoardMatrix[i], 0, 8);
        }

        cloneState.gameBoardMatrix = cloneGameBoardMatrix;

        return cloneState;
    }

    public void play(int row, int column, boolean isPlayerOne, boolean updateButtons) throws GameStateException.RowColumnOverFlow, GameStateException.IllegalMove {
        validateMove(row, column);

        int playerValue = isPlayerOne ? 1 : 2;
        int opponentValue = !(isPlayerOne) ? 1 : 2;

        String playerText = isPlayerOne ? "X" : "O";
        String opponentText = !isPlayerOne ? "X" : "O";

        scoreIncrementor incrementPlayerScore = isPlayerOne ? this::incrementxScore : this::incrementoScore;
        scoreIncrementor decrementOpponentScore = !(isPlayerOne) ? this::decrementxScore : this::decrementoScore;

        if (gameBoardMatrix[row][column] != 0) {
            throw new GameStateException.IllegalMove();
        }

        gameBoardMatrix[row][column] = playerValue;

        if (updateButtons) {
            buttons[row][column].setText(playerText);
        }

        incrementPlayerScore.execute();

        int[][] cellNeighbors = {
                {row - 1, column},
                {row + 1, column},
                {row, column - 1},
                {row, column + 1}
        };

        ArrayList<int[]> validNeighbors = new ArrayList<>();

        for (int[] cellNeighbor : cellNeighbors) {
            int rowNeighbor = cellNeighbor[0];
            int columnNeighbor = cellNeighbor[1];
            if (rowNeighbor >= 0 && rowNeighbor < 8 && columnNeighbor >= 0 && columnNeighbor < 8) {
                validNeighbors.add(new int[]{cellNeighbor[0], cellNeighbor[1]});
            }
        }

        for (int[] validNeighbor : validNeighbors) {
            int rowNeighbor = validNeighbor[0];
            int columnNeighbor = validNeighbor[1];
            int neighborCell = gameBoardMatrix[rowNeighbor][columnNeighbor];

            if (neighborCell == opponentValue) {
                gameBoardMatrix[rowNeighbor][columnNeighbor] = playerValue;
                if (updateButtons) {
                buttons[rowNeighbor][columnNeighbor].setText(playerText);
                }
                incrementPlayerScore.execute();
                decrementOpponentScore.execute();
            }
        }

        this.lastPlay = new Pair<>(row, column);
    }

    public void play(int row, int column, boolean isPlayerOne) throws GameStateException.RowColumnOverFlow, GameStateException.IllegalMove {
        this.play(row, column, isPlayerOne, true);
    }

    /**
     *
     * @return the difference in score between player one and player two
     */
    public int getScoreDifference() {
        return getxScore() - getoScore();
    }


    /*
     * Check the neighboring cells of the given cell and add them to the heuristic set if they are empty
     * */
    private void addHeuristic(List<Pair<Integer, Integer>> heuristic, int i, int j) {
        if (i - 1 >= 0) { // up
            if (gameBoardMatrix[i - 1][j] == 0) {
                heuristic.add(new Pair<>(i - 1, j));
            }
        }
        if (i + 1 < gameBoardMatrix.length) { // down
            if (gameBoardMatrix[i + 1][j] == 0) {
                heuristic.add(new Pair<>(i + 1, j));
            }
        }
        if (j - 1 >= 0) { // left
            if (gameBoardMatrix[i][j - 1] == 0) {
                heuristic.add(new Pair<>(i, j - 1));
            }
        }
        if (j + 1 < gameBoardMatrix[i].length) { // right
            if (gameBoardMatrix[i][j + 1] == 0) {
                heuristic.add(new Pair<>(i, j + 1));
            }
        }
    }

    /*
     * Check if current move will create a hole in the board
     * */
    private boolean holeCreated(Pair<Integer, Integer> possibleMove, List<Pair<Integer, Integer>> playerPieces) {
        // check if (i-1, j-1) (i-1, j+1) (i+1, j-1) (i+1, j+1) are all occupied by the player
        int i = possibleMove.getKey();
        int j = possibleMove.getValue();

        // conditioning if possibleMove is at the edge of the board
        if (i == 0) { // top edge
            if (j == 0) {
                return playerPieces.contains(new Pair<>(i + 1, j + 1));
            } else if (j == gameBoardMatrix[0].length) {
                return playerPieces.contains(new Pair<>(i + 1, j - 1));
            } else {
                return playerPieces.contains(new Pair<>(i + 1, j - 1)) || playerPieces.contains(new Pair<>(i + 1, j + 1));
            }
        } else if (i == gameBoardMatrix.length) { // bottom edge
            if (j == 0) {
                return playerPieces.contains(new Pair<>(i - 1, j + 1));
            } else if (j == gameBoardMatrix[0].length) {
                return playerPieces.contains(new Pair<>(i - 1, j - 1));
            } else {
                return playerPieces.contains(new Pair<>(i - 1, j - 1)) || playerPieces.contains(new Pair<>(i - 1, j + 1));
            }
        } else if (j == 0) { // left edge
            return playerPieces.contains(new Pair<>(i - 1, j + 1)) || playerPieces.contains(new Pair<>(i + 1, j + 1));
        } else if (j == gameBoardMatrix[0].length) { // right edge
            return playerPieces.contains(new Pair<>(i - 1, j - 1)) || playerPieces.contains(new Pair<>(i + 1, j - 1));
        } else { // not at the edge of the board
            return playerPieces.contains(new Pair<>(i - 1, j - 1)) || playerPieces.contains(new Pair<>(i - 1, j + 1)) ||
                    playerPieces.contains(new Pair<>(i + 1, j - 1)) || playerPieces.contains(new Pair<>(i + 1, j + 1));
        }
    }

    /*
     * Returns a set of all possible moves for the current player in which
     * the move is next to the opponent's piece
     * */
    public List<Pair<Integer, Integer>> heuristic(boolean isPlayerOne) {
        List<Pair<Integer, Integer>> heuristic = new ArrayList<>();

        // X is 1, 0 is 2
        int enemy = isPlayerOne ? 2 : 1;

        for (int i = 0; i < gameBoardMatrix.length; i++) {
            for (int j = 0; j < gameBoardMatrix[i].length; j++) {
                if (gameBoardMatrix[i][j] == enemy) {
                    addHeuristic(heuristic, i, j);
                }
            }
        }
        return heuristic;
    }

    /**
     * @param isPlayerOne whether the player is X or not
     * @return a list of all possible moves for the current player in which
     * they fit the heuristic, i.e. the move is next to the opponent's piece,
     * and it avoids any moves that cause the appearance of hole in the board.
     * If the there is no suitable cell that fits the heuristic,
     * return a list of all possible moves.
     */
    public List<Pair<Integer, Integer>> heuristicFiltered(boolean isPlayerOne) {
        List<Pair<Integer, Integer>> playerPieces = new ArrayList<>();
        List<Pair<Integer, Integer>> heuristic = new ArrayList<>();

        // X is 1, 0 is 2
        int enemy = isPlayerOne ? 2 : 1;
        int player = isPlayerOne ? 1 : 2;

        for (int i = 0; i < gameBoardMatrix.length; i++) {
            for (int j = 0; j < gameBoardMatrix[i].length; j++) {
                if (gameBoardMatrix[i][j] == enemy) {
                    // validate left right up down (nested to prevent out of bounds)
                    addHeuristic(heuristic, i, j);
                } else if (gameBoardMatrix[i][j] == player) {
                    playerPieces.add(new Pair<>(i, j));
                }
            }
        }

        // filter the heuristic set to remove any moves that cause a hole
        List<Pair<Integer, Integer>> heuristicFiltered = new ArrayList<>();
        for (Pair<Integer, Integer> possibleMove : heuristic) {
            if (!holeCreated(possibleMove, playerPieces)) {
                heuristicFiltered.add(possibleMove);
            }
        }

        return heuristicFiltered.isEmpty() ? heuristic : heuristicFiltered;
    }
}
