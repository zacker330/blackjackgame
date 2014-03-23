package com.zhaizhijun.blackjack.core.card;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 手上的牌
 * User: zjzhai
 * Date: 1/22/14
 */
public class HeadCard {

    /**
     * 21点，最大的点数
     */
    public static final int BLACK_JACK_MAX = 21;

    /**
     * 是否属于分牌
     */
    private boolean split = false;

    private List<Card> cards = new LinkedList<Card>();

    private CardCategory cardCategory;

    private CardCategoryCalculateEngine cardCategoryCalculateEngine = new GroovyCardCategoryCalculateEngine();


    public HeadCard() {
    }

    public HeadCard(CardCategoryCalculateEngine engine) {
        cardCategoryCalculateEngine = engine;
    }

    /**
     * 点数
     *
     * @return
     */
    public int getSumPoint() {
        int sum = 0;
        int aceNumber = 0;
        for (Card card : cards) {
            if (card.isAce()) {
                aceNumber++;
                sum += 11;
            } else {
                sum += card.getValue();
            }
        }

        while (sum > BLACK_JACK_MAX && aceNumber > 0) {
            sum -= 10;
            aceNumber--;
        }
        return sum;
    }

    public boolean isBlackjack() {
        return getCards().size() == 2 && getSumPoint() == BLACK_JACK_MAX;
    }

    public boolean isSplit() {
        return split;
    }

    public int getCardCount() {
        return cards.size();
    }

    /**
     * 是否同花
     *
     * @return
     */
    public boolean isCardFaceIdentical() {
        assert !cards.isEmpty();
        CardColor firstCardColor = cards.get(0).getColor();
        for (Card each : cards) {
            if (!firstCardColor.equals(each.getColor())) {
                return false;
            }
        }
        return true;
    }


    public List<CardFace> getAllCardFace() {
        List<CardFace> cardFaces = new ArrayList<CardFace>();

        for (Card card : cards) {
            cardFaces.add(card.getCardFace());
        }

        return cardFaces;
    }


    /**
     * 头两张牌的点数相同
     *
     * @return
     */
    public boolean isFirstTwoCardPointEquals() {
        assert getCardCount() == 2;
        return cards.get(0).getValue() == cards.get(1).getValue();
    }

    /**
     * 第一张明牌
     *
     * @return
     */
    public Card getFirstSeenCard() {
        return cards.get(1);
    }

    /**
     * 得到暗牌
     *
     * @return
     */
    public Card getBlindCard() {
        return cards.get(0);
    }

    /**
     * 是否包含该牌面
     *
     * @param values [1:13]
     * @return
     */
    public boolean containsAllCardFace(Integer... values) {
        for (Integer face : values) {
            if (!getAllCardFace().contains(CardFace.get(face))) return false;
        }
        return true;
    }

    public HeadCard addCard(Card card) {
        cards.add(card);
        return this;
    }

    public List<Card> getCards() {
        return cards;
    }

    public boolean isBust() {
        return getSumPoint() > BLACK_JACK_MAX;
    }

    public boolean isNotBust() {
        return !isBust();
    }

    public Card split() {
        split = true;
        return ((LinkedList<Card>) cards).pop();
    }

    public CardCategory getCardCategory() {
        return cardCategory;
    }

    public void flushCardCategory() {
        cardCategory = cardCategoryCalculateEngine.calculate(this);
    }

    @Override
    public String toString() {
        return "HeadCard{" +
                "split=" + split +
                ", cards=" + cards +
                ", cardCategory=" + cardCategory +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HeadCard)) return false;

        HeadCard headCard = (HeadCard) o;

        if (split != headCard.split) return false;
        if (cardCategory != null ? !cardCategory.equals(headCard.cardCategory) : headCard.cardCategory != null)
            return false;
        if (cards != null ? !cards.equals(headCard.cards) : headCard.cards != null) return false;

        return true;
    }

    public void setSplit(boolean split) {
        this.split = split;
    }

    @Override
    public int hashCode() {
        int result = (split ? 1 : 0);
        result = 31 * result + (cards != null ? cards.hashCode() : 0);
        result = 31 * result + (cardCategory != null ? cardCategory.hashCode() : 0);
        return result;
    }


}
