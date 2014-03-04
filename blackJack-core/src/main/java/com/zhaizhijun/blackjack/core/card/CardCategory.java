package com.zhaizhijun.blackjack.core.card;

import com.zhaizhijun.blackjack.core.playerAction.AbstractPlayerAction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * User: zjzhai
 * Date: 2/7/14
 */
public class CardCategory {

    private final String name;

    private List<AbstractPlayerAction> actions = new ArrayList<AbstractPlayerAction>();

    public CardCategory(String name, List<AbstractPlayerAction> actions) {
        this.name = name;
        this.actions = actions;
    }

    public String getName() {
        return name;
    }

    public List<AbstractPlayerAction> getActions() {
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
