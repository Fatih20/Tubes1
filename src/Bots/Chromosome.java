package Bots;

import GameStateBetter.GameStateBetter;
import GameStateBetter.GameStateException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Chromosome {
    private List<int[]> genes;
    private int fitness;
    private final boolean isX;
    private final GameStateBetter gameState;
    private static final List<int[]> allMoves = List.<int[]>of(
            new int[]{0, 0},
            new int[]{0, 1},
            new int[]{0, 2},
            new int[]{0, 3},
            new int[]{0, 4},
            new int[]{0, 5},
            new int[]{0, 6},
            new int[]{0, 7},
            new int[]{1, 0},
            new int[]{1, 1},
            new int[]{1, 2},
            new int[]{1, 3},
            new int[]{1, 4},
            new int[]{1, 5},
            new int[]{1, 6},
            new int[]{1, 7},
            new int[]{2, 0},
            new int[]{2, 1},
            new int[]{2, 2},
            new int[]{2, 3},
            new int[]{2, 4},
            new int[]{2, 5},
            new int[]{2, 6},
            new int[]{2, 7},
            new int[]{3, 0},
            new int[]{3, 1},
            new int[]{3, 2},
            new int[]{3, 3},
            new int[]{3, 4},
            new int[]{3, 5},
            new int[]{3, 6},
            new int[]{3, 7},
            new int[]{4, 0},
            new int[]{4, 1},
            new int[]{4, 2},
            new int[]{4, 3},
            new int[]{4, 4},
            new int[]{4, 5},
            new int[]{4, 6},
            new int[]{4, 7},
            new int[]{5, 0},
            new int[]{5, 1},
            new int[]{5, 2},
            new int[]{5, 3},
            new int[]{5, 4},
            new int[]{5, 5},
            new int[]{5, 6},
            new int[]{5, 7},
            new int[]{6, 0},
            new int[]{6, 1},
            new int[]{6, 2},
            new int[]{6, 3},
            new int[]{6, 4},
            new int[]{6, 5},
            new int[]{6, 6},
            new int[]{6, 7},
            new int[]{7, 0},
            new int[]{7, 1},
            new int[]{7, 2},
            new int[]{7, 3},
            new int[]{7, 4},
            new int[]{7, 5},
            new int[]{7, 6},
            new int[]{7, 7}
    );

    public Chromosome(List<int[]> genes, boolean isX, GameStateBetter gameState) {
        this.genes = genes;
        this.isX = isX;
        this.gameState = gameState;
        this.fitness = 0;
    }

    public List<int[]> getGenes() {
        return this.genes;
    }

    public int getFitness() {
        return this.fitness;
    }

    public void setFitness() {
        /*
         * 1. Make a copy of the game state
         * 2. Make moves according to the genes
         * 3. Calculate the fitness
         * */
        try {
            GameStateBetter gameStateCopy = (GameStateBetter) this.gameState.clone();
            boolean turn = this.isX;
            System.out.println("Genes: " + this.genes);
            for (int[] move : this.genes) {
                System.out.println("Move: i :" + move[0] + " j :" + move[1]);
                gameStateCopy.play(move[0], move[1], turn, false);
                turn = !turn;
                System.out.println("Move played");
            }

            int XScore = gameStateCopy.getxScore();
            int OScore = gameStateCopy.getoScore();
            if (this.isX) {
                this.fitness = XScore - OScore;
            } else {
                this.fitness = OScore - XScore;
            }
        } catch (CloneNotSupportedException | GameStateException.IllegalMove | GameStateException.RowColumnOverFlow e) {
            throw new RuntimeException(e);
        }
    }

    public void mutate(List<int[]> bannedMoves) {
        /*
         * Idea is usage of randomMutationPoint
         * 1. Generate a random number between 0 and 1
         * 2. If the number is less than mutationTreshold, then mutation happens
         * 3. Generate a random mutation point
         * 4. Generate a random move at the mutation point
         * 5. Check if the move is not in the bannedMoves list and unique in the chromosome
         * 6. If it is, then repeat 4-5
         * */
        Random random = new Random();
        double mutationChance = random.nextDouble();
        double mutationTreshold = 0.15;
        if (mutationChance < mutationTreshold) { // mutation happens
            int randomMutationPoint = random.nextInt(this.genes.size());
            int[] move = this.genes.get(randomMutationPoint);
            List<int[]> allowedMoves = new ArrayList<>(allMoves);
            allowedMoves.removeAll(bannedMoves);
            allowedMoves.removeAll(this.genes);
            int[] newMove = allowedMoves.get(random.nextInt(allowedMoves.size()));
            this.genes.set(randomMutationPoint, newMove);
        }
    }

    /*
     * Used to compare two chromosomes by their genes
     * */
    public boolean equals(Chromosome chromosome) {
        if (this.genes.size() != chromosome.getGenes().size()) {
            return false;
        }
        for (int i = 0; i < this.genes.size(); i++) {
            if (!Arrays.equals(this.genes.get(i), chromosome.getGenes().get(i))) {
                return false;
            }
        }
        return true;
    }

    public static Chromosome make(int turns, boolean isX, GameStateBetter gameState, List<int[]> bannedMoves) {
        Chromosome chromosome = new Chromosome(new ArrayList<>(), isX, gameState);
        Random random = new Random();

        for (int i = 0; i < turns; i++) {
            List<int[]> allowedMoves = new ArrayList<>(allMoves);
            allowedMoves.removeAll(bannedMoves);
            allowedMoves.removeAll(chromosome.getGenes());
            int[] move = allowedMoves.get(random.nextInt(allowedMoves.size()));
            while (chromosome.getGenes().contains(move)) {
                move = allowedMoves.get(random.nextInt(allowedMoves.size()));
            }
            chromosome.getGenes().add(move);
        }
        System.out.println("Chromosome made, checking fitness");
        chromosome.setFitness();
        System.out.println("Fitness checked: " + chromosome.getFitness());
        return chromosome;
    }
}
