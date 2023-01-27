public class Card implements Comparable<Card>{
    private String rank;
    private String suit;

    private int id;

    public Card(int id) {
        this.id = id;
        rank = switch (id % 13) {
            case 0 -> "Ace";
            case 10 -> "Jack";
            case 11 -> "Queen";
            case 12 -> "King";
            default -> Integer.toString((id % 13) + 1);
        };
        suit = switch ( (id - (id % 13))/ 13) {
            case 0 -> "Spades";
            case 1 -> "Hearts";
            case 2 -> "Diamonds";
            case 3 -> "Clubs";
            default -> "Invalid";
        };
    }

    // Getters
    public String getRank() {
        return rank;
    }
    public String getSuit() {
        return suit;
    }

    public int getId(){ return id; }
    /**
     * Check if the rank of a card is equal to the rank of another card.
     * @param other The other card to compare to.
     * @return True if the card ranks are equal, false otherwise.
     */
    public boolean equals(Card other) {
        return this.rank.equals(other.rank);
    }

    /**
     * Convert the rank to an integer format
     * @return The integer representation of the rank.
     */
    public int rankToInt(){
        return id % 13;
    }

    /**
     * Check if the rank is valid.
     * @param rank The rank to check.
     * @return True if the rank is valid, false otherwise.
     */
    public static boolean validRank(String rank){
        String[] validRanks = {"2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King", "Ace"};
        for (String validRank : validRanks) {
            if (rank.equals(validRank)) {
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
        for (String validSuit : validSuits) {
            if (suit.equals(validSuit)) {
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
