package com.zhaizhijun.blackjack.core;

import com.zhaizhijun.blackjack.BlackjackException;
import com.zhaizhijun.blackjack.core.playerAction.*;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;

/**
 * 默认实现
 * User: zjzhai
 * Date: 2/27/14
 */
public class GroovyWinnerLoserCalculateEngine implements WinnerLoserCalculateEngine {
    /**
     * 计算下注的输赢
     *
     * @param playerProxy
     * @param dealerProxy
     * @return
     * @throws com.zhaizhijun.blackjack.BlackjackException
     *          处理脚本时发生异常
     */
    public WinnerLoserCalculateResult betCalculate(PlayerProxy playerProxy, DealerProxy dealerProxy) {
        GroovyShell shell = new GroovyShell(createDefaultBinding(playerProxy, dealerProxy));

        try {
            shell.evaluate(new File(WinnerLoserCalculateEngine.class.getResource("/Settlement.groovy").getFile()));
            Winner winner = (Winner) shell.getVariable("winner");
            BigDecimal winAmount = BigDecimal.ZERO;
            if (shell.getVariable("winMoney") != null) {
                winAmount = new BigDecimal(shell.getVariable("winMoney").toString());
            }
            return new WinnerLoserCalculateResult(winner, winAmount);
        } catch (IOException e) {
            throw new BlackjackException("处理Settlement.groovy时脚本异常", e);
        }
    }

    /**
     * 计算买保险的输赢
     *
     * @param playerProxy
     * @param dealerProxy
     * @return
     * @throws BlackjackException 处理脚本时发生异常
     */
    public WinnerLoserCalculateResult insuranceCalculate(PlayerProxy playerProxy, DealerProxy dealerProxy) {
        GroovyShell shell = new GroovyShell(createDefaultBinding(playerProxy, dealerProxy));

        try {
            shell.evaluate(new File(WinnerLoserCalculateEngine.class.getResource("/Insurance.groovy").getFile()));
            BigDecimal winAmount = BigDecimal.ZERO;
            if (shell.getVariable("winMoney") != null) {
                winAmount = new BigDecimal(shell.getVariable("winMoney").toString());
            }
            return new WinnerLoserCalculateResult(dealerProxy.isBlackJack() ? Winner.DEALER : Winner.PLAYER, winAmount);
        } catch (IOException e) {
            throw new BlackjackException("处理Insurance.groovy时脚本异常", e);
        }
    }


    private Binding createDefaultBinding(PlayerProxy playerProxy, DealerProxy dealerProxy) {
        Binding binding = new Binding();
        binding.setVariable("PLAYER", Winner.PLAYER);
        binding.setVariable("DEALER", Winner.DEALER);
        binding.setVariable("PUSH", Winner.PUSH);

        injectPlayerActionTo(binding);

        binding.setVariable("playerBetAmounts", playerProxy.getBet().getBetAmounts());
        binding.setVariable("playerFirstBet", playerProxy.getBet().getBetAmounts().get(0));
        binding.setVariable("playerBetSum", playerProxy.getBet().getBetSum());
        binding.setVariable("playerLatestAction", playerProxy.getLatestActionName());
        binding.setVariable("isPlayerBlackjack", playerProxy.isBlackJack());
        binding.setVariable("playerCardCategory", playerProxy.getCardCategoryName());
        binding.setVariable("playerSumPoint", playerProxy.getSumPoint());
        binding.setVariable("playerCardCount", playerProxy.getHeadCard().getCardCount());

        binding.setVariable("dealerCardCategory", dealerProxy.getCardCategoryName());
        binding.setVariable("dealerSumPoint", dealerProxy.getSumPoint());
        binding.setVariable("dealerCardCount", dealerProxy.getHeadCard().getCardCount());
        binding.setVariable("isDealerBlackjack", dealerProxy.isBlackJack());

        return binding;
    }

    private void injectPlayerActionTo(Binding binding) {
        binding.setVariable(StandAction._name, StandAction._name);
        binding.setVariable(HitAction._name, HitAction._name);
        binding.setVariable(ReportAction._name, ReportAction._name);
        binding.setVariable(DoubleDownAction._name, DoubleDownAction._name);
        binding.setVariable(SplitAction._name, SplitAction._name);
        binding.setVariable(StopAction._name, StopAction._name);
        binding.setVariable(SurrenderAction._name, SurrenderAction._name);
    }

}
