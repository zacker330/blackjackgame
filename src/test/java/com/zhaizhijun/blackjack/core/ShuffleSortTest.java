package com.zhaizhijun.blackjack.core;

import com.zhaizhijun.blackjack.core.card.Card;
import com.zhaizhijun.blackjack.core.card.Deck;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 1/22/14
 */
public class ShuffleSortTest {


    @Test
    public void test() {

        Map<Card, Integer> resultMap = new HashMap<Card, Integer>();
        for (int count = 0; count < 1000000; count++) {
            DealerRobot dealerRobot = new DealerRobot(1);
            Card card = dealerRobot.deal();
            if (resultMap.get(card) != null) {
                resultMap.put(card, (resultMap.get(card) + 1));
            } else {
                resultMap.put(card, 1);
            }
        }

        for (Card card : Deck.getSomeDecksOfCards(1)) {
            System.out.println(card + ": " + resultMap.get(card) / (1000000.0 / 52));
        }

    }


}
