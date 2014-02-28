package com.zhaizhijun.blackjack

import com.zhaizhijun.blackjack.core.Casino
import com.zhaizhijun.blackjack.core.Player
import com.zhaizhijun.blackjack.core.Tables
import com.zhaizhijun.blackjack.server.Casino
import org.junit.Test

/**
 * User: zjzhai
 * Date: 2/20/14
 * Time: 12:46 PM
 */
public class MainTest {

    @Test
    public void testAll() throws Exception {


        Player dealer = new Player("A person");
        Player person1 = new Player("B person");
        Player person2 = new Player("C person");

        Casino.signUp(dealer, person1, person2);

        Casino.deposit(dealer, new BigDecimal(400));
        Casino.deposit(person1, new BigDecimal(400));
        Casino.deposit(person2, new BigDecimal(400));

        int tableId = 1;
        Casino.joinTable(tableId, dealer);
        Casino.joinTable(tableId, person1, person2);


        Casino.ready(dealer);
        Casino.ready(person1);
        Casino.ready(person2);

        Tables tables = Casino.show(tableId);

        Casino.bet(person1, new BigDecimal(20));
        Casino.bet(person2, new BigDecimal(30));

        int person1ProxyId = 1;
        int person2ProxyId = 2;
        Casino.action(person1ProxyId, "hit");
        Casino.action(person1ProxyId, "hit");


    }

    @Test
    public void test2() throws Exception {


    }
}
