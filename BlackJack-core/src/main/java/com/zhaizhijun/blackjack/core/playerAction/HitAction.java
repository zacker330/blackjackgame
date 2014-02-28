package com.zhaizhijun.blackjack.core.playerAction;

import com.zhaizhijun.blackjack.core.PlayerProxy;
import com.zhaizhijun.blackjack.core.Tables;

/**
 * User: zjzhai
 * Date: 2/7/14
 */
public class HitAction extends AbstractPlayerAction {

    public final static String _name = "hit";


    public HitAction() {
        super(_name);
    }

    @Override
    public void invoke(PlayerProxy playerProxy, Tables tables, Object... args) {
        tables.deal(playerProxy);

    }
}
