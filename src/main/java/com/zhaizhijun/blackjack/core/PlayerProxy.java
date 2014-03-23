package com.zhaizhijun.blackjack.core;

import com.zhaizhijun.blackjack.core.card.PlayerAction;
import com.zhaizhijun.blackjack.core.playerAction.SurrenderAction;

import java.math.BigDecimal;

/**
 * 玩家代理，你可以理解成“一手牌”的抽象。一个玩家是可以同时操作两手牌的。
 *
 * User: zjzhai
 * Date: 1/30/14
 */
public class PlayerProxy extends PartyProxy {

    private Bet bet;

    private PlayerAction latestAction;


    protected PlayerProxy(String player) {
        super(player);
    }

    protected PlayerProxy(String player, BigDecimal amountOfBet) {
        super(player);
        bet = new Bet(amountOfBet);
    }

    /**
     * 买保险
     */
    protected void buyInsurance() {
        bet.addInsurance();
    }


    /**
     * 是否已经买保险
     *
     * @return
     */
    public boolean isInsurance() {
        return bet.isInsurance();
    }


    public BigDecimal getBetMoney() {
        return bet.getBetSum();
    }


    public String getLatestActionName() {
        assert latestAction != null;
        return latestAction.getName();
    }

    public void setLatestAction(PlayerAction latestAction) {
        this.latestAction = latestAction;
    }

    public Bet getBet() {
        return bet;
    }

    public void doubleDown() {
        bet.doubleDown();
    }


    public void surrender() {
        latestAction = new SurrenderAction();
        stop();
    }


    public BigDecimal getInsuranceAmount() {
        return bet.getInsuranceAmount();
    }


    public boolean actionsContains(PlayerAction playerAction) {
        return getActions().contains(playerAction);
    }

    public boolean actionsContains(Class clasz) {
        for (PlayerAction action : getActions()) {
            if (action.getClass().equals(clasz)) {
                return true;
            }
        }
        return false;
    }

    public PlayerProxy split() {
        if (getHeadCard().isSplit()) {
            // TODO 抛异常
            return null;
        }
        PlayerProxy result = new PlayerProxy(getPlayer(), getBetMoney());
        result.getHeadCard().addCard(getHeadCard().split());
        result.getHeadCard().setSplit(true);
        return result;
    }


    public boolean isSplit() {
        return getHeadCard().isSplit();
    }

    public void bet(BigDecimal amount) {
        if (bet != null) {
            return;
        }
        this.bet = new Bet(amount);
    }


    @Override
    public String toString() {
        return "PlayerProxy{" +
                "bet=" + bet +
                "id=" + getId() +
                '}';
    }

    public static PlayerProxy createPlayerProxy(String player) {
        return new PlayerProxy(player);
    }
}
