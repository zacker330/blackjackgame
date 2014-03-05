package com.zhaizhijun.blackjack.core;

import com.zhaizhijun.blackjack.NotEnoughMoneyException;
import com.zhaizhijun.blackjack.NotFoundTablesException;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 赌场
 * User: zjzhai
 * Date: 2/20/14
 */
public class Casino {

    /**
     * 所有玩家
     */
    private static Set<String> playerSet = new HashSet<String>();

    private static Bank bank = new Bank();

    private static DealerElectPloy dealerElectPloy = new RandomOneOfPlayerDealerElectPloy();


    private Casino() {
    }

    public synchronized static void signUp(String... players) {
        assert players != null;

        for (String each : players) {
            if (isSignUpped(each)) {
                continue;
            }
            playerSet.add(each);
        }
    }

    public static List<Tables> getAllTables() {
        return TablesPool.allTables();
    }


    public static boolean isSignUpped(String player) {
        return playerSet.contains(player);
    }

    public static void deposit(String player, BigDecimal amount) {
        assert player != null && amount != null && amount.doubleValue() > 0;
        bank.deposit(player, amount);
    }

    public static Tables getTables(int tableId) {
        return TablesPool.findTablesById(tableId);
    }

    /**
     * 下注
     *
     * @param player
     * @param amount
     * @throws com.zhaizhijun.blackjack.NotEnoughMoneyException
     *          没有足够的钱下注
     */
    public synchronized static void bet(String player, BigDecimal amount) {

        if (!isEnoughMoneyToBet(player, amount)) throw new NotEnoughMoneyException();

        Tables tables = TablesPool.findTablesByPlayer(player);

        for (PlayerProxy each : tables.getAllPlayerProxies()) {
            if (each.getPlayer().equals(player)) {
                each.bet(amount);
            }
        }

    }

    public static boolean isEnoughMoneyToBet(String player, BigDecimal bet) {
        return bank.hadMoney(player, bet);
    }


    /**
     * 设置为从所有玩家随机选一位玩家为庄家
     *
     * @return
     */
    public Casino randomOneOfPlayerDealerElectPloy() {
        this.dealerElectPloy = new RandomOneOfPlayerDealerElectPloy();
        return this;
    }

    public static void joinTable(int tableId, String... players) {
        assert players != null;
        for (String player : players) {
            assert playerSet.contains(player);
            TablesPool.join(tableId, player);
        }
    }

    public static BigDecimal playerBalance(String player) {
        assert player != null;
        return bank.balance(player);
    }


    /**
     * 初始化赌桌的数量
     *
     * @param tableCount
     */
    public static void initTablesPool(int tableCount) {
        assert tableCount > 0;

        TablesPool.init(tableCount);

        Collection<Integer> ids = TablesPool.allTablesId();
        JoinMapping.initSomeIds(ids.toArray(new Integer[ids.size()]));
    }

    public static List<Tables> allTables() {
        return TablesPool.allTables();
    }

    /**
     * 庄家选举：因为庄家可以是玩家之一，也可以是电脑，所以，需要用户确定使用哪一种策略
     *
     * @param tableId
     */
    public static Tables dealerElect(int tableId) {
        assert tableId > 0;

        Tables tables = getTables(tableId);

        whenTableNullThrowException(tables);

        ElectedResult electedResult = dealerElectPloy.elect(JoinMapping.whoAtTables(tableId));

        tables.setDealerProxy(electedResult.getDealerProxy());

        for (PlayerProxy each : electedResult.getPlayerProxies()) {
            tables.addPlayerProxy(each);
        }

        return tables;
    }

    /**
     * 报到
     *
     * @param playerProxyId
     */
    public static void report(String playerProxyId, int tableId) {
        assert tableId > 0;

        Tables tables = getTables(tableId);

        whenTableNullThrowException(tables);

        tables.report(playerProxyId);

    }

    /**
     * 双倍
     *
     * @param playerProxyId
     * @param tableId
     */
    public static void doubleDown(String playerProxyId, int tableId) {
        assert tableId > 0 && !_.isEmpty(playerProxyId);

        Tables tables = getTables(tableId);

        whenTableNullThrowException(tables);

        PlayerProxy playerProxy = tables.findPlayerProxyById(playerProxyId);

        if (!isDoubleDownAbility(playerProxy.getPlayer(), playerProxy.getBetMoney()))
            throw new NotEnoughMoneyException("没有足够的金钱双倍加注");

        tables.doubleDown(playerProxyId);


    }

