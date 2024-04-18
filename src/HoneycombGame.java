import java.util.Random;
import java.util.Scanner;

public class HoneycombGame {
    private static final int INFINITY = Integer.MAX_VALUE;
    private static final int HONEYCOMB_PENALTY = -1;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to the Honeycomb Game!");
        System.out.println("Enter the total number of items (fruits + honeycombs):");
        int totalItems = scanner.nextInt();

        Random random = new Random();
        int honeycombs = random.nextInt(totalItems); // Generate a random number of honeycombs

        // Generate the array representing fruits and honeycombs
        char[] fruitsAndHoneycombs = generateFruitsAndHoneycombs(totalItems, honeycombs);
        System.out.println("Fruits and Honeycombs:");
        for (char item : fruitsAndHoneycombs) {
            System.out.print(item + " ");
        }
        System.out.println();

        int result = minimax(totalItems - honeycombs, true);
        System.out.println("The optimal outcome for the AI player is to collect " + result + " fruits.");
    }

    private static char[] generateFruitsAndHoneycombs(int totalItems, int honeycombs) {
        char[] fruitsAndHoneycombs = new char[totalItems];
        for (int i = 0; i < totalItems; i++) {
            if (i < honeycombs) {
                fruitsAndHoneycombs[i] = 'X'; // Representing honeycombs with 'X'
            } else {
                fruitsAndHoneycombs[i] = '0'; // Representing fruits with '0'
            }
        }
        return fruitsAndHoneycombs;
    }

    private static int minimax(int itemsLeft, boolean isMaximizingPlayer) {
        if (itemsLeft <= 0) {
            // Base case: no items left, return 0
            return 0;
        }

        // If the maximizing player's turn
        if (isMaximizingPlayer) {
            int maxScore = -INFINITY;
            for (int i = 1; i <= 2; i++) {
                int score = minimax(itemsLeft - i, false);
                maxScore = Math.max(maxScore, score);
            }
            return maxScore;
        } else {
            // If the minimizing player's turn
            int minScore = INFINITY;
            for (int i = 1; i <= 2; i++) {
                int score = minimax(itemsLeft - i, true);
                if (itemsLeft - i <= 0) {
                    // Penalty for collecting a honeycomb
                    score += HONEYCOMB_PENALTY;
                }
                minScore = Math.min(minScore, score);
            }
            return minScore;
        }
    }
}

