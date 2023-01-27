public class GoFish {
    public static void main(String[] args) {  
        Game game = new Game();
        if(game.gameSetupText())
            game.playGame();
    }
}
