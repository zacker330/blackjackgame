package com.zhaizhijun.blackjack.core;

import com.zhaizhijun.blackjack.core.card.*;
import com.zhaizhijun.blackjack.core.playerAction.StopAction;
import org.junit.Test;

/**
 * User: zjzhai
 * Date: 2/7/14
 * Time: 2:02 PM
 */
public class CardCategoryCalculateEngineTest {
    @Test
    public void testName() throws Exception {
        HeadCard bustHeadCard = new HeadCard();
        bustHeadCard.addCard(Card.create(CardColor.CLUB, CardFace._10));
        bustHeadCard.addCard(Card.create(CardColor.CLUB, CardFace._10));

        System.out.println(new GroovyCardCategoryCalculateEngine().calculate(bustHeadCard));


        bustHeadCard.addCard(Card.create(CardColor.CLUB, CardFace._10));


        CardCategory bustCC = new GroovyCardCategoryCalculateEngine().calculate(bustHeadCard);

        assert bustCC.getName().equals("bust");

        assert bustCC.getActions().contains(new StopAction());


        HeadCard royalHeadCard = new HeadCard();
        royalHeadCard.addCard(Card.create(CardColor.CLUB, CardFace._6));
        royalHeadCard.addCard(Card.create(CardColor.CLUB, CardFace._8));
        royalHeadCard.addCard(Card.create(CardColor.CLUB, CardFace._7));


        CardCategory cc = new GroovyCardCategoryCalculateEngine().calculate(royalHeadCard);

        assert cc.getName().equals("royal");

    }


}
