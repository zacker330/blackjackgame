package com.zhaizhijun.blackjack.core;

import java.util.List;

/**
 * 庄家选举结果
 * User: zjzhai
 * Date: 2/23/14
 */
public class ElectedResult {

    private DealerProxy dealerProxy;

    private List<PlayerProxy> playerProxies;

    protected ElectedResult(DealerProxy dealerProxy, List<PlayerProxy> playerProxies) {
        this.dealerProxy = dealerProxy;
        this.playerProxies = playerProxies;
    }

    protected DealerProxy getDealerProxy() {
        return dealerProxy;
    }

    public List<PlayerProxy> getPlayerProxies() {
        return playerProxies;
    }
}
