package com.zhaizhijun.blackjack.core.playerAction;

import com.zhaizhijun.blackjack.core.PlayerProxy;
import com.zhaizhijun.blackjack.core.Tables;
import com.zhaizhijun.blackjack.core.card.PlayerAction;

/**
 * 投降
 *
 * User: zjzhai
 * Date: 2/7/14
 */
public class SurrenderAction extends PlayerAction {

    public final static String _name = "surrender";


    public SurrenderAction() {
        super(_name);
    }

    @Override
    public void invoke(PlayerProxy playerProxy, Tables tables, Object... args) {
        playerProxy.surrender();
    }
}
