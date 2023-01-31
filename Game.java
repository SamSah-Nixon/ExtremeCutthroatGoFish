import java.util.List;
import java.util.Scanner;
public class Game {
    Scanner scanner = new Scanner(System.in);
    CirclyList<Player> players;
    CirclyList<Card> deck;

    public Game() {
        players = new CirclyList<>();
        deck = new CirclyList<>();
    }

    /**
     * Sets up a game of go fish by asking the amount of players and decks to be used
     * @return true if the game was set up successfully, false otherwise
     */
    public boolean gameSetupText(){
        int playerNumber = 0;
        //Ask for number of players
        do{
            try{
                playerNumber = Integer.parseInt(ask("How many players?"));
            } catch(Exception e){
                System.out.println("Invalid input. Please input an integer");
            }
            if((playerNumber < 1 || playerNumber > 4) && playerNumber != 607)
                System.out.println("Number must be greater than 0 and less than 5");
            //For testing purposes
            else if (playerNumber == 607){
                addPlayer(new Player("Sam", false));
                addPlayer(new Player("Seb", true));
                createDeck(1);
                shuffleDeck();
                dealCards(7);
                return true;
            }
        } while (playerNumber < 1 || playerNumber > 4);

        //Ask for number of AI players
        int aiNumber = 0;
        do{
            try{
                aiNumber = Integer.parseInt(ask("How many ai players?"));
            } catch(Exception e){
                System.out.println("Invalid input. Please input an integer");
            }
            if(aiNumber + playerNumber > 4 || aiNumber + playerNumber < 1)
                System.out.println("Number of all players must be greater than 1 and less than 5");
        } while (aiNumber + playerNumber > 4 || aiNumber + playerNumber < 1);

        //Ask for names of players
        for(int i = 1; i <= playerNumber; i++){
            addPlayer(new Player(ask("What is the name of player " + i + "?"), false));
        }
        for(int i = 1; i <= aiNumber; i++){
            addPlayer(new Player(ask("What is the name of the AI player " + i + "?"), true));
        }

        //Ask for number of decks
        int deckNumber = 0;
        do{
            try{
                deckNumber = Integer.parseInt(ask("How many decks? Choose a number between 1 and 3"));
            } catch(Exception e){
                System.out.println("Invalid input. Please input an integer");
            }
            if(deckNumber < 1 || deckNumber > 3)
                System.out.println("Number must be greater than 0 and less than 4");
        } while (deckNumber < 1 || deckNumber > 3);

        //Print out game setup
        System.out.println("Game Started with " + deckNumber + " decks, "+players.size()+" players and 7 cards per player");
        System.out.println("Player Order:");
        for(int i = 1; i <= players.size(); i++){
            System.out.println("Player " + i + ": " + players.valueAt(i-1).getName());
        }
        createDeck(deckNumber);
        shuffleDeck();
        dealCards(7);
        return true;
    }
    
    /**
     * Runs the game
     */
    public void playGame() {
        Player currentPlayer = players.valueAt((int)(Math.random()*players.size()));
        System.out.println("Starting player is " + currentPlayer.getName());
        boolean gameEnd = false;
        while(!gameEnd){
            try{
                while(turn(currentPlayer)){}
            } catch(Exception e){
                System.err.println("Error: " + e);
            }

            currentPlayer = nextPlayer(currentPlayer);
            if(deck.size() == 0)
                gameEnd = true;
        }
        gameEnd();
    }

    /**
     * Runs a turn of one player
     * @param currentPlayer the player whose turn it is
     * @return true if the player takes another turn, false otherwise
     */
    public boolean turn(Player currentPlayer) throws InterruptedException {
        Thread.sleep(250);
        System.out.println("\nThere are " + deck.size() + " cards left in the deck");
        System.out.println("\n\n" + currentPlayer.getName() + "'s turn");
        currentPlayer.sortCards();
        Thread.sleep(1000);
        String cardName;

        do {
            cardName = askForCard(currentPlayer);
        }
        while(cardName == null);

        Player askee;
        do{
            askee = askForAskee(currentPlayer);
        }
        while(askee == null);

        System.out.println(currentPlayer.getName() + " is asking " + askee.getName() + " for a " + cardName);
        if (askee.hasRank(cardName) == null) {
            System.out.println("Go Fish!");
            Thread.sleep(500);
            goFish(currentPlayer);
            return false;
        } else {
            int count = 0;
            while (askee.hasRank(cardName) != null) {
                giveCard(askee, currentPlayer, askee.hasRank(cardName));
                count++;
            }
            if (count == 1)
                System.out.println(askee.getName() + " has a " + cardName + "!");
            else
                System.out.println(askee.getName() + " has " + count + " " + cardName + "s!");
            Thread.sleep(1000);
            currentPlayer.sortCards();
            currentPlayer.checkCompleteSet();
            return true;
        }
    }


