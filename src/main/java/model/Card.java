package model;


public class Card {

    private Suit suit;
    private String cardName;
    private int cardVal;
    private boolean royalty = false;
    private boolean ace = false;

    private boolean dealt = false;

    public Suit getSuit() {
        return suit;
    }

    public void setSuit(Suit suit) {
        this.suit = suit;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    public int getCardVal() {
        return cardVal;
    }

    public void setCardVal(int cardVal) {
        this.cardVal = cardVal;
    }

    public boolean isDealt() {
        return dealt;
    }

    public Card setDealt(boolean dealt) {
        this.dealt = dealt;
        return this;
    }

    public boolean isAce() {
        return ace;
    }

    public void setAce(boolean ace) {
        this.ace = ace;
    }

    public static enum Suit{ HEARTS, CLUBS, DIAMONDS, SPADES}

    public Card(Suit suit, int cardVal ){

        this.setSuit(suit);
        this.setCardVal(cardVal);
        initCard();

    }

    private void  initCard(){

        String ofSuit = " of " + getSuit() + "'s";

        switch(getCardVal()) {
            case 1:
                setCardName("ACE");
                setAce(true);
                setCardVal(11);
                break;
            case 11:
                setCardName("JACK");
                setCardVal(10);
                royalty =true;
                break;
            case 12:
                setCardName("QUEEN");
                setCardVal(10);
                royalty =true;
                break;
            case 13:
                setCardName("KING");
                setCardVal(10);
                royalty =true;
                break;
            default:
                setCardName(String.valueOf(getCardVal()));

        }
        setCardName(getCardName().concat(ofSuit));
    }


    @Override
    public String toString(){
        return cardName + ( royalty || isAce() ?  "=(" + cardVal + ")" : "");
    }
}
