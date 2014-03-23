package com.zhaizhijun.blackjack.core;


import com.zhaizhijun.blackjack.BlackjackException;
import com.zhaizhijun.blackjack.NotFoundPlayerProxy;
import com.zhaizhijun.blackjack.core.card.*;
import com.zhaizhijun.blackjack.core.playerAction.*;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 牌桌
 * User: zjzhai
 * Date: 1/21/14
 */
public class Tables implements Comparable<Tables> {

    private static final Logger LOGGER = LoggerFactory.getLogger(Tables.class);

    /**
     * 是否正在进行
     */
    private boolean opening = false;


    /**
     * 发牌员
     */
    private DealerRobot dealerRobot;


    /**
     * 庄家
     */
    private DealerProxy dealerProxy;

    private List<PlayerProxy> playerProxies = new ArrayList<PlayerProxy>();

    private Integer id;

    private WinnerLoserCalculateEngine winnerLoserCalculateEngine = new GroovyWinnerLoserCalculateEngine();

    private CardCategoryCalculateEngine cardCategoryCalculateEngine = new GroovyCardCategoryCalculateEngine();

    /**
     * 延迟到发牌时,根据玩家人数，再决定要用几副牌
     */
    private Tables() {
    }

    public Tables(int deckCount, Integer id) {
        dealerRobot = new DealerRobot(deckCount);
        this.id = id;
    }

    public Tables(DealerRobot dealerRobot, Integer id) {
        assert dealerRobot != null;
        this.dealerRobot = dealerRobot;
        this.id = id;
    }

    public void setWinnerLoserCalculateEngine(WinnerLoserCalculateEngine winnerLoserCalculateEngine) {
        assert winnerLoserCalculateEngine != null;
        this.winnerLoserCalculateEngine = winnerLoserCalculateEngine;
    }

    public void setDealerProxy(DealerProxy dealerProxy) {
        this.dealerProxy = dealerProxy;
    }


    private boolean isThePartyProxyTheTables(PartyProxy partyProxy) {
        assert partyProxy != null;

        for (PlayerProxy each : playerProxies) {
            if (partyProxy.equals(each)) {
                return true;
            }
        }
        return false;
    }

    public Tables addPlayerProxy(PlayerProxy playerProxy) {
        assert playerProxy != null;
        playerProxies.add(playerProxy);
        return this;
    }

    public List<PlayerProxy> getAllPlayerProxies() {
        return Collections.unmodifiableList(playerProxies);
    }

    /**
     * 发一张明牌，一张暗牌
     */
    public void opening() {
        assert playerProxies.size() >= 1;

        opening = true;

        dealerProxy.addCard(dealerRobot.deal()).addCard(dealerRobot.deal());

        for (PlayerProxy each : playerProxies) {
            each.addCard(dealerRobot.deal()).addCard(dealerRobot.deal());
        }


    }

    /**
     * @param playerAction
     * @param playerProxy
     * @return
     * @throws BlackjackException 玩家非法操作
     */
    public Tables invokeAction(PlayerAction playerAction, PlayerProxy playerProxy) {
        if (!isThePartyProxyTheTables(playerProxy)) {
            LOGGER.error("非法操作，该玩家代理不能进行此操作");
            throw new BlackjackException("There is not the player " + playerAction + " at the table");
        }

        assert !playerProxy.isStopped();
        playerAction.invoke(playerProxy, this);
        playerProxy.flushCardCategory();
        playerProxy.setLatestAction(playerAction);
        return this;
    }



    /**
     * @param playerProxy
     * @return
     */
    public CardCategory hit(PlayerProxy playerProxy) {
        assert playerProxy.getHeadCard().getCardCount() >= 2 && playerProxy.actionsContains(new HitAction());
        invokeAction(new HitAction(), playerProxy);
        return playerProxy.getHeadCard().getCardCategory();
    }

    public void hit(String playerProxyId) {
        assert !_.isEmpty(playerProxyId);
        hit(findPlayerProxyById(playerProxyId));
    }


    public int playerSumPoint(PlayerProxy playerProxy) {
        return playerProxy.getSumPoint();
    }


