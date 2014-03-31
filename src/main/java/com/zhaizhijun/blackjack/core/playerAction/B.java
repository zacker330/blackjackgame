package com.zhaizhijun.blackjack.core.playerAction;

/**
 * Created by zjzhai on 3/23/14.
 */
public class B {
    private static B ourInstance = new B();

    public static B getInstance() {
        return ourInstance;
    }

    private B() {
    }
}
