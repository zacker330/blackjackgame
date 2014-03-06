package com.zhaizhijun.blackjack.core;

import java.util.Set;

/**
 * 庄家选举策略
 * User: zjzhai
 * Date: 2/20/14
 */
public interface DealerElectPloy {

    ElectedResult elect(Set<String> players);
}