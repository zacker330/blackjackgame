package com.zhaizhijun.blackjack.core;

import java.math.BigDecimal;

/**
 * 输赢计算后的结果
 * User: zjzhai
 * Date: 2/8/14
 */
public class WinnerLoserCalculateResult {
    private Winner winner;

    private BigDecimal winAmount;


    public WinnerLoserCalculateResult(Winner winner, BigDecimal winAmount) {
        this.winAmount = winAmount;
        this.winner = winner;
    }

    public boolean isPlayerWin() {
        return Winner.PLAYER.equals(winner);
    }

    public boolean isPush() {
        return Winner.PUSH.equals(winner);
    }

    public boolean isDealerWin() {
        return Winner.DEALER.equals(winner);
    }

    public Winner getWinner() {
        return this.winner;
    }

    public BigDecimal getWinAmount() {
        if (Winner.DEALER.equals(winner)) {
            return BigDecimal.ZERO;
        }
        return winAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof WinnerLoserCalculateResult)) return false;

        WinnerLoserCalculateResult that = (WinnerLoserCalculateResult) o;

        if (winAmount != null ? !winAmount.equals(that.winAmount) : that.winAmount != null) return false;
        if (winner != that.winner) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = winner != null ? winner.hashCode() : 0;
        result = 31 * result + (winAmount != null ? winAmount.hashCode() : 0);
        return result;
    }
}
