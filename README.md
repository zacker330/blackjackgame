21点游戏 v0.1.0
----

[![Build Status](https://travis-ci.org/zacker330/blackjackgame.svg?branch=master)](https://travis-ci.org/zacker330/blackjackgame)




这是一个纸牌的赌博游戏。

这个游戏的规则：每个玩家分别与庄家比点数，但是又不能超过21点。这是最原始，最简单的规则。但是发展到现代。游戏的规则已经变得复杂了很多。比如双倍下注、分牌、五龙等这样例牌的出现。

具体规则可以在维基百科上搜索“21点”。


架构目标
----

**它们的架构与功能是完全独立的。这意味着我们在构建系统时，应该选择最符合当前需求的架构，并在该架构的框架下实现功能。——《恰如其分的软件架构》**

虽然这个21点看似简单，其实不然。当然，如果你只是想实现一个只有最简单21点游戏，而不考虑**可扩展性**、**可维护性**，你大可使用一个大大的`if-else`实现。这也算是一种架构。

如果你的目标是：当QQ游戏需要添加21点游戏时，只要在你21点游戏底层库上做些配置就可以直接用了（在写本文时，我真不知道QQ游戏到底有没21点游戏）。
或者某家在线赌博网站想运营一个21点游戏，拿来你的21点游戏底层库，在上面进行配置或扩展，就可以直接用了。

说得再细一些。有些赌场对于Blackjack是3倍赔率，而有些是2倍。有些赌场允许分牌后再分，
有些则不允许。有些赌场只允许在首两张牌时进行加注，而有些赌场允许在任何时候进行加注。

而敝人也希望这个底层库尽可能实现所有的场景。


难点及解决方案
----
难点在于21点游戏的规则没有标准，每个赌场的规则都可能不同。而我们写出的程序必须应对所有的变化。

解决方案是

1. 实现21点游戏的领域特定语言，各个赌场只需要修改领域特定语言就可以，而不需要关心具体实现技术
1. 将游戏中相关稳定的部分（如游戏流程，开牌每人手上都会有两张牌）固化下来，使用java实现。而游戏中变化的部分使用
动态语言实现。

而我采用的是第二种解决方案。

分析与设计

首先要知道21点的游戏流程是不会变的，而变的则是例牌的设置、例牌的赔率，而且同一例牌的规则也可能会不同。

所以，游戏流程可以写死，而例牌方面直接使用动态语言Groovy来实现。

设计中有几个重要的概念需要介绍，这样有利于了解我的设计与架构：

**玩家**(player)：首先庄家有可能是人，也有可能是机器。所以，不论机器或人，在赌桌上所有的人、机器都是玩家。
当赌局里的庄家是随机的时候，意味着所有的玩家都可能做庄家。


**玩家代理**(playerProxy)：有些规则是允许一个玩家下多门注的。所以，我们抽象出`玩家代理`这个概念。实际掌握手上牌和赌注的是玩家代理。
玩家的所有的操作实际上都是由`玩家代理`执行。这样就可以解决一个玩家有多门注，及分牌的问题。

**庄家代理**(dealerProxy)：当一个玩家做了庄家，那么他在赌局中实际是通过`庄家代理`来完成操作。

**牌型**(cardCategory)：牌型指的是手上牌所符合的模式。比如首两张牌组成21点，那么这手牌的牌型就是Blackjack。由于赔率很大程度与牌型及玩
家操作决定，而且每个赌场的规则又不一样。所以，这部分使用Groovy实现。

**玩家操作**(playerAction)：玩家在游戏中只有有限的几个操作：要牌（hit），停牌（stand）等。所以，这部分，直接使用java实现。

**银行**(bank)：这是对赌场中钱的管理人的一个抽象。统一管理所有的钱、判断玩家是否还有足够的钱进行加注等。

**赌桌**(tables)：玩家加入赌桌进行赌博。而赌桌则是所有玩家玩牌的接口。


使用Groovy来实现的好处有：

1. 实时修改游戏规则（例牌等），不需要重启服务器
1. 提高程序的可扩展性
1. 提高游戏的可配置性

为什么不使用XML来配置呢？

鉴于很多人喜欢使用XML做配置文件，我的想法如下：

1. XML对于游戏规则的表达力不够
1. 还需要自己写解释器，注意，这个解释器并不是JDOM这样的库，而是指语义解释器。而且，使用java处理XML，你懂的～
1. 为什么还有多一层解释呢？配置即执行不多好？目前ROR，Gradle，Vagrant也都是配置即执行。
1. 本人不喜欢XML的哆嗦


当然，如果你希望实现一个使用XML配置的，那也可以。本库的扩展非常好。只需要改`WinnerLoserCalculateEngine`这个
输赢计算引擎就可以了。


关于例牌
----
由于例牌是21点中最大的变化，而且各类繁多。所以，我有必要在这里说明一下，哪些例牌是我已经实现并测试了的。

## 已经实现了的

1. 保险
    保险与玩家下的筹码是独立的。是单独结算的。
    **所以分牌时，保险金不会分**

1. Blackjack：
    开牌后就得到了21点（一张面牌为10的牌，一张A）

1. 分牌(split) ：
    玩家首两张牌的点数相同，则可以选择分牌，并须加注。分出的每门的下注金额须与原注相同。
    **注：有些赌场可分牌后再分，有些则不能**

1. 双倍下注:
    发牌后，玩家手上两张牌的点数加起来有11点，就可以进行双倍下注。但是双倍下注后，只能要一张牌。

1. 过五龙：
    玩家或庄家手上的牌超过了5张，但还没有爆牌的情况。

1. 同花顺:
    玩家手上有三张牌：牌色相同，面值分别是6,7,8的情况。

实际上，你可以发挥你的想像来实现你的例牌。比如，当庄家为Blackjack而玩家为五龙，则为打平等等。

## 未实现的操作

1. 投降(surrender)
    首两张牌可以投降，其他情况不可以投降。投降后，玩家只可以拿回原来赌注的一半。

1. Ace分牌(AceSplit)：
    这是分牌的特例。当玩家首两张牌都是A牌时，可以选择分牌，但是分牌后只能得到一张牌。

1. 退出



分层
---
在这里，你看不到什么MVC这样的分层方式。是因为我们这个库目前做的事情就是21点最核心的业务。所以MVC这样的分层方式不适合。
目前，你也看不到关于持久化的内容。因为，一持久化是客户的事情，二我目前还没有时间写一个默认的实现。

Blackjack-core 核心包
-----
这是一个核。实现目标是将21点游戏的业务逻辑集中在这个核内。

### 它可以做什么：
* 技术上：
    1. 提供方便，简单的接口
    1. 可自定义你的例牌及赔率，并且是实时的，不需要重启服务器
    1. 洗牌算法的可替换

* 业务上：
    1. 庄家可以是真人，也可以是机器
    1. 支持一个玩家可以下多注
    1. 未限定投注大小，注的限额，我觉得不是21点游戏的核心问题，所以，不实现。
    1. 可以设置保险的赔率


### 它不可以做什么：
1. 不可以持久化:玩家数据，金钱数据都存在内存里
1. 不提供界面
1. 发牌时的那种一张牌，一张牌的显示的那样的效果。那不应该是核心业务问题。


Blackjack-server 服务器端
-----
目前还没时间做


关于持久化
----
目前，关于持久化的工作几乎为0。因为我在游戏开发方面没有经验，有一些问题没有想清楚。

1. 游戏过程的数据是否需要持久？
    游戏过程是指记录下玩家的每张出牌，每次的操作。这个过程，我觉得1.完全没有必要；2.性能上的问题。
1. 游戏中的金钱问题
    关于钱的数据当然要持久化了。而且，在设计上要求非常的高。在这方面的我经验也不足。需要从别人系统学习后再设计。


要解决的问题
---
1. 如何实现全自动化测试？
    也就是不需要任何的mock。玩家自动玩牌。

1. 如果没有实现自动化测试，那么也就没有测试并发情况也会发生什么。

1. Groovy脚本配置的带来的安全问题

如何阅读代码
---
有一个`CasinoTest`测试类。测试类模拟的是一场简单的赌博。从中可以看出如何使用此核心库。

下面是跑测试时的输出：

		12:33:54.568 [main] INFO  c.z.blackjack.core.CasinoTest - 三名玩家分别存入赌场
		12:33:54.573 [main] INFO  c.z.blackjack.core.CasinoTest - 甲 存入：400.0
		12:33:54.576 [main] INFO  c.z.blackjack.core.CasinoTest - 乙 存入：400.0
		12:33:54.577 [main] INFO  c.z.blackjack.core.CasinoTest - 丙 存入：400.0
		12:33:54.580 [main] WARN  c.z.blackjack.core.CasinoTest - 三名玩家加入赌桌:1
		12:33:54.584 [main] INFO  c.z.blackjack.core.CasinoTest - 选举出庄家为：甲 
		12:33:54.586 [main] INFO  c.z.blackjack.core.CasinoTest - 玩家乙 下注：20
		12:33:54.586 [main] INFO  c.z.blackjack.core.CasinoTest - 玩家丙 下注：20
		12:33:56.154 [main] INFO  c.z.blackjack.core.CasinoTest - *****开始******
		12:33:56.154 [main] INFO  c.z.blackjack.core.CasinoTest - 乙 手上的牌，点数：[20] 分别为：[{SPADE-Q}, {SPADE-K}]
		12:33:56.154 [main] INFO  c.z.blackjack.core.CasinoTest - 丙 手上的牌，点数：[17] 分别为：[{HEART-J}, {HEART-7}]
		12:33:56.236 [main] INFO  c.z.blackjack.core.CasinoTest - 乙 停牌，点数：20，分别为：[{SPADE-Q}, {SPADE-K}]
		12:33:56.319 [main] INFO  c.z.blackjack.core.CasinoTest - 丙 停牌，点数：17，分别为：[{HEART-J}, {HEART-7}]
		12:33:56.320 [main] INFO  c.z.blackjack.core.CasinoTest - 庄家甲 点数为14不足17点
		12:33:56.474 [main] INFO  c.z.blackjack.core.CasinoTest - 庄家要牌，最后点数：[24] 牌为：[{HEART-6}, {HEART-8}, {DIAMOND-K}]
		12:33:56.474 [main] INFO  c.z.blackjack.core.CasinoTest - **********所有玩家停牌，开始结算
		12:33:56.737 [main] INFO  c.z.blackjack.core.CasinoTest - 乙  下注 20.0 赢家为PLAYER 最后余额：420.0
		12:33:56.817 [main] INFO  c.z.blackjack.core.CasinoTest - 丙  下注 20.0 赢家为PLAYER 最后余额：420.0
		12:33:56.817 [main] INFO  c.z.blackjack.core.CasinoTest - 庄家甲  最后余额:360.0
		12:33:56.818 [main] WARN  c.z.blackjack.core.CasinoTest - *******结束



附录
===


1. 保险规则配置
    * 配置文件为类路径下：Insurance.groovy
    * 例子

            //玩家赢时将得到的彩金
            winMoney = playerFirstBet * 2

1. 结算相关配置
	* 实质是配置根据玩家的牌型及庄家的牌来计算输赢，并返回赔率
	* 配置文件为类路径下：Settlement.groovy
	* 例子

            //如果玩家赢，赢多少
			winMoney = 0

            switch (playerCardCategory) {
            //同花顺
                case "royal":
                    winner = PLAYER
                    winMoney = playerBetSum * 3
                    break
                case "Blackjack":
                    switch (playerLatestAction) {
                        case stand:
                            if (isDealerBlackjack) {
                                winner = PUSH
                            } else if (!isDealerBlackjack) {
                                winner = PLAYER
                                winMoney = playerBetSum * 3
                            }
                        default:
                            winner = PLAYER
                            winMoney = playerBetSum * 3
                            break
                    }
                    break
                case "fiveCard":
                    winner = PLAYER
                    winMoney = playerBetSum * 3
                    break
                case "bust":
                    winner = DEALER
                    break
                default:
                    //庄家爆牌
                    if (dealerSumPoint > 21) {
                        if(playerSumPoint <= 21){
                            winner = PLAYER
                            winMoney = playerBetSum * 1;
                            break
                        }

                        if (playerSumPoint > 21) {
                            winner = PUSH
                            winMoney = 0
                            break;
                        }

                    }else{
                        if (playerSumPoint > dealerSumPoint) {
                            winner = PLAYER
                            winMoney = playerBetSum * 1
                        } else if (playerSumPoint < dealerSumPoint) {
                            winner = DEALER
                        } else {
                            winner = PUSH
                        }
                    }


                    break
            }


1. 牌型配置
	* 实质是根据玩家手上的牌，计算出手上牌所属的牌型
	* 配置文件为类路径下：Special.groovy
	* 例子  


    		switch (_headCard.cardCount) {
                case 2:
                    if (_headCard.sumPoint == 21) { // 首两张牌点数为21点时是Blackjack
                        name = "Blackjack"
                        actions = [_report]
                    } else if (_headCard.sumPoint == 11) {  // 首两张下牌点数等于11点时可以双倍下注
                        name = "doubleDown"
                        actions = [_hit, _stand, _doubleDown]
                    } else if (_headCard.isFirstTwoCardPointEquals()) { //首两张牌的面值相同，可进行分牌
                        //设置分牌后不可以再分。
                        if (_headCard.isSplit()) {
                            return
                        }
                        name = "split"
                        actions = [_hit, _stand, _split]
                    }
                    break
                case 3:
                    // 同花顺
                    if (_headCard.containsAllCardFace(6, 7, 8) && _headCard.isCardFaceIdentical()) {
                        name = "royal"
                        actions = [_report]
                    }
                    break
                case 5:
                    if (_headCard.sumPoint <= 21){
                        name = "fiveCard"
                        actions = [_report]
                    }
                    break
            }

            // 爆牌
            if (_headCard.sumPoint > 21) {
                name = "bust"
                actions = [_stop]
            }


1. 在配置脚本中可以使用的变量 ：

         binding.setVariable("PLAYER", Winner.PLAYER);
         binding.setVariable("DEALER", Winner.DEALER);
         binding.setVariable("PUSH", Winner.PUSH);

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
         binding.setVariable(StandAction._name, StandAction._name);
         binding.setVariable(HitAction._name, HitAction._name);
         binding.setVariable(ReportAction._name, ReportAction._name);
         binding.setVariable(DoubleDownAction._name, DoubleDownAction._name);
         binding.setVariable(SplitAction._name, SplitAction._name);
         binding.setVariable(StopAction._name, StopAction._name);
         binding.setVariable(SurrenderAction._name, SurrenderAction._name);

	
	




