/*
 * Sam Sah-Nixon
 * Date Created: 01/27/23
 * Last Modified: 02/02/23
 * Description: The class that runs the game. Contains the methods for setting up the game and running the game
 */
import java.util.Scanner;

public class Game {
    CirclyList<Player> players;
    CirclyList<Card> deck;
    Player currentPlayer;

    public Game() {
        players = new CirclyList<>();
        deck = new CirclyList<>();
        currentPlayer = null;
    }

    /**
     * Sets up a game of go fish by asking the amount of players and decks to be used
     */
    public void gameSetupText(){
        System.out.println("Welcome to Go Fish!");
        System.out.println("Input Q to quit at any time.");
        players.append(new Player(ask("What is your name?"), false));

        //Ask for number of AI players
        int aiNumber = 0;
        do{
            try{
                aiNumber = Integer.parseInt(ask("How many ai players?"));
            } catch(Exception e){
                System.out.println("Invalid input. Please input an integer");
            }
            if(aiNumber > 3 || aiNumber  < 1)
                System.out.println("Number of AI players must be between 1 and 3");
        } while (aiNumber > 3 || aiNumber < 1);

        //Ask for names of players
        String name;
        for(int i = 1; i <= aiNumber; i++){
            name = ask("What is the name of AI player " + i + "?");
            if(notValidPlayer(name))
                players.append(new Player(name, true));
            else {
                System.out.println("Name already taken. Please choose another name");
                i--;
            }
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
    }
    
    /**
     * Runs the game
     */
    public void playGame() {
        //Choose random starting player
        currentPlayer = players.valueAt((int)(Math.random()*players.size()));
        System.out.println("Starting player is " + currentPlayer.getName());
        boolean gameEnd = false;
        boolean anotherTurn;
        //This runs until the deck is empty
        while(!gameEnd){
            try{
                anotherTurn = true;
                //If turn returns true then the player takes another turn
                //Else switch to the next player and run again
                while(anotherTurn)
                    anotherTurn = turn();
            } catch(Exception e){
                System.err.println("Error: " + e);
            }
            currentPlayer = players.next(currentPlayer);
            if(checkEndGame()) {
                gameEnd = true;
                gameEnd();
            }
        }
    }

    /**
     * Runs a turn of one player
     * @return true if the player takes another turn, false otherwise
     * @throws InterruptedException if the thread is interrupted while sleeping
     */
    public boolean turn() throws InterruptedException {
        System.out.println("\nThere are " + deck.size() + " cards left in the deck");
        System.out.println("\n\n" + currentPlayer.getName() + "'s turn");
        currentPlayer.sortCards();
        String cardName;
        //Askee = the player you are asking a card for
        Player askee;
        //Gets the card and the player to ask for if the player has cards left
        do {
            Thread.sleep(500);
            cardName = askForCard();
        } while(cardName == null);
        do{
            Thread.sleep(500);
            askee = askForAskee();
        } while(askee == null);
        System.out.println(currentPlayer.getName() + " is asking " + askee.getName() + " for a " + cardName);
        //If they don't have the card then go fish
        if (askee.hasRank(cardName) == null) {
            System.out.println("Go Fish!");
            goFish();
            return false;
        } else {
            int count = 0;
            //While the player has the card, give it to the current player and count how many they have taken
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

    /**
     * Asks the player for the rank of a card they want to ask for
     * @return the rank of the card
     */
    public String askForCard(){
        if(currentPlayer.isReal()){
            currentPlayer.printHand();
            String cardName = ask("\nWhat card are you looking for? (2-10, Jack, Queen, King, Ace)");
            if (Card.validRank(cardName) && currentPlayer.hasRank(cardName) != null) {
                return cardName;
            } else if (!Card.validRank(cardName)) {
                System.out.println("Invalid card. Try again");
                return null;
            }
            else {
                System.out.println("You don't have that card. Try again");
                return null;
            }
        }
        else {
            int randomRank;
            do
                randomRank = (int)(Math.random()*13);
                while (currentPlayer.hasRank(randomRank) == null);
            return new Card((randomRank-1)*4).getRank();
        }
    }

    /**
     * Asks the player who they would like to ask for a card
     * @return the player they would like to ask
     */
    public Player askForAskee() {
        if(currentPlayer.isReal()) {
            System.out.print("Who would you like to ask? (");
            //Print all players except yourself
            for (int i = 0; i < players.size() - 1; i++) {
                if (players.valueAt(i) != currentPlayer)
                    System.out.print(players.valueAt(i).getName()+",");
            }
            System.out.print(players.valueAt(players.size()-1).getName()+")");
            String playerName = ask("");
            //Prevent asking yourself
            if (playerName.equals(currentPlayer.getName())) {
                System.out.println("You can't ask yourself!");
                return null;
            } else if (notValidPlayer(playerName)) {
                System.out.println("Invalid player. Try again");
                return null;
            } else
                return findPlayer(playerName);
        }
        else {
            Player askee;
            do
                askee = players.valueAt((int)(Math.random()*players.size()));
                while (askee.getName().equals(currentPlayer.getName()));
            return askee;
        }
    }

    /**
     * Makes the current player pick up a card from the deck
     */
    public void goFish(){
        //Addresses the player as "you" if they are not an AI
        if(deck.size() > 0){
            currentPlayer.addHand(deck.pop());
            if(currentPlayer.isReal()){
                System.out.println("You picked up a "+currentPlayer.getHand().rear());
            }
            else {
                System.out.println(currentPlayer.getName() + " picked up a card");
            }
            currentPlayer.sortCards();
            currentPlayer.checkCompleteSet();
        } else {
            System.out.println("No more cards in deck so you cannot go fish");
        }
    }

    /**
     * Checks if the game has ended
     * @return true if the game has ended, false otherwise
     */
    private boolean checkEndGame() {
        for(int i = 0; i < players.size(); i++){
            if(players.valueAt(i).getHand().size() == 0)
                return true;
        }
        return false;
    }

    /**
     * Ends the game
     */
    public void gameEnd(){
        System.out.println("Game Over");
        StringBuilder result = new StringBuilder();
        int largest = 0;
        //Go through every player and find who has the most finished sets
        for(int i = 0; i<players.size(); i++){
            if(largest<players.valueAt(i).getFinishedSets()){
                largest = players.valueAt(i).getFinishedSets();
                result = new StringBuilder(players.valueAt(i).getName());
            }
            else if(largest==players.valueAt(i).getFinishedSets()){
                result.append(" and ").append(players.valueAt(i).getName());
            }
        }
        System.out.println("The Winner(s) are : "+result +" with "+largest+" sets!");
    }
    
    /**
     * Checks to see if a string is the name of a valid player in the game
     * @param playerName the string to check
     * @return false if the string is the name of a valid player, true otherwise
     */
    public boolean notValidPlayer(String playerName){
        for(int i = 0; i < players.size(); i++){
            if(players.valueAt(i).getName().equals(playerName)){
                return false;
            }
        }
        return true;
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
        CirclyList<Card> shuffled = new CirclyList<>();
        int deckSize = deck.size();
        for(int i = 0; i < deckSize; i++){
            int random = (int)(Math.random() * deck.size());
            shuffled.append(deck.valueAt(random));
            deck.pop(random);
        }
        deck = shuffled;
    }

    /**
     * Deals the same amount of cards to each player
     * @param cardsPerPlayer the amount of cards each player will get
     */
    public void dealCards(int cardsPerPlayer){
        for(int i = 0; i < cardsPerPlayer; i++){
            for(int j = 0; j < players.size(); j++){
                players.valueAt(j).addHand(deck.pop());
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
        Scanner scanner = new Scanner(System.in);
        String answer = scanner.next();
        if(answer.equalsIgnoreCase("Q")){
            System.out.println("Quitting game");
            System.exit(0);
        }
        return answer;
    }
}
