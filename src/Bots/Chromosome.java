package Bots;

import javafx.util.Pair;
import java.util.List;
import java.util.Random;

public class Chromosome {
    private double mutationTreshold = 0.15;
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
