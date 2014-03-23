package com.zhaizhijun.blackjack.core.card;

import com.zhaizhijun.blackjack.core.playerAction.HitAction;
import com.zhaizhijun.blackjack.core.playerAction.StandAction;

import java.util.Arrays;

/**
 * 常规牌型,不是任何的例牌，就为常规牌型
 * User: zjzhai
 * Date: 2/8/14
 */
public class GeneralCardCategory extends CardCategory {
    public final static String _name = "general";

    public GeneralCardCategory() {
        super(_name, Arrays.asList(new HitAction(), new StandAction()));
    }
}
