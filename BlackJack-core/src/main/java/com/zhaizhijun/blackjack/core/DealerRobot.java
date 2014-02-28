package com.zhaizhijun.blackjack.core;

import com.zhaizhijun.blackjack.core.card.Card;
import com.zhaizhijun.blackjack.core.card.Deck;

import java.util.LinkedList;

/**
 * 发牌员
 * User: zjzhai
 * Date: 1/23/14
 */
public class DealerRobot implements DealerRobotable {

    private final int deckCount;

    private Card[] cards;

    /**
     * 可替换洗牌策略
     */
    private ShuffleStrategy shuffleStrategy = new SimpleShuffle();

    /**
     * 存放洗牌后的栈
     */
    private LinkedList<Card> cardStack = new LinkedList<Card>();

    public DealerRobot() {
        deckCount = 4;
        this.cards = Deck.getSomeDecksOfCards(deckCount);
        shuffle();
    }

    public DealerRobot(int deckCount) {
        assert deckCount > 0;
        this.deckCount = deckCount;
        this.cards = Deck.getSomeDecksOfCards(deckCount);
        shuffle();
    }

    public DealerRobot(int deckCounter, ShuffleStrategy shuffleStrategy) {
        this(deckCounter);
        this.shuffleStrategy = shuffleStrategy;
        shuffle();
    }

    /**
     * 洗牌
     */
    public void shuffle() {
        cardStack.clear();
        Integer[] sortArray = shuffleStrategy.shuffle(createIntArray(deckCount * Deck.CARD_COUNT));
        for (int index : sortArray) {
            cardStack.addFirst(cards[index]);
        }
    }

    /**
     * 发牌
     *
     * @return
     */
    public Card deal() {
        return cardStack.pop();
    }


    /**
     * 剩下的牌数
     *
     * @return
     */
    public int beLeftCardCount() {
        return cardStack.size();
    }

    private int[] createIntArray(int maxNumber) {
        int[] result = new int[maxNumber];

        for (int i = 0; i < maxNumber; i++) {
            result[i] = i;
        }
        return result;

    }

    public int getDeckCount() {
        return deckCount;
    }

}
