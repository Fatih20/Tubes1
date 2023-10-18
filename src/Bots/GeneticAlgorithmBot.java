package Bots;

import GameStateBetter.GameStateBetter;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class GeneticAlgorithmBot extends Bot {
    private final int populationSize = 100;
    private List<Chromosome> population;
    private List<Pair<Integer,Integer>> bannedMoves;

    public GeneticAlgorithmBot(GameStateBetter gameState, String playerType) {
        super(gameState, playerType);
        this.bannedMoves = new ArrayList<>();
        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++) {
                if (gameState.getGameBoardMatrix()[i][j] != 0){
                    this.bannedMoves.add(new Pair<>(i, j));
                }
            }
        }
        this.population = initializePopulation(getGameState().getRemainingRound());
    }

    private Chromosome makeChromosome(int rounds){
        Chromosome chromosome = new Chromosome(new ArrayList<>(), this.isPlayerOne(), this.getGameState());
        Random random = new Random();

        // TODO: refactor to use the heuristic (also change Chromosome.java)
        /*
        * Idea is as follows:
        * 1. make the chromosome constructor have a parameter for the game state and length of rounds
        * */

        for (int i = 0; i < 2*rounds; i++){
            int x = random.nextInt(8);
            int y = random.nextInt(8);
            Pair<Integer, Integer> move = new Pair<>(x, y);
            while (bannedMoves.contains(move) || chromosome.getGenes().contains(move)){
                x = random.nextInt(8);
                y = random.nextInt(8);
                move = new Pair<>(x, y);
            }
            chromosome.getGenes().add(move);
        }

        chromosome.setFitness();
        return chromosome;
    }

    private List<Chromosome> initializePopulation(int rounds) {
        List<Chromosome> population = new ArrayList<>();
        /*
        * General idea is as follows:
        * 1. Determine the length of the chromosome (number of genes) = 2 * rounds
        * 2. Generate a random chromosome of the determined length by using heuristics
        * 3. Repeat 2. until populationSize chromosomes are generated
        * */
        for (int i = 0; i < populationSize; i++){
            Chromosome chromosome = makeChromosome(rounds);
            // check if the chromosome is unique (by its genes)
            while (population.stream().anyMatch(chromosome::equals)){
                chromosome = makeChromosome(rounds);
            }
            population.add(chromosome);
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
        // TODO: fix the crossover function (most cases stuck here)

        List<Chromosome> children = new ArrayList<>();
        /*
        * The idea is as follows:
        * 1. Select two concurrent parents from the list (i and i+1)
        * 2. Generate a random crossover point (but make sure that the crossover point it an even number)
        * 3. Create two children by combining the genes of the parents (make sure that all genes are unique in the resulting children)
        * 4. Repeat 1-3 until the number of children is equal to the number of parents
        * 5. Return the list of children
        * 6. Mutate the children
        *
        * NB: to pertain uniqueness of the genes for the children, the random crossover point will be tried 3 times before giving up and then the parents are just copied
        * */
        for (int i = 0; i < populationSize; i+=2) {
            Chromosome parent1 = parents.get(i);
            Chromosome parent2 = parents.get((i + 1) % populationSize);
            int tries = 0;
            boolean success = false;
            Random random = new Random();
            Chromosome child1 = null;
            Chromosome child2 = null;
            while (tries < 3 && !success) {
                int crossoverPoint = random.nextInt(parent1.getGenes().size()/2);
                crossoverPoint *= 2;
                List<Pair<Integer, Integer>> genes1 = new ArrayList<>(parent1.getGenes().subList(0, crossoverPoint));
                List<Pair<Integer, Integer>> genes2 = new ArrayList<>(parent2.getGenes().subList(0, crossoverPoint));
                genes1.addAll(parent2.getGenes().subList(crossoverPoint, parent2.getGenes().size()));
                genes2.addAll(parent1.getGenes().subList(crossoverPoint, parent1.getGenes().size()));
                if (genes1.stream().distinct().count() == genes1.size() && genes2.stream().distinct().count() == genes2.size()) {
                    success = true;
                    child1 = new Chromosome(genes1, this.isPlayerOne(), this.getGameState());
                    child2 = new Chromosome(genes2, this.isPlayerOne(), this.getGameState());
                } else {
                    tries++;
                }
            }
            if (!success) {
                child1 = new Chromosome(parent1.getGenes(), this.isPlayerOne(), this.getGameState());
                child2 = new Chromosome(parent2.getGenes(), this.isPlayerOne(), this.getGameState());
            }
            child1.mutate(bannedMoves);
            child2.mutate(bannedMoves);
            children.add(child1);
            children.add(child2);
        }
        return children;
    }

    public int[] move() {

        long startTime = System.currentTimeMillis();
        int maxGenerations = 100;
        for (int i = 0; i < maxGenerations; i++){
            List<Chromosome> parents = selectParents();
            List<Chromosome> children = crossover(parents);
            this.population = new ArrayList<>();
            this.population.addAll(children);

            if (System.currentTimeMillis() - startTime > 4000){
                break;
            }
        }
        long endTime = System.currentTimeMillis();

        this.population.sort((o1, o2) -> {
            return Integer.compare(o2.getFitness(), o1.getFitness());
        });
        Pair<Integer,Integer> res =  this.population.get(0).getGenes().get(0);
        return new int[]{res.getKey(), res.getValue()};
    }
}
