public class Card implements Comparable<Card>{
    private String rank;
    private String suit;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    // Getters and setters
    public String getRank() {
        return rank;
    }
    public String getSuit() {
        return suit;
    }
    public void setRank(String rank) {
        this.rank = rank;
    } 
    public void setSuit(String suit) {
        this.suit = suit;
    }

    /**
     * Check if the rank of a card is equal to the rank of another card.
     * @param other The other card to compare to.
     * @return True if the card ranks are equal, false otherwise.
     */
    public boolean equals(Card other) {
        return (this.rank == other.rank);
    }

    public int rankToInt(){
        switch(rank){
            case "Ace":
                return 1;
            case "Jack":
                return 11;
            case "Queen":
                return 12;
            case "King":
                return 13;
            default:
                return Integer.parseInt(rank);
        }
    }

    /**
     * Check if the rank is valid.
     * @param rank The rank to check.
     * @return True if the rank is valid, false otherwise.
     */
    public static boolean validRank(String rank){
        String[] validRanks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        for(int i = 0; i < validRanks.length; i++){
            if(rank.equals(validRanks[i])){
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the suit is valid.
     * @param suit The suit to check.
     * @return True if the suit is valid, false otherwise.
     */
    public static boolean validSuit(String suit){
        String[] validSuits = {"Spades", "Clubs", "Diamonds", "Hearts"};
        for(int i = 0; i < validSuits.length; i++){
            if(suit == validSuits[i]){
                return true;
            }
        }
        return false;
    }

    /**
     * Return a string representation of the card.
     * @return A string representation of the card.
     */
    public String toString() {
        return rank + " of " + suit;
    }

    /**
     * Return 0. Required for the card to be comparable.
     * @param o The card to compare to.
     * @return 0
     */
    public int compareTo(Card o) {
        return 0;
    }

}
