package com.zhaizhijun.blackjack.core.playerAction;

import com.zhaizhijun.blackjack.core.PlayerProxy;
import com.zhaizhijun.blackjack.core.Tables;
import com.zhaizhijun.blackjack.core.card.PlayerAction;

/**
 * 结束
 * User: zjzhai
 * Date: 2/8/14
 */
public class StopAction extends PlayerAction {

    public final static String _name = "stop";

    public StopAction() {
        super(_name);
    }

    @Override
    public void invoke(PlayerProxy playerProxy, Tables tables, Object... args) {
        playerProxy.stop();
    }
}