    public static boolean isDoubleDownAbility(String player, BigDecimal betAmount) {
        return bank.balance(player).subtract(betAmount.multiply(new BigDecimal(2))).doubleValue() >= 0;
    }

    public static void openTable(int tableId) {
        assert tableId > 0;

        Tables tables = getTables(tableId);

        whenTableNullThrowException(tables);

        tables.opening();

    }

    public static void stand(String playerProxyId, int tableId) {
        assert tableId > 0 && !_.isEmpty(playerProxyId);

        Tables tables = getTables(tableId);

        whenTableNullThrowException(tables);

        tables.stand(playerProxyId);

    }

    public static void hit(String playerProxyId, int tableId) {

        assert tableId > 0 && !_.isEmpty(playerProxyId);

        Tables tables = getTables(tableId);

        whenTableNullThrowException(tables);

        tables.hit(playerProxyId);
    }

    /**
     * 计算保险的输赢
     *
     * @param tableId
     * @param playerProxy
     * @return
     */
    public static WinnerLoserCalculateResult insuranceCalculate(int tableId, PlayerProxy playerProxy) {
        assert tableId > 0;

        Tables tables = getTables(tableId);

        whenTableNullThrowException(tables);

        return tables.insuranceWinnerLoserCalculate(playerProxy);
    }

    /**
     * 计算下注的输赢
     *
     * @param tableId
     * @param playerProxy
     * @return
     */
    public static WinnerLoserCalculateResult betCalculate(int tableId, PlayerProxy playerProxy) {
        assert tableId > 0;

        Tables tables = getTables(tableId);

        whenTableNullThrowException(tables);

        return tables.betWinnerLoserCalculate(playerProxy);
    }


    /**
     * 结算
     *
     * @param tableId
     */
    public static void settlement(int tableId) {

        assert tableId > 0;

        Tables tables = getTables(tableId);

        whenTableNullThrowException(tables);

        for (PlayerProxy proxy : tables.getAllPlayerProxies()) {
            // 是否有买保险
            if (proxy.isInsurance()) {
                WinnerLoserCalculateResult insuranceResult = tables.insuranceWinnerLoserCalculate(proxy);
                insuranceSettlement(insuranceResult, proxy, tables);
            }
            WinnerLoserCalculateResult betResult = tables.betWinnerLoserCalculate(proxy);
            betSettlement(betResult, proxy, tables);
        }
    }

    private static void betSettlement(WinnerLoserCalculateResult betResult, PlayerProxy playerProxy, Tables tables) {
        switch (betResult.getWinner()) {
            case PLAYER:
                bank.transferMoney(tables.getDealerPlayer(), playerProxy.getPlayer(), betResult.getWinAmount());
                break;
            case DEALER:
                bank.transferMoney(playerProxy.getPlayer(), tables.getDealerPlayer(), playerProxy.getBetMoney());
                break;
            case PUSH:
                break;
        }

    }

    private static void insuranceSettlement(WinnerLoserCalculateResult winnerLoserCalculateResult,
                                            PlayerProxy playerProxy, Tables tables) {
        switch (winnerLoserCalculateResult.getWinner()) {
            case PLAYER:
                bank.transferMoney(tables.getDealerPlayer(), playerProxy.getPlayer(), winnerLoserCalculateResult.getWinAmount());
                break;
            case DEALER:
                bank.transferMoney(playerProxy.getPlayer(), tables.getDealerPlayer(), playerProxy.getInsuranceAmount());
                break;
            case PUSH:
                break;
        }
    }


    private static void whenTableNullThrowException(Tables tables) {
        if (tables == null) throw new NotFoundTablesException();
    }

    public static void close(int tableId) {
        assert tableId > 0;

        Tables tables = getTables(tableId);

        whenTableNullThrowException(tables);

        tables.close();
    }

    public static void setBankRepository(BankRepository bankRepository) {
        bank.setBankRepository(bankRepository);
    }
}
