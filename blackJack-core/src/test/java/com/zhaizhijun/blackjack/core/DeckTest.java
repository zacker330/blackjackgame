package com.zhaizhijun.blackjack.core;

import com.zhaizhijun.blackjack.core.card.Deck;
import com.zhaizhijun.blackjack.core.card.Card;
import org.junit.Test;

import java.util.List;

/**
 * User: zjzhai
 * Date: 1/25/14
 */
public class DeckTest {

    @Test
    public void testConstruction() throws Exception {
        int deckCount = 4;

        assert Deck.CARD_COUNT == 52;

        List<Deck> decks = Deck.getSomeDecks(deckCount);

        assert decks.size() == deckCount;

        Card[] cards = Deck.getSomeDecksOfCards(2);

        assert cards.length == (52 * 2);


    }
}
