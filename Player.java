/*
 * Sam Sah-Nixon
 * Date Created: 01/27/23
 * Last Modified: 02/02/23
 * Description: A class that represents a player.
 */
public class Player implements Comparable<Player>{
    private final String name;
    private CirclyList<Card> hand;

    // The number of sets the player has completed
    private int finishedSets;

    private final boolean ai;

    public Player(String name, boolean ai) {
        this.name = name;
        hand = new CirclyList<>();
        finishedSets = 0;
        this.ai = ai;
    }

    // Getters and setters
    public String getName() {
        return name;
    }

    public CirclyList<Card> getHand() {
        return hand;
    }
    public int getFinishedSets(){
        return finishedSets;
    }
    public boolean isReal(){
        return !ai;
    }

    /**
     * Checks if a player has 4 of the same rank card. If so, then remove it from the deck
     */
    public void checkCompleteSet(){
        int count = 0;
        for(int i = 1; i < hand.size(); i++){
            if(hand.valueAt(i).getRank().equals(hand.valueAt(i-1).getRank()))
                count++;
            else
                count = 0;
            //If there are 3 cards of the same rank in a row, then remove them from the player's hand
            if(count == 3){
                System.out.println(name+" got a set of "+hand.valueAt(i).getRank()+"s!");
                //Remove the 4 cards from the player's hand
                removeHand(hand.valueAt(i));
                removeHand(hand.valueAt(i - 1));
                removeHand(hand.valueAt(i - 2));
                removeHand(hand.valueAt(i - 3));
                finishedSets++;
                System.out.println(name+" now has "+finishedSets+" finished sets.");
                return;
            }
        }
    }

    /**
     * Sorts the player's hand by rank.
     */
    public void sortCards(){
        for(int i = 0; i < hand.size() - 1; i++){
            int minIndex = i;
            for(int j = i + 1; j < hand.size(); j++){
                if(hand.valueAt(j).getId() < hand.valueAt(minIndex).getId()){
                    minIndex = j;
                }
            }
            hand.swap(i, minIndex);
        }
    }

    /**
     * Prints the player's hand.
     */
    public void printHand(){
        System.out.println(name + "'s hand:");
        String prevRank = "";
        for(int i = 0; i < hand.size(); i++){
            if(prevRank.equals(hand.valueAt(i).getRank()))
                System.out.print(hand.valueAt(i)+", ");
            else
                System.out.print("\n"+hand.valueAt(i)+", ");
            prevRank = hand.valueAt(i).getRank();
        }
        System.out.println();
    }

    /**
     * Adds a card to the player's hand.
     * @param card the card to add
     */
    public void addHand(Card card) {
        this.hand.append(card);
    }

    /**
     * Removes the card from the player's hand.
     * @param card the card to remove
     */
    public void removeHand(Card card) {
        this.hand.remove(card);
    }

    /**
     * Checks if the player has a card with the given rank.
     * @param rank the rank to check for
     * @return the first instance of a card with the given rank, or null if the player does not have a card with the given rank
     */
    public Card hasRank(String rank){
        for(int i = 0; i< hand.size(); i++){
            if(hand.valueAt(i).getRank().equals(rank)){
                return hand.valueAt(i);
            }
        }
        return null;
    }

    public Card hasRank(int rank){
        for(int i = 0; i< hand.size(); i++){
            if(hand.valueAt(i).getId() / 4 + 1 == rank){
                return hand.valueAt(i);
            }
        }
        return null;
    }
    /**
     * Returns a string representation of the player.
     * @return A string representation of the player.
     */
    public String toString() {
        return name + " has " + hand.size() + " cards";
    }

    /**
     * Returns 0. This is a required method for the Comparable interface.
     * @param o The other player to compare to.
     * @return 0
     */
    public int compareTo(Player o) {
        return 0;
    }
}
