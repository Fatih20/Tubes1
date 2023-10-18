package Bots;

import GameStateBetter.GameStateBetter;
import GameStateBetter.GameStateException;
import javafx.util.Pair;
import java.util.List;
import java.util.Random;

public class Chromosome {
    private List<Pair<Integer,Integer>> genes;
    private int fitness;
    private final boolean isX;
    private final GameStateBetter gameState;

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
            for (Pair<Integer, Integer> move : this.genes) {
                gameStateCopy.play(move.getKey(), move.getValue(), isX,false);
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
            int x = random.nextInt(8);
            int y = random.nextInt(8);
            Pair<Integer, Integer> newMove = new Pair<>(x, y);
            while (bannedMoves.contains(newMove) || this.genes.contains(newMove)){
                x = random.nextInt(8);
                y = random.nextInt(8);
                newMove = new Pair<>(x, y);
            }
            this.genes.set(randomMutationPoint, newMove);
            this.setFitness();
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
