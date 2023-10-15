package Bots;

import java.util.ArrayList;
import java.util.List;

public class GeneticAlgorithmBot extends Bot {
    private int populationSize = 100;
    private int maxGenerations = 100;

    public GeneticAlgorithmBot(String playerType) {
        super(playerType);
    }

    private List<Chromosome> initializePopulation(int populationSize) {
        List<Chromosome> population = new ArrayList<>();
        // TODO: implement population initialization
        return population;
    }

    private List<Chromosome> selectParents(){
        List<Chromosome> parents = new ArrayList<>();
        // TODO: implement parent selection using roulette wheel selection
        return parents;
    }

    private List<Chromosome> crossover(List<Chromosome> parents) {
        List<Chromosome> children = new ArrayList<>();
        // TODO: implement crossover
        return children;
    }

    private void mutate(List<Chromosome> children) {
        // TODO: call mutate() on each child
    }

    private void evaluateFitness(List<Chromosome> population) {
        // TODO: evaluate fitness for each chromosome in population
    }

    public int[] move() {
        // find first empty tile
        var state = this.getGameState();


        int i = 0;
        int j = 0;

        while (i < 8) {
            if (state.getGameBoardMatrix()[i][j] == 0) {
                return new int[]{i, j};

            }

            j++;

            if (j == 8) {
                j = 0;
                i++;
            }
        }

        return new int[]{0, 0};
    }
}
