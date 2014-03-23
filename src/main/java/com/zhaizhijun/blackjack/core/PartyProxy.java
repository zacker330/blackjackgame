package com.zhaizhijun.blackjack.core;

import com.zhaizhijun.blackjack.core.card.Card;
import com.zhaizhijun.blackjack.core.card.HeadCard;
import com.zhaizhijun.blackjack.core.card.PlayerAction;

import java.util.List;

/**
 * 当事人代理
 * User: zjzhai
 * Date: 1/30/14
 */
public abstract class PartyProxy {

    private HeadCard headCard = new HeadCard();

    private final String id;

    /**
     * 是否已经停牌
     */
    private boolean stop = false;

    private String player;

    protected PartyProxy(String player) {
        this.player = player;
        // TODO 目前将ID的生成策略写死
        this.id = _.randomString(10);
    }

    /**
     * @return
     */
    protected PartyProxy addCard(Card aCard) {
        headCard.addCard(aCard);
        flushCardCategory();
        return this;
    }

    public String getId() {
        return this.id;
    }

    public String getCardCategoryName() {
        return headCard.getCardCategory().getName();
    }

    public HeadCard getHeadCard() {
        return headCard;
    }

    public int getSumPoint() {
        return headCard.getSumPoint();
    }

    protected String getPlayer() {
        return player;
    }

    public boolean isBlackJack() {
        return headCard.isBlackjack();
    }

    public void stop() {
        stop = true;
    }

    public boolean isStopped() {
        return stop;
    }

    public void flushCardCategory() {
        getHeadCard().flushCardCategory();
    }

    public List<PlayerAction> getActions() {
        return getHeadCard().getCardCategory().getActions();
    }

    public boolean isBust() {
        return getSumPoint() > 21;
    }

    @Override
    public String toString() {
        return "PartyProxy{" +
                "headCard=" + headCard +
                ", id='" + id + '\'' +
                ", stop=" + stop +
                ", player=" + player +
                '}';
    }
}
