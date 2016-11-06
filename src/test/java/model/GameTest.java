package model;

import game.Game;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by mark on 11/5/16.
 */
public class GameTest {


    @Test
    public void testSumCards() {


        Dealer dealer = new Dealer();
        List<Card> cards = dealer.createDeck();

        Card ace = cards.
                stream()
                .filter(card -> card.isAce())
                .limit(1)
                .findFirst()
                .get();

        Card king = cards
                .stream()
                .filter(card -> card.getCardName().startsWith("KING"))
                .findFirst()
                .get();
        System.out.println(king);

        Card eight = cards
                .stream()
                .filter(card -> card.getCardVal() == 8)
                .findFirst()
                .get();

        Card four = cards
                .stream()
                .filter(card -> card.getCardVal() == 4)
                .findFirst()
                .get();


        Game game = new Game();

        int sumOneAce = game.sumCards(Arrays.asList(ace));
        assertEquals(11, sumOneAce);

        List<Card> oneKingOneAce = new ArrayList<>();
        oneKingOneAce.add(ace);
        oneKingOneAce.add(king);
        int sumOneAceOneKing = game.sumCards(oneKingOneAce);
        assertEquals(21, sumOneAceOneKing);

        List<Card> aceKingEightFour = new ArrayList<>();
        aceKingEightFour.add(ace);
        aceKingEightFour.add(king);
        aceKingEightFour.add(eight);
        aceKingEightFour.add(four);

        assertEquals(23, game.sumCards(aceKingEightFour));
        assertEquals(4, aceKingEightFour.size());


    }

    @Test
    public void testHit() {

        Dealer dealer = new Dealer();
        List<Card> cards = dealer.createDeck();
        dealer.shuffleDeck(cards);
        Game game = new Game();

        List<Card> tenCards = cards.subList(0, 10);
        assertEquals(10, tenCards.size());
        game.hit(tenCards, true);

        assertEquals(11, tenCards.size());
        assertEquals(1, game.cards.stream().filter(card -> card.isDealt()).count());
        tenCards.stream().forEach(System.out::println);
    }

}
