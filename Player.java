public class Player implements Comparable<Player>{
    private String name;
    private CirclyList<Card> hand;
    private int finishedSets;

    private final boolean ai;

    public Player(String name, boolean ai) {
        this.name = name;
        hand = new CirclyList<Card>();
        finishedSets = 0;
        this.ai = ai;
    }

    // Getters and setters
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }

    public CirclyList<Card> getHand() {
        return hand;
    }
    public int getFinishedSets(){
        return finishedSets;
    }
    public boolean isAi(){
        return ai;
    }
    public void setFinishedSets(int finishedSets){
        this.finishedSets = finishedSets;
    }

    /**
     * Checks if a player has 4 of the same rank card. If so, then remove it from the deck
     * @return the rank of the set that was removed and null of no rank was removed
     */
    public String checkCompleteSet(){
        int count = 0;
        for(int i = 1; i < hand.size(); i++){
            if(hand.valueAt(i).getRank().equals(hand.valueAt(i-1).getRank()))
                count++;
            else
                count = 0;
            if(count == 3){
                System.out.println(name+" got a set of "+hand.valueAt(i).getRank()+"s!");
                String removedRank = hand.valueAt(i).getRank();
                removeHand(hand.valueAt(i));
                removeHand(hand.valueAt(i - 1));
                removeHand(hand.valueAt(i - 2));
                removeHand(hand.valueAt(i - 3));
                finishedSets++;
                System.out.println("You now have "+finishedSets+" finished sets.");
                return removedRank;
            }
        }
        return null;
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

    public boolean equals(Player other){
        return this.name.equals(other.name);
    }

    /**
     * Prints the player's hand.
     */
    public void printHand(){
        System.out.println(getName() + "'s hand:");
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
     * Returns true if the player has the card in their hand.
     * @param card the card to check for
     * @return true if the player has the card in their hand, false otherwise
     */
    public boolean hasCard(Card card) {
        return this.hand.contains(card);
    }

    /**
     * Checks if the player has a card with the given rank.
     * @param rank the rank to check for
     * @return true if the player has a card with the given rank, false otherwise
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
