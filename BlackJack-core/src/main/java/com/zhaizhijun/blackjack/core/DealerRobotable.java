package com.zhaizhijun.blackjack.core;

import com.zhaizhijun.blackjack.core.card.Card;

/**
 * 发牌员接口
 * User: zjzhai
 * Date: 2/23/14
 */
public interface DealerRobotable {
    Card deal();

    void shuffle();

}
