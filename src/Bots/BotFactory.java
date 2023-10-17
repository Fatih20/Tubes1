package Bots;

import GameStateBetter.GameStateBetter;

public class BotFactory {
    public static Bot getBot(String name, String playerType, GameStateBetter gameState) {
        switch (name) {
            case "Minimax Bot":
                return new MiniMaxBot(playerType);
            case "Hill Climbing Bot":
                return new HillClimbingBot(playerType);
            case "Genetic Algorithm Bot":
                return new GeneticAlgorithmBot(gameState, playerType);
        }

        return null;
    }
}
