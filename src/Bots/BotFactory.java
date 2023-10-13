package Bots;

import GameStateBetter.GameStateBetter;
import javafx.scene.control.Button;

public class BotFactory {
    public static Bot getBot(GameStateBetter gameState, String name, String playerType)
    {
        return switch (name) {
            case "Minimax Bot" -> new MiniMaxBot(gameState, playerType);
            case "Simulated Annealing Bot" -> new LocalSearchBot(gameState, playerType);
            case "Genetic Algorithm Bot" -> new GeneticAlgorithmBot(gameState, playerType);
            default -> null;
        };

    }
}
