/*
 * Sam Sah-Nixon
 * Date Created: 01/27/23
 * Last Modified: 02/01/23
 * Description: Main class to run the game.
 */
public class GoFish {
    public static void main(String[] args) {
        System.out.println("=================================");
        System.out.println("          G O F I S H !          ");
        System.out.println("=================================");

        Game game = new Game();
        game.gameSetupText();
        game.playGame();

    }
}
