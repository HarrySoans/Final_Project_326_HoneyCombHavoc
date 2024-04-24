import java.util.Random;
import java.util.Scanner;

public class HoneycombGame {
    private static final int HONEYCOMB_PENALTY = -1;

    static Random random = new Random();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to Honeycomb Havoc!");
        System.out.println("Try to collect as many fruits without but at the same time avoid the Honeycombs!");
        System.out.println("The fruits are 0 and the Honeycombs are X");
        System.out.println("Enter the total number of items (fruits + honeycombs):");
        int totalItems = scanner.nextInt();

        int honeycombs = random.nextInt(totalItems/2); // Generate a random number of honeycombs

        // Generate the array representing fruits and honeycombs
        char[] fruitsAndHoneycombs = generateFruitsAndHoneycombs(totalItems, honeycombs);
        System.out.println("Fruits and Honeycombs:");
        displayGameState(fruitsAndHoneycombs);

        int playerPoints = 0;
        int aiPoints = 0;

        // Game loop
        while (!isGameEnded(fruitsAndHoneycombs)) {
            // Player's turn
            System.out.println("Player's turn:");
            int playerChoice = getPlayerInput(scanner, fruitsAndHoneycombs);
            playerPoints += pointCount(playerChoice, fruitsAndHoneycombs);
            totalItems -= playerChoice;
            removeItems(fruitsAndHoneycombs, playerChoice);
            displayGameState(fruitsAndHoneycombs);
            if (isGameEnded(fruitsAndHoneycombs)) break;

            // AI's turn
            System.out.println("AI's turn:");
            int aiChoice = minimax(totalItems, true);
            System.out.println("AI chooses to remove " + aiChoice + " item(s).");
            aiPoints += pointCount(aiChoice, fruitsAndHoneycombs);
            totalItems -= aiChoice;
            removeItems(fruitsAndHoneycombs, aiChoice);
            displayGameState(fruitsAndHoneycombs);
        }

        // Display points and winner
        System.out.println("Player points: " + playerPoints);
        System.out.println("AI points: " + aiPoints);
        if (playerPoints > aiPoints) {
            System.out.println("Player wins!");
        } else if (playerPoints < aiPoints) {
            System.out.println("AI wins!");
        } else {
            System.out.println("It's a tie!");
        }
    }

    private static int pointCount(int playerChoice, char[] fruitsAndHoneycombs) {
        int pointsGained = 0;
        int position = 0;
        for(int i =0; i<playerChoice; i++){
            while(fruitsAndHoneycombs[position] == ' '){
                position ++;
            }
            if(fruitsAndHoneycombs[position] == 'O'){
                pointsGained++;
            }
            if(fruitsAndHoneycombs[position] == 'X'){
                pointsGained += HONEYCOMB_PENALTY;
            }
            if(i == 0){
                position++;
            }
        }
        return pointsGained;
    }

    private static int getPlayerInput(Scanner scanner, char[] fruitsAndHoneycombs) {
        int choice;
        do {
            System.out.println("Enter 1 or 2 to remove that many items:");
            choice = scanner.nextInt();
        } while (choice < 1 || choice > 2 || !isValidMove(choice, fruitsAndHoneycombs));
        return choice;
    }

    private static boolean isValidMove(int choice, char[] fruitsAndHoneycombs) {
        int count = 0;
        for (char c : fruitsAndHoneycombs) {
            if (c != ' ') count++;
        }
        return choice <= count;
    }

    private static boolean isGameEnded(char[] fruitsAndHoneycombs) {
        for (char c : fruitsAndHoneycombs) {
            if (c != ' ') return false;
        }
        return true;
    }

    private static void displayGameState(char[] fruitsAndHoneycombs) {
        System.out.println("Fruits and Honeycombs:");
        for (char item : fruitsAndHoneycombs) {
            System.out.print(item + " ");
        }
        System.out.println();
    }

    private static void removeItems(char[] array, int count) {
        for (int i = 0; i < array.length && count > 0; i++) {
            if (array[i] != ' ') {
                array[i] = ' '; // Mark as removed
                count--;
            }
        }
    }

    private static char[] generateFruitsAndHoneycombs(int totalItems, int honeycombs) {
        char[] fruitsAndHoneycombs = new char[totalItems];
        int numHoneycombs = 0;
        fruitsAndHoneycombs[0] = 'O';
        for (int i = 1; i < totalItems; i++) {
            if (numHoneycombs < honeycombs) {
                int rand = random.nextInt(2);
                if(rand == 1) {
                    fruitsAndHoneycombs[i] = 'X'; // Representing honeycombs with 'X'
                    numHoneycombs++;
                }else{
                    fruitsAndHoneycombs[i] = 'O';
                }
            } else {
                fruitsAndHoneycombs[i] = 'O'; // Representing fruits with 'O'
            }
        }
        fruitsAndHoneycombs[totalItems-1] = 'X';
        return fruitsAndHoneycombs;
    }

    private static int minimax(int itemsLeft, boolean isMaximizingPlayer) {
        if (itemsLeft <= 0) {
            // Base case: no items left, return 0
            return 0;
        }

        // If the maximizing player's turn
        if (isMaximizingPlayer) {
            int maxScore = Integer.MIN_VALUE;
            for (int i = 1; i <= 2; i++) {
                if (itemsLeft - i >= 0) {
                    int score = minimax(itemsLeft - i, false);
                    maxScore = Math.max(maxScore, score);
                }
            }
            return maxScore;
        } else {
            // If the minimizing player's turn
            int minScore = Integer.MAX_VALUE;
            for (int i = 1; i <= 2; i++) {
                if (itemsLeft - i >= 0) {
                    int score = minimax(itemsLeft - i, true);
                    if (itemsLeft - i == 0) {
                        // Penalty for collecting a honeycomb
                        score += HONEYCOMB_PENALTY;
                    }
                    minScore = Math.min(minScore, score);
                }
            }
            return minScore;
        }
    }
}
