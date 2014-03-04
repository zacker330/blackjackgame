package com.zhaizhijun.blackjack.core;

import com.zhaizhijun.blackjack.NotEnoughMoneyException;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * User: zjzhai
 * Date: 2/21/14
 */
public class Bank {

    private Map<String, BigDecimal> data = new HashMap<String, BigDecimal>();

    /**
     * 存款
     *
     * @param player
     * @param amount
     */
    protected void deposit(String player, BigDecimal amount) {
        assert player != null && amount != null && amount.doubleValue() > 0;

        if (isPlayerInBank(player)) {
            data.put(player, data.get(player).add(amount));
        } else {
            data.put(player, amount);
        }
    }

    protected BigDecimal query(String player) {
        assert player != null;
        if (isPlayerInBank(player)) {
            return data.get(player);
        }

        return BigDecimal.ZERO;

    }


    protected boolean isPlayerInBank(String player) {
        assert player != null;
        return data.containsKey(player);
    }

    /**
     * TODO 需要持久化操作
     *
     * @param player
     * @param amount
     * @throws NotEnoughMoneyException 没有足够的钱取
     */
    protected synchronized void withdraw(String player, BigDecimal amount) {
        assert player != null && isPlayerInBank(player) && amount != null && amount.doubleValue() > 0;

        if (!hadMoney(player, amount)) {
            throw new NotEnoughMoneyException();
        }

        data.put(player, data.get(player).subtract(amount));
    }

    protected synchronized boolean hadMoney(String player, BigDecimal amount) {
        return data.get(player).compareTo(amount) >= 0;
    }

    @Override
    public String toString() {
        return "Bank{" +
                "data=" + data +
                '}';
    }

    /**
     * 转钱
     *
     * @param from
     * @param to
     * @param amount
     * @throws NotEnoughMoneyException 没有足够的钱转
     */
    public synchronized void transferMoney(String from, String to, BigDecimal amount) {
        assert from != null && to != null && amount != null && amount.doubleValue() > 0;

        if (!hadMoney(from, amount)) {
            throw new NotEnoughMoneyException();
        }
        withdraw(from, amount);
        deposit(to, amount);
    }


}
