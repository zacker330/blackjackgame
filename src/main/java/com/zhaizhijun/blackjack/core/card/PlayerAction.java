package com.zhaizhijun.blackjack.core.card;

import com.zhaizhijun.blackjack.core.PlayerProxy;
import com.zhaizhijun.blackjack.core.Tables;

/**
 * User: zjzhai
 * Date: 2/7/14
 */
public abstract class PlayerAction {

    private static String name;

    protected PlayerAction(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public abstract void invoke(PlayerProxy playerProxy, Tables tables, Object... args);


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerAction)) return false;

        PlayerAction that = (PlayerAction) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
