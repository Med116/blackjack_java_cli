package game;

import model.Card;
import model.Dealer;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Game {

    public List<Card> cards;
    private Dealer dealer;
    private List<Card> humanCards;
    private List<Card> dealerCards;
    private boolean gameOver = false;
    private static int roundCount = 0;
    private static int humanWins = 0;
    private static int dealerWins = 0;
    private static int tieCount = 0;
    private static boolean humansTurn = true;
    private static final Scanner SCANNER = new Scanner(System.in);


    public Game() {

        System.out.println("Welcome to BLACKJACK!");
        this.dealer = new Dealer();
        this.cards = dealer.createDeck();
        cards = dealer.shuffleDeck(cards);

    }


    public String getCardsDisplayText(List<Card> cardsToPrint, boolean human) {
        int cardCount = sumCards(cardsToPrint);

        String cardNames = cardsToPrint.stream()
                .map(Card::toString)
                .collect(Collectors.joining(" | "));

        String cardVals = cardsToPrint.stream()
                .map(card -> "[" + card.getCardVal() + "]")
                .collect(Collectors.joining(""));


        return String.format("%s Cards =>  %s - ,( %s ) --- Sum: %d", human ? "Your " : "Dealer's ", cardNames, cardVals, cardCount);
    }

    public int sumCards(List<Card> cardsToCount) {
        int sum = cardsToCount.stream()
                .mapToInt(Card::getCardVal)
                .sum();

        int aceCount = (int) cardsToCount.stream()
                .filter(card -> card.isAce() && card.getCardVal() == 11)
                .count();
        if (aceCount > 0) {
            int aceSum = cardsToCount.stream()
                    .filter(card -> card.isAce())
                    .mapToInt(Card::getCardVal)
                    .sum();
            int nonAceSum = cardsToCount.stream()
                    .filter(card -> !card.isAce())
                    .mapToInt(Card::getCardVal)
                    .sum();

            if (aceSum + nonAceSum > 21) {
                while (aceCount > 0) {

                    for (int i = 0; i < cardsToCount.size(); i++) {
                        Card card = cardsToCount.get(i);
                        if (card.isAce() && card.getCardVal() == 11) {
                            card.setCardVal(1);
                            cardsToCount.set(i, card);
                            break;
                        }
                        aceCount = (int) cardsToCount.stream()
                                .filter(c -> c.isAce() && c.getCardVal() == 11)
                                .count();
                    }
                    sum = cardsToCount.stream()
                            .mapToInt(Card::getCardVal)
                            .sum();
                }
            }
        }
        return sum;
    }

    public void start() {

        while (cardsLeftCount() > 0) {
            roundCount++;

            if (cardsLeftCount() > 4) {
                humanCards = dealer.deal(2, cards);
                dealerCards = dealer.deal(2, cards);
            } else {
                System.out.println("Not enough cards to deal");
                break;
            }

            printStats();

            if (sumCards(humanCards) == 21 && sumCards(dealerCards) == 21) {
                System.out.println("TIE");
                tieCount++;
                continue;
            }

            if (sumCards(humanCards) == 21 && sumCards(dealerCards) < 21) {
                System.out.println("YOU WIN ON DEAL");
                humanWins++;
                continue;
            }

            if (sumCards(dealerCards) == 21 && sumCards(humanCards) < 21) {
                dealerWins++;
                System.out.println("DEALER WINS ON DEAL");
                continue;
            }


            System.out.println("YOUR TURN : ROUND: " + roundCount + "\n####\nCARDS LEFT " + cardsLeftCount());
            // human
            System.out.println(getCardsDisplayText(humanCards, true));

            if (cardsLeftCount() > 1) {
                boolean humanStay = humanDecide(humanCards);
                if (sumCards(humanCards) < 21) {
                    while (humanStay == false) {
                        humanStay = humanDecide(humanCards);
                        System.out.println(getCardsDisplayText(humanCards, true));
                        if (sumCards(humanCards) >= 21) {
                            System.out.println("Your cards >= 21, breaking");
                            break;
                        }
                    }
                } else if (sumCards(humanCards) == 21) {
                    System.out.println("You Got 21!, Dealers turn noe");
                } else {
                    System.out.println("You Busted :(");
                }
            }

            // dealer
            System.out.println("Dealers turn");
            while (sumCards(dealerCards) < 17) {
                boolean enoughCards = hit(dealerCards, false);
                if (!enoughCards) {
                    break;
                }
                System.out.println(getCardsDisplayText(dealerCards, false));

            }
            System.out.println("Dealer stayed");

            int humanSum = sumCards(humanCards);
            int dealerSum = sumCards(dealerCards);
            System.out.println(String.format("Human %s, Dealer %s", humanSum, dealerSum));

            if (humanSum > 21 && dealerSum > 21) {
                System.out.println("DEALER WINS (BOTH BUSTED)");
                dealerWins++;
                continue;
            }

            if (humanSum <= 21 && dealerSum <= 21) {

                if (humanSum > dealerSum) {
                    System.out.println("YOU WIN!");
                    humanWins++;
                } else if (dealerSum > humanSum) {
                    System.out.println("DEALER WINS :(");
                    dealerWins++;
                } else if (dealerSum == humanSum) {
                    System.out.println("YOU TIED UP");
                    tieCount++;
                } else {
                    System.out.println("DEAULT CONDITION");
                }
            }
            if (humanSum <= 21 && dealerSum > 21) {
                System.out.println("DEALER BUSTED");
                humanWins++;
            }

            if (dealerSum <= 21 && humanSum > 21) {
                System.out.println("YOU BUSTED");
                dealerWins++;
            }


        }
        System.out.println("ALL DONE, NO MORE CARDS");
        printStats();


    }

    private void printStats() {

        System.out.println("\t YOU :" + humanWins);
        System.out.println("\t DEALER: " + dealerWins);
        System.out.println("\t TIES: " + tieCount);
    }

    private int cardsLeftCount() {
        return (int) cards.stream().filter(card -> card.isDealt() == false).count();
    }


    /*
    * if a hit makes cards over 21, the true is sent back for going over
     */
    public boolean hit(List<Card> playersCards, boolean human) {
        if (cardsLeftCount() > 0) {
            List<Card> cardHitList = dealer.deal(1, cards);
            Card cardHit = cardHitList.get(0);
            playersCards.add(cardHit);
            if (human) {
                System.out.println(getCardsDisplayText(playersCards, human));

            } else {
                // show thinking ...
                for (int i = 0; i < 5; i++) {
                    try {
                        Thread.sleep(500);
                        System.out.print(".");
                        if (i == 4) {
                            System.out.println("");
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
                System.out.println("Dealer Hit a card.");
                System.out.println(getCardsDisplayText(playersCards, human));
            }
        } else {
            System.out.println("Tried to deal a card when there were no cards left");
            return false;
        }
        return true;
    }


    private boolean humanDecide(List<Card> humanCards) {
        System.out.println("Press 'H' to Draw/Hit a new card, or 'S' to stand (Then hit enter)");
        String humanAction = SCANNER.nextLine().toLowerCase();
        if (humanAction.equals("h")) {
            hit(humanCards, true);
            return false;
        } else if (humanAction.equals("s")) {
            return true;
        } else {
            System.out.println("Please enter 'S' to stand or 'H' to hit.");
            return humanDecide(humanCards);
        }
    }
}