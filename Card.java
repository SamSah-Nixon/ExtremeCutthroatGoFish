public class Card implements Comparable<Card>{
    private final int id;

    public Card(int id) {
        this.id = id;
    }

    // Getters
    public String getRank() {
        return switch (id / 4) {
            case 0 -> "Ace";
            case 10 -> "Jack";
            case 11 -> "Queen";
            case 12 -> "King";
            default -> Integer.toString(id / 4 + 1);
        };
    }
    public String getSuit() {
        return switch (id % 4) {
            case 0 -> "Spades";
            case 1 -> "Clubs";
            case 2 -> "Diamonds";
            case 3 -> "Hearts";
            default -> "Invalid suit";
        };
    }

    public int getId(){ return id; }
    /**
     * Check if the rank of a card is equal to the rank of another card.
     * @param other The other card to compare to.
     * @return True if the card ranks are equal, false otherwise.
     */
    public boolean equals(Card other) {
        return (this.id % 13) == (other.id % 13);
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
        return getRank() + " of " + getSuit();
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