    public String askForCard(Player currentPlayer){
        if(!currentPlayer.isAi()){
            currentPlayer.printHand();
            String cardName = ask("\nWhat card are you looking for? (2-10, Jack, Queen, King, Ace)");
            if (Card.validRank(cardName)) {
                return cardName;
            } else {
                System.out.println("Invalid card. Try again");
                return null;
            }
        }
        else {
            int randomRank;
            do {
                randomRank = (int)(Math.random()*13);
            }
            while (currentPlayer.hasRank(randomRank) == null);
            return new Card((randomRank+1)*4).getRank();
        }
    }

    public Player askForAskee(Player currentPlayer) {
        if(!currentPlayer.isAi()) {
            System.out.println("Who would you like to ask?");
            //Print all players except yourself
            for (int i = 0; i < players.size(); i++) {
                if (players.valueAt(i) != currentPlayer)
                    System.out.println(players.valueAt(i).getName());
            }
            String playerName = scanner.next();
            //Prevent asking yourself
            if (playerName.equals(currentPlayer.getName())) {
                System.out.println("You can't ask yourself!");
                return null;
            } else if (!validPlayer(playerName)) {
                System.out.println("Invalid player. Try again");
                return null;
            } else
                return findPlayer(playerName);
        }
        else {
            Player askee;
            do {
                askee = players.valueAt((int)(Math.random()*players.size()));
            }
            while (askee.equals(currentPlayer));
            return askee;
        }
    }

    public void goFish(Player currentPlayer){
        String player = currentPlayer.isAi() ? currentPlayer.getName() : "You";
        if(deck.size() > 0){
            pickUpCard(currentPlayer);
            System.out.println(player + " picked up a "+currentPlayer.getHand().rear());
            currentPlayer.sortCards();
            currentPlayer.checkCompleteSet();
        } else {
            System.out.println("No more cards in deck so you cannot go fish");
        }
    }

    /**
     * Ends the game
     */
    public void gameEnd(){
        System.out.println("Game Over");
        StringBuilder result = new StringBuilder();
        int largest = 0;
        for(int i = 0; i<players.size(); i++){
            if(largest<players.valueAt(i).getFinishedSets()){
                largest = players.valueAt(i).getFinishedSets();
                result = new StringBuilder(players.valueAt(i).getName());
            }
            else if(largest==players.valueAt(i).getFinishedSets()){
                result.append(" and ").append(players.valueAt(i).getName());
            }
        }
        System.out.println("The Winner(s) are : "+result);
    }

    /**
     *  Moves to the next player
     *  @param player the current player
     *  @return the next player
     */
    public Player nextPlayer(Player player){
        return players.next(player);
    }
    
    /**
     * Checks to see if a string is the name of a valid player in the game
     * @param playerName the string to check
     * @return true if the string is the name of a valid player, false otherwise
     */
    public boolean validPlayer(String playerName){
        for(int i = 0; i < players.size(); i++){
            if(players.valueAt(i).getName().equals(playerName)){
                return true;
            }
        }
        return false;
    }

    /**
     * Adds a player to the game
     * @param player the player to be added
     */
    public void addPlayer(Player player) {
        players.append(player);
    }

    /**
     * Removes a player from the game
     * @param player the player to be removed
     */
    public void removePlayer(Player player) {
        players.remove(player);
    }

    /**
     * Finds a player in the game by name
     * @param name the name of the player to be found
     * @return the player with the given name, null if no player with that name is found
     */
    public Player findPlayer(String name) {
        for (int i = 0; i < players.size(); i++) {
            if (players.valueAt(i).getName().equals(name)) {
                return players.valueAt(i);
            }
        }
        return null;
    }

    /**
     * Creates a deck with the given number of sets of cards
     * @param setNumber the number of sets of cards to be in the duck
     */
    public void createDeck(int setNumber){
        for(int i = 0; i < setNumber; i++){
            for(int j = 0; j < 52; j++){
                deck.append(new Card(j));
            }
        }
    }

    /**
     * Shuffles the deck
     */
    public void shuffleDeck(){
        CirclyList<Card> shuffled = new CirclyList<Card>();
        int deckSize = deck.size();
        for(int i = 0; i < deckSize; i++){
            int random = (int)(Math.random() * deck.size());
            shuffled.append(deck.valueAt(random));
            deck.pop(random);
        }
        deck = shuffled;
    }

    /**
     * Gives a card from the deck to a player
     * @param player the player who will get the card
     */
    public void pickUpCard(Player player){
        player.addHand(deck.pop());
    }

    /**
     * Deals the same amount of cards to each player
     * @param cardsPerPlayer the amount of cards each player will get
     */
    public void dealCards(int cardsPerPlayer){
        for(int i = 0; i < cardsPerPlayer; i++){
            for(int j = 0; j < players.size(); j++){
                pickUpCard(players.valueAt(j));
            }
        }
    }

    /**
     * Gives a card from one player to another
     * @param giver the person giving the card
     * @param getter the person getting the card
     * @param card the card being given
     */
    public void giveCard(Player giver, Player getter, Card card){
        giver.removeHand(card);
        getter.addHand(card);
    }

    /**
     * Asks a question and returns the answer
     * @param question the question to be asked
     * @return the answer to the question
     */
    public String ask(String question){
        System.out.println(question);
        return scanner.next();
    }
}
