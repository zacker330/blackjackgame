package com.zhaizhijun.blackjack.core;

import java.util.*;

/**
 * 使用JDK内部实现的洗牌算法
 * User: zjzhai
 * Date: 1/23/14
 */
public class SimpleShuffle implements ShuffleStrategy {

    @Override
    public Integer[] shuffle(int... numbers) {
        List<Integer> tem = new ArrayList<Integer>();
        for (Integer i : numbers) {
            tem.add(i);
        }
        Collections.shuffle(tem, new Random(new Random().nextLong()));
        return tem.toArray(new Integer[numbers.length]);
    }


}
