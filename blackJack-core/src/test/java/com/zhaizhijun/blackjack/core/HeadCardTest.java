package com.zhaizhijun.blackjack.core;

import com.zhaizhijun.blackjack.core.card.Card;
import com.zhaizhijun.blackjack.core.card.CardColor;
import com.zhaizhijun.blackjack.core.card.CardFace;
import com.zhaizhijun.blackjack.core.card.HeadCard;
import org.junit.Test;

/**
 * User: zjzhai
 * Date: 2/3/14
 */
public class HeadCardTest {
    @Test
    public void testName() throws Exception {
        HeadCard headCard = new HeadCard();
        headCard.addCard(Card.create(CardColor.CLUB, CardFace._3));

        assert headCard.getAllCardFace().get(0).equals(CardFace._3);

        headCard.addCard(Card.create(CardColor.DIAMOND, CardFace._J));

        assert headCard.getAllCardFace().get(1).equals(CardFace._J);


    }

    @Test
    public void testSumPoint() throws Exception {

        HeadCard headCard = new HeadCard();
        headCard.addCard(Card.create(CardColor.CLUB, CardFace._10));
        headCard.addCard(Card.create(CardColor.HEART, CardFace._A));

        assert  headCard.getSumPoint() == 21;


        HeadCard headCard1 = new HeadCard();
        headCard1.addCard(Card.create(CardColor.CLUB, CardFace._3));
        headCard1.addCard(Card.create(CardColor.HEART, CardFace._A));

        assert  headCard1.getSumPoint() == 14;



    }

    /**
     * 同花顺
     *
     * @throws Exception
     */
    @Test
    public void testCardColor() throws Exception {
        HeadCard headCard = new HeadCard();
        headCard.addCard(Card.create(CardColor.CLUB, CardFace._3));
        headCard.addCard(Card.create(CardColor.DIAMOND, CardFace._J));
        assert !headCard.isCardFaceIdentical();

        HeadCard headCard1 = new HeadCard();
        headCard1.addCard(Card.create(CardColor.DIAMOND, CardFace._6));
        headCard1.addCard(Card.create(CardColor.DIAMOND, CardFace._7));
        headCard1.addCard(Card.create(CardColor.DIAMOND, CardFace._8));
        assert headCard1.isCardFaceIdentical();


    }


}
