package com.zhaizhijun.blackjack.core;


/**
 * User: zjzhai
 * Date: 1/30/14
 */
public class DealerProxy extends PartyProxy {


    /**
     * 庄家最小的值
     */
    public static final int BANKER_MIN_POINT = 17;


    protected DealerProxy(String player) {
        super(player);
    }

    public boolean beInsurancable() {
        return getHeadCard().getFirstSeenCard().isAce();
    }

    public boolean sumPointIs17Point() {
        return getSumPoint() < BANKER_MIN_POINT;
    }


    public static DealerProxy createDealerProxy(String partyName) {
        return new DealerProxy(partyName);
    }
}
