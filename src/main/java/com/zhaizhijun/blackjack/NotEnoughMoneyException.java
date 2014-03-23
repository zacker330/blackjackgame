package com.zhaizhijun.blackjack;

/**
 * 没有足够的金钱
 * User: zjzhai
 * Date: 2/23/14
 */
public class NotEnoughMoneyException extends BlackjackException {

    public NotEnoughMoneyException() {
    }

    public NotEnoughMoneyException(String message, Exception cause) {
        super(message, cause);
    }

    public NotEnoughMoneyException(String message) {
        super(message);
    }
}
