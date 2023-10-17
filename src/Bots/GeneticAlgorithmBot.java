package Bots;

import GameStateBetter.GameStateBetter;
import javafx.util.Pair;

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
    public GeneticAlgorithmBot(GameStateBetter gameState, String playerType) {
        super(gameState, playerType);
    }

    private Chromosome makeChromosome(int rounds){
        Chromosome chromosome = new Chromosome(new ArrayList<>(), 0);
        Random random = new Random();
        final List<Pair<Integer, Integer>> bannedMoves = List.of(
                new Pair<>(0,6),
                new Pair<>(1,6),
                new Pair<>(0,7),
                new Pair<>(1,7),
                new Pair<>(6,0),
                new Pair<>(6,1),
                new Pair<>(7,0),
                new Pair<>(7,1)
        );
        /*
        * Banned coordinates = (0,6), (1,6), (0,7), (1,7), (6,0), (6,1), (7,0), (7,1)
        * */
        for (int i = 0; i < 2*rounds; i++){
            int x = random.nextInt(8);
            int y = random.nextInt(8);
            Pair<Integer, Integer> move = new Pair<>(x, y);
            while (bannedMoves.contains(move)){
                x = random.nextInt(8);
                y = random.nextInt(8);
                move = new Pair<>(x, y);
            }
            chromosome.getGenes().add(move);
        }
        return chromosome;
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
        for (int i = 0; i < populationSize; i++){
            Chromosome chromosome = makeChromosome(rounds);
            // check if the chromosome is unique (by its genes)
            while (population.contains(chromosome)){
                chromosome = makeChromosome(rounds);
            }
        }

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
        /*
        * The idea is as follows:
        * 1. Select two concurrent parents from the list
        * 2. Generate a random crossover point (but make sure that the crossover point it an even number)
        * 3. Create two children by combining the genes of the parents (make sure that all genes are unique in the resulting children)
        * 4. Repeat 1-3 until the number of children is equal to the number of parents
        * 5. Return the list of children
        * */
        return children;
    }

    private void mutate(List<Chromosome> children) {
        children.forEach(Chromosome::mutate);
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
