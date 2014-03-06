package com.zhaizhijun.blackjack.core.card;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 整副牌
 * User: zjzhai
 * Date: 1/21/14
 */
public class Deck {


    /**
     * 每副牌的数量 = 花色 * 每种花色包含的牌的数量
     */
    public final static int CARD_COUNT = 52;


    private final Card[] cards = new Card[CARD_COUNT];

    /**
     * 未洗牌的
     */
    private Deck() {
        int i = 0;
        for (int eachCardNumber = 0; eachCardNumber < Card.EACH_CARDCOLOR_CONTAINS_CARDCOUNT; eachCardNumber++) {
            for (int cardColorIndex = 0; cardColorIndex < CardColor.values().length; cardColorIndex++) {
                cards[i] = Card.create(CardColor.values()[cardColorIndex], CardFace.values()[eachCardNumber]);
                i++;
            }
        }

    }


    public static List<Deck> getSomeDecks(int frenchDeckCount) {
        assert frenchDeckCount > 0;
        List<Deck> result = new ArrayList<Deck>();
        for (int i = 0; i < frenchDeckCount; i++) {
            result.add(new Deck());
        }
        return result;
    }

    public static Card[] getSomeDecksOfCards(int deckCount) {
        assert deckCount > 0;

        List<Deck> decks = getSomeDecks(deckCount);

        List<Card> result = new ArrayList<Card>();

        for (Deck deck : decks) {
            result.addAll(Arrays.asList(deck.cards));
        }

        return result.toArray(new Card[CARD_COUNT * deckCount]);
    }


}
