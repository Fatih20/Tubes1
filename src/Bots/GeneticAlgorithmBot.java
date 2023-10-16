package Bots;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GeneticAlgorithmBot extends Bot {
    private int populationSize = 100;
    private int maxGenerations = 100;
    private List<Chromosome> population;

    public GeneticAlgorithmBot(String playerType) {
        super(playerType);
    }

    private List<Chromosome> initializePopulation(int rounds) {
        List<Chromosome> population = new ArrayList<>();
        // TODO: implement population initialization
        /*
        * General idea is as follows:
        * 1. Determine the length of the chromosome (number of genes) = 2 * rounds
        * 2. Generate a random chromosome of the determined length by using heuristics
        * 3. Repeat 2. until populationSize chromosomes are generated
        * */

        return population;
    }

    private List<Chromosome> selectParents(){
        List<Chromosome> parents = new ArrayList<>();

        // make the roulette wheel
        Map<Double, Integer> rouletteWheel= new HashMap<>();
        double totalFitness = population.stream().mapToDouble(Chromosome::getFitness).sum();
        double curr = 0.0D;
        for (int i = 0; i < population.size(); i++) {
            curr += population.get(i).getFitness() / totalFitness;
            rouletteWheel.put(curr, i);
        }

        // spin the wheel
        Random random = new Random();
        for (int i = 0; i < populationSize; i++){
            double spin = random.nextDouble();
            double current = 0;
            for (double fitness : rouletteWheel.keySet()){
                current += fitness;
                if (spin <= current){
                    parents.add(population.get(rouletteWheel.get(fitness)));
                    break;
                }
            }
        }

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
