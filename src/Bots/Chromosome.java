package Bots;

import GameStateBetter.GameStateBetter;
import GameStateBetter.GameStateException;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Chromosome {
    private List<Pair<Integer,Integer>> genes;
    private int fitness;
    private final boolean isX;
    private final GameStateBetter gameState;
    private static final List<Pair<Integer,Integer>> allMoves = List.<Pair<Integer, Integer>>of(
            new Pair<>(0, 0),
            new Pair<>(0, 1),
            new Pair<>(0, 2),
            new Pair<>(0, 3),
            new Pair<>(0, 4),
            new Pair<>(0, 5),
            new Pair<>(0, 6),
            new Pair<>(0, 7),
            new Pair<>(1, 0),
            new Pair<>(1, 1),
            new Pair<>(1, 2),
            new Pair<>(1, 3),
            new Pair<>(1, 4),
            new Pair<>(1, 5),
            new Pair<>(1, 6),
            new Pair<>(1, 7),
            new Pair<>(2, 0),
            new Pair<>(2, 1),
            new Pair<>(2, 2),
            new Pair<>(2, 3),
            new Pair<>(2, 4),
            new Pair<>(2, 5),
            new Pair<>(2, 6),
            new Pair<>(2, 7),
            new Pair<>(3, 0),
            new Pair<>(3, 1),
            new Pair<>(3, 2),
            new Pair<>(3, 3),
            new Pair<>(3, 4),
            new Pair<>(3, 5),
            new Pair<>(3, 6),
            new Pair<>(3, 7),
            new Pair<>(4, 0),
            new Pair<>(4, 1),
            new Pair<>(4, 2),
            new Pair<>(4, 3),
            new Pair<>(4, 4),
            new Pair<>(4, 5),
            new Pair<>(4, 6),
            new Pair<>(4, 7),
            new Pair<>(5, 0),
            new Pair<>(5, 1),
            new Pair<>(5, 2),
            new Pair<>(5, 3),
            new Pair<>(5, 4),
            new Pair<>(5, 5),
            new Pair<>(5, 6),
            new Pair<>(5, 7),
            new Pair<>(6, 0),
            new Pair<>(6, 1),
            new Pair<>(6, 2),
            new Pair<>(6, 3),
            new Pair<>(6, 4),
            new Pair<>(6, 5),
            new Pair<>(6, 6),
            new Pair<>(6, 7),
            new Pair<>(7, 0),
            new Pair<>(7, 1),
            new Pair<>(7, 2),
            new Pair<>(7, 3),
            new Pair<>(7, 4),
            new Pair<>(7, 5),
            new Pair<>(7, 6),
            new Pair<>(7, 7)
    );

    public Chromosome(List<Pair<Integer,Integer>> genes, boolean isX, GameStateBetter gameState){
        this.genes = genes;
        this.isX = isX;
        this.gameState = gameState;
        this.fitness = 0;
    }

    public List<Pair<Integer,Integer>> getGenes(){
        return this.genes;
    }
 
    public int getFitness(){
        return this.fitness;
    }

    public void setFitness() {
        /*
        * 1. Make a copy of the game state
        * 2. Make moves according to the genes
        * 3. Calculate the fitness
        * */
        try{
            GameStateBetter gameStateCopy = (GameStateBetter) this.gameState.clone();
            boolean turn = this.isX;
            for (Pair<Integer, Integer> move : this.genes) {
                gameStateCopy.play(move.getKey(), move.getValue(), turn,false);
                turn = !turn;
            }
            int XScore = gameStateCopy.getxScore();
            int OScore = gameStateCopy.getoScore();
            if (this.isX){
                this.fitness = XScore - OScore;
            } else {
                this.fitness = OScore - XScore;
            }
        } catch (CloneNotSupportedException | GameStateException.IllegalMove | GameStateException.RowColumnOverFlow e){
            throw new RuntimeException(e);
        }
    }

    public void mutate(List<Pair<Integer,Integer>> bannedMoves){
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
        if (mutationChance < mutationTreshold){ // mutation happens
            int randomMutationPoint = random.nextInt(this.genes.size());
            Pair<Integer, Integer> move = this.genes.get(randomMutationPoint);
            List<Pair<Integer, Integer>> allowedMoves = new ArrayList<>(allMoves);
            allowedMoves.removeAll(bannedMoves);
            allowedMoves.removeAll(this.genes);
            Pair<Integer, Integer> newMove = allowedMoves.get(random.nextInt(allowedMoves.size()));
            this.genes.set(randomMutationPoint, newMove);
        }
    }

    /*
    * Used to compare two chromosomes by their genes
    * */
    public boolean equals(Chromosome chromosome){
        if (this.genes.size() != chromosome.getGenes().size()){
            return false;
        }
        for (int i = 0; i < this.genes.size(); i++){
            if (!this.genes.get(i).equals(chromosome.getGenes().get(i))){
                return false;
            }
        }
        return true;
    }
}
