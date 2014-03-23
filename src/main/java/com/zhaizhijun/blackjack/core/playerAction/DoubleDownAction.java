package com.zhaizhijun.blackjack.core.playerAction;

import com.zhaizhijun.blackjack.core.PlayerProxy;
import com.zhaizhijun.blackjack.core.Tables;
import com.zhaizhijun.blackjack.core.card.PlayerAction;

/**
 * User: zjzhai
 * Date: 2/7/14
 */
public class DoubleDownAction extends PlayerAction {
    public final static String _name = "doubleDown";


    public DoubleDownAction() {
        super(_name);
    }

    @Override
    public void invoke(PlayerProxy playerProxy, Tables tables, Object... args) {
        tables.hit(playerProxy);
        playerProxy.doubleDown();
        playerProxy.stop();
    }
}
