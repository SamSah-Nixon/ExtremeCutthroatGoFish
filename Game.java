import java.util.Scanner;

public class Game {
    Scanner scanner = new Scanner(System.in);
    CirclyList<Player> players;
    CirclyList<Card> deck;

    public Game() {
        players = new CirclyList<Player>();
        deck = new CirclyList<Card>();
    }

    /**
     * Sets up a game of go fish by asking the amount of players and decks to be used
     * @return true if the game was set up successfully, false otherwise
     */
    public boolean gameSetupText(){
        try{
            System.out.println("How many players?");
            int playerNumber = scanner.nextInt();
            if(playerNumber < 2){
                System.out.println("Number must be greater than 1");
                return false;
            }
            else if (playerNumber > 4 && playerNumber != 607){
                System.out.println("Number must be less than 5");
                return false;
            }
            if(playerNumber == 607){
                addPlayer(new Player("a"));
                addPlayer(new Player("b"));
                createDeck(1);
                shuffleDeck();
                dealCards(7);
                return true;
            }
            for(int i = 1; i <= playerNumber; i++){
                System.out.println("What is the name of player " + i + "?");
                String name = scanner.next();
                addPlayer(new Player(name));
            }
            System.out.println("How many decks? Choose a number between 1 and 3");
            int deckNumber = scanner.nextInt();
            if(deckNumber < 1 || deckNumber > 3){
                System.out.println("Number must be between 1 and 3");
                return false;
            }
            System.out.println("Game Started with " + deckNumber + " decks, "+players.size()+" players and 7 cards per player");
            System.out.println("Player Order:");
            for(int i = 1; i <= players.size(); i++){
                System.out.println("Player " + i + ": " + players.valueAt(i-1).getName());
            }
            createDeck(deckNumber);
            shuffleDeck();
            dealCards(7);
        } catch(Exception e){
            System.out.println("Invalid input. Please input an integer");
            return false;
        }
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
            while(turn(currentPlayer)){}
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
    public boolean turn(Player currentPlayer){
        System.out.println(currentPlayer.getName() + "'s turn");
        currentPlayer.sortCards();
        currentPlayer.printHand();
        System.out.println("\nWhat card are you looking for? (2-10, Jack, Queen, King, Ace)");
        String cardName = scanner.next();
        if(Card.validRank(cardName)){
            System.out.println("Who would you like to ask?");
            for(int i = 0; i < players.size(); i++){
                if(players.valueAt(i) != currentPlayer)
                    System.out.println(players.valueAt(i).getName());
            }
            String playerName = scanner.next();
            if(validPlayer(playerName) && playerName != currentPlayer.getName()){
                Player askee = findPlayer(playerName);
                if(askee.hasRank(cardName) == null){
                    System.out.println("Go Fish!");
                    if(deck.size() > 0){
                        pickUpCard(currentPlayer);
                        System.out.println("You picked up a "+currentPlayer.getHand().rear());
                        currentPlayer.sortCards();
                        currentPlayer.checkCompleteSet();
                    } else {
                        System.out.println("No more cards in deck");
                        return false;
                        
                    }
                    return false;
                }
                int count = 0;
                while(askee.hasRank(cardName) != null){
                    giveCard(askee, currentPlayer, askee.hasRank(cardName));
                    count++;
                }
                if(count == 1)
                    System.out.println(playerName + " has a "+ cardName + "!");
                else
                    System.out.println(playerName + " has " + count + " " + cardName + "s!");
                currentPlayer.sortCards();
                currentPlayer.checkCompleteSet();
                return false;
            } else {
                System.out.println("Invalid player. Try again");
                return true;
            }
        }
        else{
            System.out.println("Invalid card. Try again");
            return true;
        }
    }

    /**
     * Ends the game
     */
    public void gameEnd(){
        System.out.println("Game Over");
        String result = "";
        int largest = 0;
        for(int i = 0; i<players.size(); i++){
            if(largest<players.valueAt(i).getFinishedSets()){
                largest = players.valueAt(i).getFinishedSets();
                result = players.valueAt(i).getName();
            }
            else if(largest==players.valueAt(i).getFinishedSets()){
                result += " and " + players.valueAt(i).getName();
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
            for(int j = 0; j < 4; j++){
                for(int k = 1; k < 14; k++){
                    deck.append(createCard(j, k));
                }
            }
        }
    }

    /**
     * Creates a card with the given suit and rank in integer format
     * @param suit 0 = Spades, 1 = Hearts, 2 = Clubs, 3 = Diamonds
     * @param rank 0 = Ace, 1 = 2, 2 = 3, 3 = 4, 4 = 5, 5 = 6, 6 = 7, 7 = 8, 8 = 9, 9 = 10, 10 = Jack, 11 = Queen, 12 = King
     * @return the card created with the given suit and rank
     */
    public Card createCard(int suit, int rank){
        String rankString = Integer.toString(rank);
        String suitString = null;
        switch (suit) {
            case 0:
                suitString = "Spades";
                break;
            case 1:
                suitString = "Hearts";
                break;
            case 2:
                suitString = "Clubs";
                break;
            case 3:
                suitString = "Diamonds";
                break;
            }
        switch (rank){
                case 1:
                    rankString = "Ace";
                    break;
                case 11:
                    rankString = "Jack";
                    break;
                case 12:
                    rankString = "Queen";
                    break;
                case 13:
                    rankString = "King";
                    break;
            }
        return new Card(rankString, suitString);
    }

    /**
     * Shuffles the deck
     */
    public void shuffleDeck(){
        CirclyList<Card> shuffled = new CirclyList<Card>();
        for(int i = 0; i < deck.size(); i++){
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
}
