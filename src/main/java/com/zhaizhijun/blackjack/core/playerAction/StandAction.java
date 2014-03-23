package com.zhaizhijun.blackjack.core.playerAction;

import com.zhaizhijun.blackjack.core.PlayerProxy;
import com.zhaizhijun.blackjack.core.Tables;
import com.zhaizhijun.blackjack.core.card.PlayerAction;

/**
 * User: zjzhai
 * Date: 2/8/14
 */
public class StandAction extends PlayerAction {

    public final static String _name = "stand";

    public StandAction() {
        super(_name);
    }

    @Override
    public void invoke(PlayerProxy playerProxy, Tables tables, Object... args) {
        playerProxy.stop();
    }
}
