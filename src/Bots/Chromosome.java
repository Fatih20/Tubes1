package Bots;

import javafx.util.Pair;
import java.util.List;
import java.util.Random;

public class Chromosome {
    private static double mutationTreshold = 0.15;

    private List<Pair<Integer,Integer>> genes;
    private int fitness;

    public Chromosome(List<Pair<Integer,Integer>> genes, int fitness){
        this.genes = genes;
        this.fitness = fitness;
    }

    public List<Pair<Integer,Integer>> getGenes(){
        return this.genes;
    }

    public int getFitness(){
        return this.fitness;
    }

    public void setFitness(int fitness){
        this.fitness = fitness;
    }

    public void mutate(){
        // TODO: implement mutation (too much checking for now)
    }
}
