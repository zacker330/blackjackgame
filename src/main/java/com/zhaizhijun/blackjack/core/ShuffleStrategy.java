package com.zhaizhijun.blackjack.core;

/**
 * 洗牌接口
 * User: zjzhai
 * Date: 1/23/14
 */
public interface ShuffleStrategy {

    Integer[] shuffle(int... numbers);

}
