package com.zhaizhijun.blackjack.core.playerAction;

import com.zhaizhijun.blackjack.core.PlayerProxy;
import com.zhaizhijun.blackjack.core.Tables;
import com.zhaizhijun.blackjack.core.card.PlayerAction;

/**
 * 分牌
 * User: zjzhai
 * Date: 2/7/14
 */
public class SplitAction extends PlayerAction {
    public final static String _name = "split";


    public SplitAction() {
        super(_name);
    }

    @Override
    public void invoke(PlayerProxy playerProxy, Tables tables, Object... args) {
        PlayerProxy newPlayerProxy = playerProxy.split();
        assert newPlayerProxy.getHeadCard().getCardCount() == 1;
        assert playerProxy.getHeadCard().getCardCount() == 1;

        tables.deal(newPlayerProxy);
        tables.deal(playerProxy);

        newPlayerProxy.flushCardCategory();
        playerProxy.flushCardCategory();

        tables.addPlayerProxy(newPlayerProxy);


    }
}
