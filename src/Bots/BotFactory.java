package Bots;

import GameStateBetter.GameStateBetter;

public class BotFactory {
    public static Bot getBot(GameStateBetter gameState, String name, String playerType) {
        switch (name) {
            case "Minimax Bot":
                return new MiniMaxBot(gameState, playerType);
            case "Simulated Annealing Bot":
                return new LocalSearchBot(gameState, playerType);
            case "Genetic Algorithm Bot":
                return new GeneticAlgorithmBot(gameState, playerType);
        }

        return null;
    }
}
