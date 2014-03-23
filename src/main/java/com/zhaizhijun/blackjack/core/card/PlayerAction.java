package com.zhaizhijun.blackjack.core.card;

import com.zhaizhijun.blackjack.core.PlayerProxy;
import com.zhaizhijun.blackjack.core.Tables;

/**
 * 21点游戏中，玩家所能进行的操作是有限的，只有：要牌(Hit),停牌(Stand),双倍下注(Double)，报到(Report),投降(Surrender)，分牌(Split)。
 * PlayerAction 是各个操作的基类
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
