package com.zhaizhijun.blackjack.core;

import java.util.*;

/**
 * 从所有玩家中随机抽取一名玩家为庄家
 * User: zjzhai
 * Date: 2/20/14
 */
public class RandomOneOfPlayerDealerElectPloy implements DealerElectPloy {


    @Override
    public ElectedResult elect(Set<String> players) {

        DealerProxy dealerProxy = DealerProxy.createDealerProxy(selectLuckyPlayer(players));

        List<PlayerProxy> playerProxies = new ArrayList<PlayerProxy>();

        Iterator<String> it = players.iterator();
        while (it.hasNext()) {
            String player = it.next();

            if (dealerProxy.getPlayer().equals(player)) continue;

            playerProxies.add(PlayerProxy.createPlayerProxy(player));
        }
        return new ElectedResult(dealerProxy, playerProxies);
    }

    private String selectLuckyPlayer(Set<String> players){
        return players.toArray(new String[players.size()])[new Random(System.currentTimeMillis()).nextInt(3)];
    }
}
