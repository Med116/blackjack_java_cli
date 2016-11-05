package util;

import model.Card;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by mark on 11/5/16.
 */
public class UtilTest {

    @Test
    public void testCreateDeck(){

        List<Card> cards = new Dealer().createDeck();
        assertEquals(true,cards.size() > 0);
        assertEquals(52, cards.size());
        cards.stream().forEach(card -> {
            if(!card.getCardName().contains("ACE")){
                assertEquals(true, card.getCardVal() > 0);
            }else{
                assertEquals(-1, card.getCardVal());
            }

            System.out.println("CARD NAME : " + card);
        });

    }

    @Test
    public void testShuffle(){
        Dealer dealer = new Dealer();
        List<Card> cards = dealer.createDeck();
        String cardsStringJoined = cards.stream()
                .map(Card::getCardVal)
                .map(String::valueOf)
                .collect(Collectors.joining(" | "));
        System.out.println("CREATED: UNSHUFFLED");
        System.out.println(cardsStringJoined);

        cards = dealer.shuffleDeck(cards);
        String cardsShuffledStringJoined = cards.stream()
                .map(Card::getCardVal)
                .map(String::valueOf)
                .collect(Collectors.joining(" | "));
        System.out.println("SHUFFLED");
        System.out.println(cardsShuffledStringJoined);
        assertEquals(52, cards.size());
        assertNotEquals(cardsStringJoined, cardsShuffledStringJoined );

    }
    @Test
    public void testDeal(){
        Dealer dealer = new Dealer();
        List<Card> cards = dealer.createDeck();
        List<Card> cardsDealt = dealer.deal(2, cards);
        assertEquals(2, cardsDealt.size());
        long countDealt = cards.stream()
                .filter(card -> card.isDealt() == true)
                .count();
        assertEquals(2, countDealt);

        long countNotDealt = cards.stream()
                .filter(card -> card.isDealt() == false)
                .count();
        assertEquals(50, countNotDealt);

        List<Card> nextCards = dealer.deal(1, cards);
        assertEquals(1, nextCards.size());

    }

}
