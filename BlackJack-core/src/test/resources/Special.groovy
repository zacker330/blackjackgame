

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
