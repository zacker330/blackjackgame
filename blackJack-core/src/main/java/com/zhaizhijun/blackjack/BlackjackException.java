package com.zhaizhijun.blackjack;

/**
 * User: zjzhai
 * Date: 2/22/14
 */
public class BlackjackException extends RuntimeException {

    public BlackjackException() {
    }

    public BlackjackException(String message, Exception cause) {
        super(message, cause);
    }

    public BlackjackException(String message) {
        super(message);
    }
}
