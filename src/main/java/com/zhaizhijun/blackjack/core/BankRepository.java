package com.zhaizhijun.blackjack.core;

import java.math.BigDecimal;

/**
 * User: zjzhai
 * Date: 3/5/14
 * Time: 11:45 AM
 */
public interface BankRepository {
    void deposit(String player, BigDecimal amount);

    BigDecimal balance(String player);

    void withdraw(String player, BigDecimal amount);

    void transferMoney(String fromPlayer, String toPlayer, BigDecimal amount);
}
