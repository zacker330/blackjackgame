package com.zhaizhijun.blackjack.core.card;

import com.zhaizhijun.blackjack.BlackjackException;

/**
 * 牌面
 * User: zjzhai
 * Date: 1/23/14
 */
public enum CardFace {
    _A {
        @Override
        public int getValue() {
            return 1;
        }
    },
    _2 {
        @Override
        public int getValue() {
            return 2;
        }
    },
    _3 {
        @Override
        public int getValue() {
            return 3;
        }
    },
    _4 {
        @Override
        public int getValue() {
            return 4;
        }
    },
    _5 {
        @Override
        public int getValue() {
            return 5;
        }
    },
    _6 {
        @Override
        public int getValue() {
            return 6;
        }
    },
    _7 {
        @Override
        public int getValue() {
            return 7;
        }
    },
    _8 {
        @Override
        public int getValue() {
            return 8;
        }
    },
    _9 {
        @Override
        public int getValue() {
            return 9;
        }
    },
    _10 {
        @Override
        public int getValue() {
            return 10;
        }
    },
    _J {
        @Override
        public int getValue() {
            return 10;
        }
    },
    _Q {
        @Override
        public int getValue() {
            return 10;
        }
    },
    _K {
        @Override
        public int getValue() {
            return 10;
        }
    };

    public abstract int getValue();

    public static boolean isAce(CardFace cardFace) {
        return _A.equals(cardFace);
    }

    /**
     * @param value 牌面，1代表ACE，10代表10,11代表J，依此类推
     * @return
     */
    public static CardFace get(Integer value) {

        if (value >= 1 && value <= 13) {
            int index = value - 1;
            return CardFace.values()[index];
        }

        throw new BlackjackException("The CardFace'value must be one of the [1:13]");
    }


    public String toString() {
        return this.name().substring(1);
    }

}
