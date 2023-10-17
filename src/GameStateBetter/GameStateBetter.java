package GameStateBetter;

import javafx.scene.control.Button;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;

@FunctionalInterface
interface scoreIncrementor {
    void execute();
}

public class GameStateBetter {
    private int emptyBox = 64;
    private int oScore = 0;
    private int xScore = 0;
    private boolean playerOneTurn;
    private int[][] gameBoardMatrix;
    private Button[][] buttons;

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

        this.playerOneTurn = true;
    }

    public boolean isPlayerOneTurn() {
        return playerOneTurn;
    }

    public void alternateTurn() {
        playerOneTurn = !playerOneTurn;
    }

    public void setPlayerOneTurn(boolean playerOneTurn) {
        this.playerOneTurn = playerOneTurn;
    }

    public int getEmptyBox() {
        return emptyBox;
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

    public void play(int row, int column, boolean isPlayerOne) throws GameStateException.RowColumnOverFlow, GameStateException.IllegalMove {
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
        buttons[row][column].setText(playerText);
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
                neighborCell = playerValue;
                buttons[rowNeighbor][columnNeighbor].setText(playerText);
                incrementPlayerScore.execute();
                decrementOpponentScore.execute();

            } /* else if (neighborCell == 0) {
                incrementPlayerScore.execute();
                gameBoardMatrix[rowNeighbor][columnNeighbor] = playerValue;
                buttons[rowNeighbor][columnNeighbor].setText(playerText);
                emptyBox--;
            } */
        }
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
     * @param isPlayerOne is the
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
