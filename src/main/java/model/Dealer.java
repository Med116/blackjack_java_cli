package model;

import model.Card;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class Dealer {


    public Dealer() {

    }

    public List<Card> createDeck() {
        List<Card> deck = new ArrayList<>();

        for (Card.Suit suit : Card.Suit.values()) {
            for (int i = 1; i <= 13; i++) {
                deck.add(new Card(suit, i));
            }
        }

        return deck;
    }


    /**
     * Does an in place shuffle, could have used collections.shuffle
     * but this is a programming assignment so I didnt!
     */
    public List<Card> shuffleDeck(List<Card> deck) {

        int sizeOfDeck = deck.size();
        for (int i = 0; i < sizeOfDeck; i++) {
            Card current = deck.get(i);
            int randomIndex = (int) (Math.random() * sizeOfDeck);
            Card randomCard = deck.get(randomIndex);
            deck.set(randomIndex, current);
            deck.set(i, randomCard);
        }
        return deck;
    }


    public List<Card> deal(int howMany, List<Card> cards) {

        return cards.stream()
                .filter(card -> card.isDealt() == false)
                .limit(howMany)
                .map(card -> card.setDealt(true))
                .collect(Collectors.toList());

    }

}
