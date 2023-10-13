package GameStateBetter;

import java.util.ArrayList;

@FunctionalInterface
interface scoreIncrementor {
    void execute();
}

public class GameStateBetter {
    private int emptyBox = 64;
    private int oScore = 0;
    private int xScore = 0;

    private int[][] gameBoardMatrix;

    public GameStateBetter(String[][] gameStateShittier) {
        gameBoardMatrix = new int[8][8];
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                String content = gameStateShittier[i][j];
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

    public int getEmptyBox() {
        return emptyBox;
    }

    public int getoScore() {
        return oScore;
    }

    public int getxScore() {
        return xScore;
    }

    public int[][] getGameBoardMatrix() {
        return gameBoardMatrix;
    }

    private void validateMove(int row, int column) throws GameStateException.RowColumnOverFlow {
        if (row >= 8 || row < 0) {
            throw new GameStateException.RowColumnOverFlow();
        }

        if (column >= 8 || column < 0) {
            throw new GameStateException.RowColumnOverFlow();
        }
    }

    public void play(int row, int column, boolean isPlayerOne) throws GameStateException.RowColumnOverFlow, GameStateException.IllegalMove {
        validateMove(row, column);

        int playerValue = isPlayerOne ? 1 : 2;
        int opponentValue = !(isPlayerOne) ? 1 : 2;

        scoreIncrementor incrementPlayerScore = isPlayerOne ? () -> {
            xScore++;
        } : () -> {
            oScore++;
        };
        scoreIncrementor decrementOpponentScore = !(isPlayerOne) ? () -> {
            xScore--;
        } : () -> {
            oScore--;
        };

        if (gameBoardMatrix[row][column] != 0) {
            throw new GameStateException.IllegalMove();
        }

        gameBoardMatrix[row][column] = playerValue;
        if (isPlayerOne) {
            xScore++;
        } else {
            oScore++;
        }

        int[][] cellNeighbors = {{row - 1, column}, {row + 1, column}, {row, column - 1}, {row, column + 1}};
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
            int neighborCell = gameBoardMatrix[validNeighbor[0]][validNeighbor[1]];
            if (neighborCell == opponentValue) {
                gameBoardMatrix[rowNeighbor][columnNeighbor] = playerValue;
                incrementPlayerScore.execute();
                decrementOpponentScore.execute();
            } else if (gameBoardMatrix[validNeighbor[0]][validNeighbor[1]] == 0) {
                incrementPlayerScore.execute();
                gameBoardMatrix[rowNeighbor][columnNeighbor] = playerValue;
                emptyBox--;
            }
        }


    }
}


