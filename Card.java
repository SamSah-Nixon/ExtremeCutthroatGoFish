/*
 * Sam Sah-Nixon
 * Date Created: 01/27/23
 * Last Modified: 02/02/23
 * Description: A class that represents a card.
 */
public class Card implements Comparable<Card>{

    // The id of the card. The sorting of the id starts with the lowest ranks coming first priority, then the suits.
    private final int id;

    public Card(int id) {
        this.id = id;
    }

    // Special Getters
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
    // Getters
    public int getId(){ return id; }


    /**
     * Check if the rank is valid.
     * @param rank The rank to check.
     * @return True if the rank is valid, false otherwise.
     */
    public static boolean validRank(String rank){
        if(rank.equals("Ace") || rank.equals("Jack") || rank.equals("Queen") || rank.equals("King"))
            return true;
        try{
            int r = Integer.parseInt(rank);
            if(r > 1 && r < 11)
                return true;
        } catch (NumberFormatException e){
            return false;
        }
        return false;
    }

    /**
     * Return a string representation of the card.
     * @return A string representation of the card.
     */
    public String toString() {
        return getRank() + "/" + getSuit();
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
