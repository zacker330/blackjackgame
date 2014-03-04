package com.zhaizhijun.blackjack.core;

import java.util.*;

/**
 * 赌桌池
 * User: zjzhai
 * Date: 2/21/14
 * Time: 4:36 PM
 */
public class TablesPool {


    private static List<Tables> tablesList = new ArrayList<Tables>();

    protected static Tables findTablesByPlayer(String player) {
        if (!JoinMapping.hadJoin(player)) {
            return null;
        }
        return findTablesById(JoinMapping.wherePlayerIn(player));
    }

    protected static Tables findTablesById(Integer id) {
        if (null == id || id <= 0) return null;

        for (Tables each : tablesList) {
            if (each.getId() == id) return each;
        }
        return null;
    }

    protected static void join(int tableId, String player) {
        JoinMapping.join(tableId, player);
    }

    protected static void init(int tableCount) {
        for (int id = 0; id < tableCount; id++) {
            tablesList.add(new Tables(8, id));
        }

    }

    public static List<Integer> allTablesId() {
        List<Integer> result = new ArrayList<Integer>();
        for (Tables each : tablesList) {
            result.add(each.getId());
        }

        return Collections.unmodifiableList(result);
    }

    public static List<Tables> allTables() {
        return Collections.unmodifiableList(tablesList);
    }
}
