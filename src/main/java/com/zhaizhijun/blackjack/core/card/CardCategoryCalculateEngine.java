package com.zhaizhijun.blackjack.core.card;

/**
 * 牌型计算引擎，事实上是一个牌型规则匹配器。比如手上牌有6,7,8，它们的牌的颜色都是一样的，那么它的牌型就是同花顺，而牌型计算引擎
 * 的目标就是完成此推导过程
 * User: zjzhai
 * Date: 2/7/14
 */
public interface CardCategoryCalculateEngine {


    public CardCategory calculate(HeadCard headCard);


}
