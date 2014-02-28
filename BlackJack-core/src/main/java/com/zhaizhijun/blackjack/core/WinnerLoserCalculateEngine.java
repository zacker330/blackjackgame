package com.zhaizhijun.blackjack.core;

import com.zhaizhijun.blackjack.BlackjackException;
import com.zhaizhijun.blackjack.core.playerAction.*;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * 输赢计算引擎
 * User: zjzhai
 * Date: 2/8/14
 * Time: 10:00 AM
 */
public interface WinnerLoserCalculateEngine {

    /**
     * 计算下注的输赢
     *
     * @param playerProxy
     * @param dealerProxy
     * @return
     * @throws BlackjackException 处理脚本时发生异常
     */
    public WinnerLoserCalculateResult betCalculate(PlayerProxy playerProxy, DealerProxy dealerProxy);

    /**
     * 计算买保险的输赢
     *
     * @param playerProxy
     * @param dealerProxy
     * @return
     * @throws BlackjackException 处理脚本时发生异常
     */
    public WinnerLoserCalculateResult insuranceCalculate(PlayerProxy playerProxy, DealerProxy dealerProxy);


}
