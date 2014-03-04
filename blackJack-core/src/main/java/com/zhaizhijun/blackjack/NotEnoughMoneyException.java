package com.zhaizhijun.blackjack;

/**
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
