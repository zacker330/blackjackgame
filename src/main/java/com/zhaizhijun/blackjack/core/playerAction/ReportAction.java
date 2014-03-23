package com.zhaizhijun.blackjack.core.playerAction;

import com.zhaizhijun.blackjack.core.PlayerProxy;
import com.zhaizhijun.blackjack.core.Tables;
import com.zhaizhijun.blackjack.core.card.PlayerAction;

/**
 * 报到
 * User: zjzhai
 * Date: 2/7/14
 */
public class ReportAction extends PlayerAction {

    public final static String _name = "report";


    public ReportAction() {
        super(_name);
    }

    @Override
    public void invoke(PlayerProxy playerProxy, Tables tables, Object... args) {
        playerProxy.stop();
    }
}
