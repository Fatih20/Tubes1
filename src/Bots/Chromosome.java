package Bots;

import GameStateBetter.GameStateBetter;
import GameStateBetter.GameStateException;
import javafx.util.Pair;
import java.util.List;
import java.util.Random;

public class Chromosome {
    private double mutationTreshold = 0.15;
    private List<Pair<Integer,Integer>> genes;
    private int fitness;
    private boolean isX;

    public Chromosome(List<Pair<Integer,Integer>> genes, boolean isX){
        this.genes = genes;
        this.isX = isX;
        this.fitness = 0;
    }

    public List<Pair<Integer,Integer>> getGenes(){
        return this.genes;
    }
 
    public int getFitness(){
        return this.fitness;
    }

    public void setFitness(GameStateBetter gameState) {
        /*
        * 1. Make a copy of the game state
        * 2. Make moves according to the genes
        * 3. Calculate the fitness
        * */
        try{
            GameStateBetter gameStateCopy = (GameStateBetter) gameState.clone();
            for (Pair<Integer, Integer> move : this.genes) {
                gameStateCopy.play(move.getKey(), move.getValue(), isX,true);
            }
            int XScore = gameStateCopy.getxScore();
            int OScore = gameStateCopy.getoScore();
            if (this.isX){
                this.fitness = XScore - OScore;
            } else {
                this.fitness = OScore - XScore;
            }
        } catch (CloneNotSupportedException e){
            e.printStackTrace();
        } catch (GameStateException.IllegalMove | GameStateException.RowColumnOverFlow e) {
            throw new RuntimeException(e);
        }
    }

    public void mutate(){
        // TODO: implement mutation (too much checking for now)
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
