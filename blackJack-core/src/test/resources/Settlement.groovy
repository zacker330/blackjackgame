

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