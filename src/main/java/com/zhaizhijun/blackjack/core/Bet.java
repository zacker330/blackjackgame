package com.zhaizhijun.blackjack.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * 注，在游戏开局前，所有闲家都会下注
 * User: zjzhai
 * Date: 1/21/14
 */
public class Bet {

    /**
     * 可以多次下注
     */
    private List<BigDecimal> betAmounts = new ArrayList<BigDecimal>();


    /**
     * 下的所有的赌注
     */
    private BigDecimal betAmount = BigDecimal.ZERO;


    /**
     * 保险
     */
    private BigDecimal insurance;

    public Bet(BigDecimal betAmount) {
        this.betAmount = betAmount;
        betAmounts.add(betAmount);
    }


    public boolean isInsurance() {
        return insurance != null && (insurance.compareTo(BigDecimal.ZERO) > 0);
    }

    public BigDecimal addInsurance() {
        if (isInsurance()) {
            return insurance;
        }

        // TODO 买保险时加的倍数
        insurance = betAmount.divide(new BigDecimal(2), 2, RoundingMode.CEILING);
        return insurance;
    }


    public void add(BigDecimal amount) {
        betAmount.add(amount);
    }

    public BigDecimal getBetAmount() {
        return betAmounts.get(0);
    }

    public BigDecimal getInsuranceAmount() {
        return insurance;
    }

    public BigDecimal getBetSum() {
        BigDecimal result = BigDecimal.ZERO;

        for (BigDecimal each : betAmounts) {
            result = result.add(each);
        }

        return result;
    }

    /**
     * 可能的保险金额
     *
     * @return
     */
    public BigDecimal getImposibleInsuranceAmount() {
        return betAmount.divide(new BigDecimal(2), BigDecimal.ROUND_CEILING);
    }

    public List<BigDecimal> getBetAmounts() {
        return betAmounts;
    }

    public void doubleDown() {
        betAmounts.add(betAmounts.get(0));
    }

    @Override
    public String toString() {
        return "Bet{" +
                "betAmount=" + betAmount +
                '}';
    }


}
