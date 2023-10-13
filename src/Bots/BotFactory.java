package Bots;

import javafx.scene.control.Button;

public class BotFactory {
    public static Bot getBot(Button[][] buttons, String name, String playerType) {
        switch (name) {
            case "Minimax Bot":
                return new MiniMaxBot(buttons, playerType);
            case "Simulated Annealing Bot":
                return new HillClimbingBot(buttons, playerType);
            case "Genetic Algorithm Bot":
                return new GeneticAlgorithmBot(buttons, playerType);
        }

        return null;
    }
}
