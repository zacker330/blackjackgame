package com.zhaizhijun.blackjack.core;

import java.math.BigDecimal;

/**
 * User: zjzhai
 * Date: 1/21/14
 */
public class Player implements Comparable<Player> {
    private String name;

    public Player(String name) {
        assert !_.isEmpty(name);
        this.name = name;
    }


    public String getName() {
        return name;
    }


    /**
     * @param bet
     * @return
     */
    public PlayerProxy createPlayerProxy(BigDecimal bet) {
        return new PlayerProxy(this, bet);
    }

    public PlayerProxy createPlayerProxy() {
        return new PlayerProxy(this);
    }


    public DealerProxy createDealerProxy() {
        return new DealerProxy(this);
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Player)) return false;

        Player player = (Player) o;

        if (name != null ? !name.equals(player.name) : player.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public int compareTo(Player o) {
        return o.name.compareTo(name);
    }
}
