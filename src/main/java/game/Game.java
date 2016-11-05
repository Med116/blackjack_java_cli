package game;

import model.Card;
import util.Dealer;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Game {

    private List<Card> cards;
    private Dealer dealer;
    private List<Card> humanCards;
    private List<Card> dealerCards;
    private boolean gameOver = false;
    private static int roundCount = 0;
    private static int humanWins = 0;
    private static int dealerWins = 0;
    private static int tieCount = 0;


    public Game(){

        System.out.println("Welcome to BLACKJACK!");
        this.dealer = new Dealer();
        this.cards = dealer.createDeck();
        start();
    }


    public String getCardsDisplayText(List<Card> cardsToPrint, boolean human){
        String cardNames =   cardsToPrint.stream()
                .map(Card::toString)
                .collect(Collectors.joining(" | "));

        String cardVals = cardsToPrint.stream()
                .map(card -> "[" + card.getCardVal() + "]")
                .collect(Collectors.joining(""));

        int cardCount = checkCards(cardsToPrint);

        return String.format("%s Cards =>  %s ---  or ,( %s ) --- Sum: %d", human ? "Your "  : "Dealer's " , cardNames, cardVals, cardCount);
    }

    public int checkCards(List<Card> cardsToCount){
        int count = cardsToCount.stream()
                .mapToInt(Card::getCardVal)
                .sum();

        // recursively turns aces into 1's if the count is > 21, until there are no more aces in the players handH
        if(count > 21){
            for(Card card : cards){
                if(card.isAce() && card.getCardVal() == 11){
                    card.setCardVal(1);
                    return checkCards(cardsToCount);
                }
            }
        }

        return count;
    }

    public void start() {

        cards = dealer.shuffleDeck(cards);
        Scanner scanner = new Scanner(System.in);
        boolean humansTurn = true;
        boolean humanStay = false;


        // compare cards - see if they are both 21 to see if its a tie, or a win for the 21 getter

        while (cardsLeftCount() > 0 && gameOver == false) {



            humanCards = dealer.deal(2, cards);
            dealerCards = dealer.deal(2, cards);
            roundCount++;
            System.out.println("=== ROUND " + roundCount + " ====");
            System.out.println("YOU : " + humanWins + " | DEALER: " + dealerWins);
            System.out.println("CARDS LEFT: " + cardsLeftCount());
            System.out.println("===============");
            humansTurn = true;

            // we only do this login on first round, because the data will be changed on subsequent rounds
            if (checkCards(humanCards) == 21 && checkCards(dealerCards) == 21) {
                gameOver("You Both Got 21 to start with Tie Game!", scanner);
                tieCount++;
                continue;
            }

            if (checkCards(humanCards) == 21) {
                gameOver("YOU GOT 21 (and the dealer didn't)! YOU WON!", scanner);
                humanWins++;
                continue;
            }

            if (checkCards(dealerCards) == 21) {
                gameOver("DEALER GOT 21 (and the you didn't)! YOU LOST :(", scanner);
                dealerWins++;
                continue;
            }



            if (humansTurn) {
                System.out.println(getCardsDisplayText(humanCards, true));
                if(cardsLeftCount() > 0){
                    humanStay = humanDecide(humanCards, scanner);


                    if (checkCards(humanCards) > 21) {
                        System.out.println("YOU BUSTED!");
                        humansTurn = false;
                        humanStay = true;

                    }
                    while (humanStay == false) {

                        humanStay = humanDecide(humanCards, scanner);  //"'H' Hit returns false" 'S' true

                        if (humanStay == true) {
                            humansTurn = false; // forces top loop to go to dealers condition
                        }

                        if (checkCards(humanCards) > 21) {
                            System.out.println("YOU BUSTED!");
                            humansTurn = false; // forces top loop to go to dealers condition
                            humanStay = true; // gets out of this while
                        }
                    }
                    humansTurn = false;
                }else{
                    System.out.println("No more cards");
                    gameOver = true;
                    break;
                }

            } else {
                // this is the dealers logic
                System.out.println("DEALERS TURN");
                int countOfDealersCards = checkCards(dealerCards);
                while (countOfDealersCards < 17) {
                    if(cardsLeftCount() > 0){
                        hit(dealerCards, false);
                        if (checkCards(dealerCards) > 21) {
                            System.out.println("DEALER BUSTED!");
                        }
                        countOfDealersCards = checkCards(dealerCards);
                    }else{
                        System.out.println("No more cards");
                        gameOver = true;
                        break;
                    }

                }
                System.out.println("DEALER STAYED");
            }

            System.out.println("ROUND DONE, HERE ARE THE RESULTS");
            System.out.println(getCardsDisplayText(dealerCards, false));

            System.out.println(getCardsDisplayText(humanCards, true));

            int totalHumanCount = checkCards(humanCards);
            int totalDealerCount = checkCards(dealerCards);

            if (totalDealerCount > 21 && totalHumanCount > 21) {
                gameOver("You Both Busted, Dealer Wins", scanner);
                dealerWins++;
                continue;
            }

            if (totalHumanCount > 21 && totalDealerCount <= 21) {
                gameOver("You Busted, Dealer Wins", scanner);
                dealerWins++;
                continue;
            }

            if (totalDealerCount > 21 && totalHumanCount <= 21) {
                gameOver("Dealer Busted, You Win", scanner);
                humanWins++;
                continue;

            }

            if (totalDealerCount == totalHumanCount) {
                gameOver("You Both Had the same score, (" + totalHumanCount + ") Tie Game", scanner);
                tieCount++;
                continue;
            }

            if (totalDealerCount > totalHumanCount) {
                gameOver("DEALER WON :(", scanner);
                dealerWins++;
                continue;

            } else {
                gameOver("YOU WON :)", scanner);
                humanWins++;
            }

        }

        System.out.println("GAME OVER, Thanks For Playing!");

        System.out.println("You played " + roundCount + " rounds");
        System.out.println("You won " + humanWins + " times.");
        System.out.println("The dealer won " + dealerWins + " times.");
        System.out.println("The tied " + tieCount + " times.");



    }


    private int cardsLeftCount(){
        return (int) cards.stream().filter(card -> card.isDealt() == false).count();
    }

    private void gameOver(String msg, Scanner scanner){
        System.out.println(msg);
        //System.out.println("Enter 'Y' to continue playing, (any other key to stop) there are " + cardsLeftCount() + " cards left ");
        //gameOver  = scanner.nextLine().toLowerCase().equals("y") ? false : true;
    }

    /*
    * if a hit makes cards over 21, the true is sent back for going over
     */
    private boolean hit(List<Card> playersCards, boolean human){
        if(cardsLeftCount() > 0){
            List<Card> cardHitList = dealer.deal(1, cards);
            Card cardHit = cardHitList.get(0);
            playersCards.add(cardHit);
            if(human){
                System.out.println(getCardsDisplayText(playersCards, human));

            }else{
                // show thinking ...
                for(int i = 0; i < 5 ; i++){
                    try{
                        Thread.sleep(500);
                        System.out.print(".");
                        if(i == 4){
                            System.out.println("");
                        }
                    }catch (InterruptedException e){
                        e.printStackTrace();
                    }

                }
                System.out.println("Dealer Hit a card.");
                System.out.println(getCardsDisplayText(playersCards,  human));
            }
        }else{
            System.out.println("Tried to deal a card when there were no cards left");
            return false;
        }
        return true;
    }


    private boolean humanDecide(List<Card> humanCards, Scanner scanner){
        System.out.println("Press 'H' to Draw/Hit a new card, or 'S' to stand (Then hit enter)");
        String humanAction = scanner.nextLine().toLowerCase();
        if(humanAction.equals("h")){
            hit(humanCards, true);
            return false;
        }else if(humanAction.equals("s")){
            return true;
        }else{
            System.out.println("Please enter 'S' to stand or 'H' to hit.");
            return humanDecide(humanCards, scanner);
        }
    }

}
