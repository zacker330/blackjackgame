package com.zhaizhijun.blackjack.core.card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 牌型，指手上的牌的排列组合，比如同花顺就是一种牌型
 * User: zjzhai
 * Date: 2/7/14
 */
public class CardCategory {

    private final String name;

    private List<PlayerAction> actions = new ArrayList<PlayerAction>();

    public CardCategory(String name, List<PlayerAction> actions) {
        this.name = name;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public List<PlayerAction> getActions() {
        return Collections.unmodifiableList(actions);
    }

    @Override
    public String toString() {
        return "CardCategory{" +
                "name='" + name + '\'' +
                ", actions=" + actions +
                '}';
    }
}
