package com.zhaizhijun.blackjack.core;

import com.zhaizhijun.blackjack.core.DealerRobot;
import com.zhaizhijun.blackjack.core.card.Card;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: zjzhai
 * Date: 1/27/14
 */
public class DealerRobotTest {

    @Test
    public void testName() {
        int deckCount = 8;

        DealerRobot dealerRobot = new DealerRobot(deckCount);

        List<Card> cards = new ArrayList<Card>();


        while (dealerRobot.beLeftCardCount() > 0) {
            cards.add(dealerRobot.deal());
        }

        assert cards.size() == (deckCount * 52);

        // 重新洗牌
        dealerRobot.shuffle();

        assert dealerRobot.beLeftCardCount() == deckCount * 52;


    }


}


