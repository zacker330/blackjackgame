package com.zhaizhijun.blackjack.core;

import com.zhaizhijun.blackjack.core.card.*;
import com.zhaizhijun.blackjack.core.playerAction.*;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.mockito.Mockito.*;

public class AppTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(Tables.class);

    private Integer tableId = 1;

    @Test
    public void testName() throws Exception {

        Player banker = new Player("A");

        Player playerB = new Player("B");
        Player playerC = new Player("C");

        int frenchDeckCount = 4;

        //将牌交给发牌员
        DealerRobot dealerRobot = mock(DealerRobot.class);


        // 开局
        Tables tables = new Tables(dealerRobot, tableId);

        DealerProxy dealerProxy = banker.createDealerProxy();

        // 设置庄家
        tables.setDealerProxy(dealerProxy);

        //设置闲家
        PlayerProxy playerProxyB = playerB.createPlayerProxy(new BigDecimal(10));

        tables.addPlayerProxy(playerProxyB);

        when(dealerRobot.deal()).thenReturn(Card.create(CardColor.CLUB, CardFace._7));

        tables.opening();

        // 是否可以买保险
        assert !tables.isPlayerCanBuyInsurance();

        // tables.playerProxyBuyInsurance(playerProxyB);


        CardCategory cc = tables.calculateProxyCardCategory(playerProxyB.getHeadCard());

        when(dealerRobot.deal()).thenReturn(Card.create(CardColor.CLUB, CardFace._4));
        tables.hit(playerProxyB);

        assert playerProxyB.getSumPoint() == 18;

        tables.stand(playerProxyB);

        assert playerProxyB.isStopped();

        assert tables.betWinnerLoserCalculate(playerProxyB).getWinner().equals(Winner.PLAYER);

        assert tables.betWinnerLoserCalculate(playerProxyB).getWinAmount().doubleValue() == 10;


        // assert dealerProxy.getPlayer().getMoney().intValue() == (1000 - 10);

        // assert playerProxyB.getPlayer().getMoney().intValue() == 100 + 10;

    }


    /**
     * 设置比点的情况
     *
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void test_dealer_player_comparePoint() throws NoSuchFieldException, IllegalAccessException {

        Player player = new Player("player");

        Player dealer = new Player("dealer");

        //发牌器
        DealerRobot dealerRobot = mock(DealerRobot.class);
        Tables tables = new Tables(dealerRobot, tableId);

        PlayerProxy playerProxy = player.createPlayerProxy(new BigDecimal(20));

        tables.addPlayerProxy(playerProxy);

        DealerProxy dealerProxy = dealer.createDealerProxy();
        tables.setDealerProxy(dealerProxy);

        when(dealerRobot.deal()).thenReturn(Card.create(CardColor.CLUB, CardFace._6));

        tables.opening();

        setPartProxyHeadCard(playerProxy, createMock13PointHeadCard());
        playerProxy.flushCardCategory();

        setPartProxyHeadCard(dealerProxy, createMock13PointHeadCard());
        dealerProxy.flushCardCategory();


        assert playerProxy.getActions().contains(new HitAction());

        tables.hit(playerProxy);

        assert playerProxy.getSumPoint() == 19;

        assert playerProxy.actionsContains(new StandAction());

        tables.stand(playerProxy);

        assert playerProxy.isStopped();

        assert tables.isAllPlayerProxyStopped();

        assert tables.isDealerLe17Point();

        when(dealerRobot.deal()).thenReturn(Card.create(CardColor.CLUB, CardFace._7));


        tables.dealerHitUntilGe17Point();

        assert dealerProxy.getSumPoint() == 20;

        assert tables.betWinnerLoserCalculate(playerProxy).getWinner().equals(Winner.DEALER);


        // assert dealer.getMoney().intValue() == 10000 + 20;

        // assert player.getMoney().intValue() == 200 - 20;


    }

    private void setPartProxyHeadCard(PartyProxy partyProxy, HeadCard headCard) throws NoSuchFieldException, IllegalAccessException {
        Field playerHeadCardField = PlayerProxy.class.getSuperclass().getDeclaredField("headCard");

        playerHeadCardField.setAccessible(true);

        playerHeadCardField.set(partyProxy, headCard);
    }


    /**
     * 设置买保险的情况
     *
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    @Test
    public void test_buy_insurance() throws NoSuchFieldException, IllegalAccessException {
        Player player = new Player("player");

        Player dealer = new Player("dealer");

        //发牌器
        DealerRobot dealerRobot = mock(DealerRobot.class);
        Tables tables = new Tables(dealerRobot, tableId);

        PlayerProxy playerProxy = player.createPlayerProxy(new BigDecimal(20));


        tables.addPlayerProxy(playerProxy);

        DealerProxy dealerProxy = dealer.createDealerProxy();
        tables.setDealerProxy(dealerProxy);

        when(dealerRobot.deal()).thenReturn(Card.create(CardColor.CLUB, CardFace._2));

        tables.opening();

        setPartProxyHeadCard(playerProxy, createMockHeadCard());
        playerProxy.flushCardCategory();

        setPartProxyHeadCard(dealerProxy, createMockBlackjackHeadCard());
        dealerProxy.flushCardCategory();

        assert tables.isPlayerCanBuyInsurance();

        //   assert playerProxy.isPlayerEnoughMoneyTobuyInsurance();

        tables.playerProxyBuyInsurance(playerProxy);

        tables.stand(playerProxy);

        WinnerLoserCalculateResult insuranceResult = tables.insuranceWinnerLoserCalculate(playerProxy);
        assert insuranceResult.getWinner().equals(Winner.DEALER);

        WinnerLoserCalculateResult betResult = tables.betWinnerLoserCalculate(playerProxy);

        assert betResult.getWinner().equals(Winner.DEALER);

        assert betResult.getWinAmount().doubleValue() == 0;


    }

    private HeadCard createMockBlackjackHeadCard() {
        HeadCard result = new HeadCard();
        result.addCard(Card.create(CardColor.CLUB, CardFace._J));
        result.addCard(Card.create(CardColor.DIAMOND, CardFace._A));
        return result;
    }

    private HeadCard createMockSplitHeadCard() {
        HeadCard result = new HeadCard();
        result.addCard(Card.create(CardColor.CLUB, CardFace._8));
        result.addCard(Card.create(CardColor.DIAMOND, CardFace._8));
        return result;
    }


    private HeadCard createMockDoubleDownHeadCard() {
        HeadCard result = new HeadCard();
        result.addCard(Card.create(CardColor.CLUB, CardFace._3));
        result.addCard(Card.create(CardColor.DIAMOND, CardFace._8));
        return result;
    }

    private HeadCard createMock13PointHeadCard() {
        HeadCard dealerHeadCard = new HeadCard();
        dealerHeadCard.addCard(Card.create(CardColor.CLUB, CardFace._10));
        dealerHeadCard.addCard(Card.create(CardColor.DIAMOND, CardFace._3));
        return dealerHeadCard;
    }

    private HeadCard createMockHeadCard() {
        HeadCard dealerHeadCard = new HeadCard();
        dealerHeadCard.addCard(Card.create(CardColor.CLUB, CardFace._10));
        dealerHeadCard.addCard(Card.create(CardColor.DIAMOND, CardFace._9));
        return dealerHeadCard;
    }


    /**
     * 设置分牌
     *
     * @throws Exception
     */
    @Test
    public void test_split() throws Exception {


        Player player = new Player("player");

        Player dealer = new Player("dealer");

        //发牌器
        DealerRobot dealerRobot = mock(DealerRobot.class);
        Tables tables = new Tables(dealerRobot, tableId);

        PlayerProxy playerProxy = player.createPlayerProxy(new BigDecimal(20));


        tables.addPlayerProxy(playerProxy);

        DealerProxy dealerProxy = dealer.createDealerProxy();
        tables.setDealerProxy(dealerProxy);

        when(dealerRobot.deal()).thenReturn(Card.create(CardColor.CLUB, CardFace._8));

        tables.opening();

        setPartProxyHeadCard(playerProxy, createMockSplitHeadCard());
        playerProxy.flushCardCategory();

        setPartProxyHeadCard(dealerProxy, createMockHeadCard());
        dealerProxy.flushCardCategory();

        assert playerProxy.getActions().contains(new SplitAction());

        /**
         * 分牌前玩家代理只有一门
         */
        assert tables.getAllPlayerProxies().size() == 1;


        tables.split(playerProxy);


        /**
         * 分牌后玩家代理多了一门
         */
        assert tables.getAllPlayerProxies().size() == 2;


        // 分牌后的两门
        PlayerProxy playerProxy0 = tables.getAllPlayerProxies().get(0);
        PlayerProxy playerProxy1 = tables.getAllPlayerProxies().get(1);

        /**
         * 分牌后两个玩家代理都没有变，筹码相同，
         */
        assert playerProxy0.getPlayer().equals(playerProxy1.getPlayer());

        assert playerProxy0.getBetMoney().doubleValue()
                == playerProxy1.getBetMoney().doubleValue();

        /**
         * 分牌后马上发牌
         */
        assert playerProxy0.getHeadCard().getCardCount() == 2 && playerProxy0.getSumPoint() == 16;
        assert playerProxy1.getHeadCard().getCardCount() == 2 && playerProxy1.getSumPoint() == 16;

        assert playerProxy0.isSplit() && playerProxy1.isSplit();

        /**
         * 因为在Special.groovy中设置了不能分牌后再分，所以，不再包含SplitAction
         */
        assert !playerProxy0.actionsContains(SplitAction.class);
        assert !playerProxy1.actionsContains(SplitAction.class);
        assert playerProxy0.getCardCategoryName().equals(GeneralCardCategory._name);
        assert playerProxy1.getCardCategoryName().equals(GeneralCardCategory._name);


    }

    /**
     * 全自动玩牌
     */
    @Test
    public void test_auto_play() {
        Player player = new Player("player");
        Player player1 = new Player("player1");

        Player dealer = new Player("dealer");

        //发牌器
        Tables tables = new Tables(4, tableId);

        PlayerProxy playerProxy = player.createPlayerProxy(new BigDecimal(20));
        PlayerProxy playerProxy1 = player1.createPlayerProxy(new BigDecimal(40));

        tables.addPlayerProxy(playerProxy);
        LOGGER.info("玩家 {} 下注一门，注大小为： {} ", playerProxy.getPlayerName(), playerProxy.getBetMoney().intValue());

        tables.addPlayerProxy(playerProxy1);
        LOGGER.info("玩家 {} 下注一门，注大小为： {} ", playerProxy1.getPlayerName(), playerProxy1.getBetMoney().intValue());


        DealerProxy dealerProxy = dealer.createDealerProxy();
        tables.setDealerProxy(dealerProxy);

        tables.opening();


        LOGGER.info("======= 赌局开始，并发牌 =========");
        LOGGER.info("{} 得到牌:{}", dealerProxy.getPlayerName(), dealerProxy.getHeadCard());
        LOGGER.info("{} 得到牌:{}", playerProxy.getPlayerName(), playerProxy.getHeadCard());
        LOGGER.info("{} 得到牌:{}", playerProxy1.getPlayerName(), playerProxy1.getHeadCard());


        if (playerProxy.getSumPoint() < 12) {
            tables.hit(playerProxy);
        }

        if (playerProxy.actionsContains(ReportAction.class)) {
            tables.report(playerProxy);
        }


    }


    /**
     * 双倍下注
     *
     * @throws Exception
     */
    @Test
    public void test_d() throws Exception {

        Player player = new Player("player");

        Player dealer = new Player("dealer");

        //发牌器
        Integer id = 1;
        Tables tables = new Tables(4, id);

        PlayerProxy playerProxy = player.createPlayerProxy(new BigDecimal(20));


        tables.addPlayerProxy(playerProxy);

        DealerProxy dealerProxy = dealer.createDealerProxy();
        tables.setDealerProxy(dealerProxy);

        tables.opening();

        setPartProxyHeadCard(playerProxy, createMockDoubleDownHeadCard());
        playerProxy.flushCardCategory();

        setPartProxyHeadCard(dealerProxy, createMockHeadCard());
        dealerProxy.flushCardCategory();

        assert playerProxy.getActions().contains(new DoubleDownAction());


        tables.doubleDown(playerProxy);

        assert playerProxy.getBet().getBetSum().intValue() == 40;

        assert playerProxy.isStopped();

        assert playerProxy.getHeadCard().getCardCount() == 3;

        WinnerLoserCalculateResult result = tables.betWinnerLoserCalculate(playerProxy);


        if (playerProxy.getSumPoint() > dealerProxy.getSumPoint()) {
            assert result.getWinner().equals(Winner.PLAYER);
        } else if (playerProxy.getSumPoint() == dealerProxy.getSumPoint()) {
            assert result.getWinner().equals(Winner.PUSH);

        } else {
            assert result.getWinner().equals(Winner.DEALER);
        }
    }


    /**
     * 测试过五龙
     */
    @Test
    public void test_five_card() {


        Player player = new Player("player");

        Player dealer = new Player("dealer");

        //发牌器
        DealerRobot dealerRobot = mock(DealerRobot.class);
        Tables tables = new Tables(dealerRobot, tableId);

        PlayerProxy playerProxy = player.createPlayerProxy(new BigDecimal(20));


        tables.addPlayerProxy(playerProxy);

        DealerProxy dealerProxy = dealer.createDealerProxy();
        tables.setDealerProxy(dealerProxy);
        when(dealerRobot.deal()).thenReturn(Card.create(CardColor.CLUB, CardFace._2));

        tables.opening();

        for (int i = 0; i < 3; i++) {
            tables.hit(playerProxy);
        }

        assert playerProxy.actionsContains(ReportAction.class);

        assert playerProxy.getHeadCard().getCardCount() == 5;

        tables.report(playerProxy);

        tables.dealerHitUntilGe17Point();

        WinnerLoserCalculateResult result = tables.betWinnerLoserCalculate(playerProxy);

        assert result.getWinner().equals(Winner.PLAYER);


        //过五龙的赔率的是3
        assert result.getWinAmount().doubleValue() == 60;


    }

}
