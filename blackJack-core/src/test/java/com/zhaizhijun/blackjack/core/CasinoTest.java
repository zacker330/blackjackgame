package com.zhaizhijun.blackjack.core;

import com.zhaizhijun.blackjack.core.playerAction.DoubleDownAction;
import com.zhaizhijun.blackjack.core.playerAction.HitAction;
import com.zhaizhijun.blackjack.core.playerAction.ReportAction;
import com.zhaizhijun.blackjack.core.playerAction.SplitAction;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.Arrays;

/**
 * User: zjzhai
 * Date: 2/20/14
 */
public class CasinoTest {

    private final static Logger LOGGER = LoggerFactory.getLogger(CasinoTest.class);

    @Test
    public void testAll() throws Exception {


        //初始化赌场
        // 400块张赌桌
        Casino.initTablesPool(400);

        assert Casino.allTables().size() == 400;

        assert JoinMapping.allTablesId().size() == 400;

        String jia = "甲";
        String yi = "乙";
        String bing = "丙";

        //玩家在赌场中注册
        Casino.signUp(jia, yi, bing);

        assert Casino.isSignUpped(jia);
        assert Casino.isSignUpped(yi);
        assert Casino.isSignUpped(bing);

        // 玩家将钱存入赌场
        Casino.deposit(jia, new BigDecimal(400));
        assert Casino.queryBank(jia).intValue() == 400;

        Casino.deposit(yi, new BigDecimal(400));
        assert Casino.queryBank(yi).intValue() == 400;
        assert !Casino.isEnoughMoneyToBet(yi, new BigDecimal(2000));

        Casino.deposit(bing, new BigDecimal(400));
        assert Casino.queryBank(bing).intValue() == 400;
        assert Casino.isEnoughMoneyToBet(bing, new BigDecimal(200));

        LOGGER.info("三名玩家分别存入赌场");
        for (String each : Arrays.asList(jia, yi, bing)) {
            LOGGER.info("{}存入：{}", each, Casino.queryBank(each).doubleValue());
        }


        int tableId = 1;

        //玩家加入赌桌 1
        Casino.joinTable(tableId, jia);
        Casino.joinTable(tableId, yi, bing);


        LOGGER.warn("三名玩家加入赌桌:" + tableId);


        // 赌桌选举庄家
        Tables elected = Casino.dealerElect(tableId);

        LOGGER.info("选举出庄家为：{}", elected.getDealerPlayer());

        // 投注
        for (PlayerProxy eachProxy : elected.getAllPlayerProxies()) {
            Casino.bet(eachProxy.getPlayer(), new BigDecimal(20));
            LOGGER.info("玩家{}下注：{}", eachProxy.getPlayer(), 20);
        }


        assert !TablesPool.findTablesById(tableId).isOpening();

        //开局
        Casino.openTable(tableId);

        // 正在开局
        assert TablesPool.findTablesById(tableId).isOpening();


        Tables tables = Casino.getTables(tableId);

        LOGGER.info("*****开始******");
        PlayerProxy playerProxy = tables.getAllPlayerProxies().get(0);
        LOGGER.info("{}手上的牌，点数：[{}] 分别为：{}", playerProxy.getPlayer(), playerProxy.getSumPoint(), playerProxy.getHeadCard().getCards());
        PlayerProxy playerProxy1 = tables.getAllPlayerProxies().get(1);
        LOGGER.info("{}手上的牌，点数：[{}] 分别为：{}", playerProxy1.getPlayer(), playerProxy1.getSumPoint(), playerProxy1.getHeadCard().getCards());

        assert tableId == tables.getId();

        //模拟玩家玩牌
        for (PlayerProxy eachProxy : tables.getAllPlayerProxies()) {
            if (eachProxy.actionsContains(ReportAction.class)) {
                Casino.report(eachProxy.getId(), tableId);
                LOGGER.info("{} 报到，牌型为：{}", eachProxy.getPlayer(), eachProxy.getCardCategoryName());
                assert eachProxy.isStopped();
                continue;
            }

            if (eachProxy.actionsContains(DoubleDownAction.class)) {
                Casino.doubleDown(eachProxy.getId(), tableId);
                LOGGER.info("{} 双倍下注，最后点数：[{}]，牌为：{}", eachProxy.getPlayer(),
                        eachProxy.getSumPoint(),
                        eachProxy.getHeadCard().getCards());
                assert eachProxy.isStopped();
                continue;
            }

            if (eachProxy.isBust()) {
                continue;
            }

            if (eachProxy.actionsContains(SplitAction.class) || eachProxy.actionsContains(HitAction.class))
                if (eachProxy.getSumPoint() < 12) {
                    Casino.hit(eachProxy.getId(), tableId);
                    LOGGER.info("{}要牌，点数：{}，分别为：{}", eachProxy.getPlayer(),
                            eachProxy.getSumPoint(),
                            eachProxy.getHeadCard().getCards());
                    continue;
                } else if (eachProxy.getSumPoint() >= 12 || eachProxy.getSumPoint() <= 21) {
                    Casino.stand(eachProxy.getId(), tableId);
                    LOGGER.info("{}停牌，点数：{}，分别为：{}", eachProxy.getPlayer(),
                            eachProxy.getSumPoint(),
                            eachProxy.getHeadCard().getCards());

                    assert eachProxy.isStopped();
                    continue;
                }

        }

        for (PlayerProxy eachProxy : tables.getAllPlayerProxies()) {
            if (eachProxy.isStopped()) {
                continue;
            }

            Casino.stand(eachProxy.getId(), tableId);
            LOGGER.info("{}停牌，点数：{}，分别为：{}", eachProxy.getPlayer(),
                    eachProxy.getSumPoint(),
                    eachProxy.getHeadCard().getCards());
        }

        assert tables.isAllPlayerProxyStopped();

        if (tables.isDealerLe17Point()) {
            LOGGER.info("庄家{}点数为{}不足17点",
                    tables.getDealerProxy().getPlayer(),
                    tables.getDealerProxy().getSumPoint());

            tables.dealerHitUntilGe17Point();
            LOGGER.info("庄家要牌，最后点数：[{}] 牌为：{}", tables.getDealerProxy().getSumPoint(), tables.getDealerProxy().getHeadCard().getCards());
        } else {
            tables.getDealerProxy().stop();
            LOGGER.info("庄家点数:[{}]停牌最后牌为：{}", tables.getDealerProxy().getSumPoint()
                    , tables.getDealerProxy().getHeadCard().getCards());

        }
        assert tables.getDealerProxy().isStopped();

        LOGGER.info("**********所有玩家停牌，开始结算");

        Casino.settlement(tableId);

        LOGGER.info("{} 下注 {} 赢家为{} 最后余额：{}", playerProxy.getPlayer(),
                playerProxy.getBetMoney().doubleValue(),
                Casino.betCalculate(tableId, playerProxy).getWinner(),
                Casino.queryBank(playerProxy.getPlayer()).doubleValue());


        LOGGER.info("{} 下注 {} 赢家为{} 最后余额：{}", playerProxy1.getPlayer(),
                playerProxy1.getBetMoney().doubleValue(),
                Casino.betCalculate(tableId, playerProxy1).getWinner(),
                Casino.queryBank(playerProxy1.getPlayer()).doubleValue());

        LOGGER.info("庄家{} 最后余额:{}", tables.getDealerProxy().getPlayer(), Casino.queryBank(tables.getDealerPlayer()).doubleValue());

        LOGGER.warn("*******结束");

        Casino.close(tableId);

        assert !TablesPool.findTablesById(tableId).isOpening();


    }


}
