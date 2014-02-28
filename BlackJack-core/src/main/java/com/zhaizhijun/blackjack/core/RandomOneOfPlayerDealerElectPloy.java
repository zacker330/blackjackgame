package com.zhaizhijun.blackjack.core;

import java.util.*;

/**
 * 从所有玩家中随机抽取一名玩家为庄家
 * User: zjzhai
 * Date: 2/20/14
 */
public class RandomOneOfPlayerDealerElectPloy implements DealerElectPloy {


    @Override
    public ElectedResult elect(Set<Player> players) {

        DealerProxy dealerProxy = selectLuckyPlayer(players).createDealerProxy();

        List<PlayerProxy> playerProxies = new ArrayList<PlayerProxy>();

        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            Player player = it.next();
            if (dealerProxy.getPlayer().equals(player)) {
                continue;
            }
            playerProxies.add(player.createPlayerProxy());
        }
        return new ElectedResult(dealerProxy, playerProxies);
    }

    private Player selectLuckyPlayer(Set<Player> players){
        return players.toArray(new Player[players.size()])[new Random(System.currentTimeMillis()).nextInt(3)];
    }
}