    public void playerProxyBuyInsurance(PlayerProxy playerProxy) {
        playerProxy.buyInsurance();
    }

    public boolean isPlayerCanBuyInsurance() {
        return dealerProxy.beInsurancable();
    }

    public CardCategory calculateProxyCardCategory(HeadCard headCard) {
        return cardCategoryCalculateEngine.calculate(headCard);
    }

    /**
     * TODO 存在安全问题，因为外部可能在未开局时调用此接口
     *
     * @param partyProxy
     */
    public void deal(PartyProxy partyProxy) {
        Card card = dealerRobot.deal();
        partyProxy.addCard(card);
    }

    public DealerProxy getDealerProxy() {
        return dealerProxy;
    }

    /**
     * 计算保险的输赢
     *
     * @param playerProxy
     * @return
     */
    public WinnerLoserCalculateResult insuranceWinnerLoserCalculate(PlayerProxy playerProxy) {
        return winnerLoserCalculateEngine.insuranceCalculate(playerProxy, dealerProxy);
    }

    /**
     * 计算下注的输赢
     *
     * @param playerProxy
     * @return
     */
    public WinnerLoserCalculateResult betWinnerLoserCalculate(PlayerProxy playerProxy) {
        return winnerLoserCalculateEngine.betCalculate(playerProxy, dealerProxy);
    }

    public String getDealerPlayer() {
        return dealerProxy.getPlayer();
    }


    public boolean isAllPlayerProxyStopped() {
        for (PlayerProxy each : playerProxies) {
            if (!each.isStopped()) return false;
        }
        return true;
    }

    /**
     * 庄家点数少于17
     *
     * @return
     */
    public boolean isDealerLe17Point() {
        return dealerProxy.sumPointIs17Point();
    }

    /**
     * 庄家要牌，直到大于17点
     */
    public void dealerHitUntilGe17Point() {
        while (isDealerLe17Point()) {
            deal(dealerProxy);
        }
        dealerProxy.stop();
    }

    public void stand(PlayerProxy playerProxy) {
        assert playerProxy.actionsContains(StandAction.class);
        invokeAction(new StandAction(), playerProxy);
    }

    public void stand(String playerProxyId) {
        assert !_.isEmpty(playerProxyId);

        stand(findPlayerProxyById(playerProxyId));

    }

    public void doubleDown(PlayerProxy playerProxy) {
        assert playerProxy.actionsContains(DoubleDownAction.class);
        invokeAction(new DoubleDownAction(), playerProxy);
    }

    public void doubleDown(String playerProxyId) {
        assert !_.isEmpty(playerProxyId);

        doubleDown(findPlayerProxyById(playerProxyId));
    }

    public void split(PlayerProxy playerProxy) {
        assert playerProxy.actionsContains(SplitAction.class);
        invokeAction(new SplitAction(), playerProxy);
    }

    public void split(String playerProxyId) {
        assert !_.isEmpty(playerProxyId);
        split(findPlayerProxyById(playerProxyId));
    }

    public void report(PlayerProxy playerProxy) {
        assert playerProxy.actionsContains(ReportAction.class);
        invokeAction(new ReportAction(), playerProxy);
    }

    /**
     * @param playerProxyId
     * @return
     * @throws NotFoundPlayerProxy 根据ID找不到该玩家代理
     */
    public PlayerProxy findPlayerProxyById(String playerProxyId) {
        for (PlayerProxy each : playerProxies) {
            if (playerProxyId.equals(each.getId())) return each;

        }
        throw new NotFoundPlayerProxy();

    }

    public void report(String playerProxyId) {
        report(findPlayerProxyById(playerProxyId));
    }

    public Integer getId() {
        return id;
    }

    public boolean isOpening() {
        return opening;
    }

    @Override
    public String toString() {
        return "Tables{" +
                "dealerRobot=" + dealerRobot +
                ", dealerProxy=" + dealerProxy +
                ", playerProxies=" + playerProxies +
                ", id=" + id +
                '}';
    }

    @Override
    public int compareTo(Tables o) {
        if (o.id > id) {
            return 1;
        } else if (o.id < id) {
            return -1;
        }
        return 0;
    }


    protected void close() {
        opening = false;
    }

}
