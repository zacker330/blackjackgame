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

    private BankRepository bankRepository;


    public void setBankRepository(BankRepository bankRepository) {
        if (null != this.bankRepository) {
            return;
        }
        this.bankRepository = bankRepository;
    }

    /**
     * 存款
     *
     * @param player
     * @param amount
     */
    protected void deposit(String player, BigDecimal amount) {
        assert player != null && amount != null && amount.doubleValue() > 0;
        bankRepository.deposit(player, amount);
    }

    protected BigDecimal balance(String player) {
        assert player != null;
        return bankRepository.balance(player);


    }


    /**
     * TODO 需要持久化操作
     *
     * @param player
     * @param amount
     * @throws NotEnoughMoneyException 没有足够的钱取
     */
    protected synchronized void withdraw(String player, BigDecimal amount) {
        assert player != null && amount != null && amount.doubleValue() > 0;
        bankRepository.withdraw(player, amount);
    }

    protected synchronized boolean hadMoney(String player, BigDecimal amount) {
        return bankRepository.balance(player).compareTo(amount) >= 0;
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
        bankRepository.transferMoney(from, to, amount);
    }

    @Override
    public String toString() {
        return "Bank{" +
                "bankRepository=" + bankRepository +
                '}';
    }
}
