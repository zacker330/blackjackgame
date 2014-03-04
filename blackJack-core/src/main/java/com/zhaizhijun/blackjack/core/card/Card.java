package com.zhaizhijun.blackjack.core.card;


/**
 * 牌
 * User: zjzhai
 * Date: 1/21/14
 */
public class Card {

    /**
     * 每种花色具有的牌数
     *
     * @return
     */
    public static int EACH_CARDCOLOR_CONTAINS_CARDCOUNT = 13;

    private final CardColor color;

    private final CardFace cardFace;

    private Card(CardColor color, CardFace cardFace) {
        this.color = color;
        this.cardFace = cardFace;
    }

    public boolean isAce() {
        return CardFace.isAce(cardFace);
    }

    public int getValue() {
        return cardFace.getValue();
    }

    public CardFace getCardFace() {
        return cardFace;
    }

    public CardColor getColor() {
        return color;
    }

    public static Card create(CardColor color, CardFace cardFace) {
        return new Card(color, cardFace);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Card)) return false;

        Card card = (Card) o;

        if (cardFace != card.cardFace) return false;
        if (color != card.color) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = color != null ? color.hashCode() : 0;
        result = 31 * result + (cardFace != null ? cardFace.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "{" +
                "" + color +
                "-" + cardFace +
                '}';
    }
}
