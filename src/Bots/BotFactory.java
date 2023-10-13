package Bots;

public class BotFactory {
    public static Bot getBot(String name, String playerType) {
        switch (name) {
            case "Minimax Bot":
                return new MiniMaxBot(playerType);
            case "Hill Climbing Bot":
                return new HillClimbingBot(playerType);
            case "Genetic Algorithm Bot":
                return new GeneticAlgorithmBot(playerType);
        }

        return null;
    }
}
