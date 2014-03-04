package com.zhaizhijun.blackjack.core;

import com.zhaizhijun.blackjack.NotFoundTablesException;

import java.util.*;

/**
 * 玩家加入赌桌的Map
 * User: zjzhai
 * Date: 2/20/14
 */
public class JoinMapping {

    private static Map<Integer, SortedSet<String>> mapping = new HashMap<Integer, SortedSet<String>>();

    /**
     * 加入某桌
     *
     * @param tableId
     * @param player
     * @throws RuntimeException
     */
    protected static void join(Integer tableId, String player) {

        if (wherePlayerIn(player) != null) return;

        if (mapping.containsKey(tableId)) {
            mapping.get(tableId).add(player);
            return;
        }

        SortedSet<String> playerSet = new TreeSet<String>();
        playerSet.add(player);
        mapping.put(tableId, playerSet);
    }

    /**
     * 玩家在哪张桌玩
     *
     * @param player
     * @return
     */
    protected static Integer wherePlayerIn(String player) {

        for (Integer each : mapping.keySet()) {
            SortedSet<String> players = mapping.get(each);
            if (players.contains(player)) return each;
        }

        return null;
    }

    /**
     * 查找在tableId桌上的所有玩家
     *
     * @param tableId
     * @return
     *
     * @throws com.zhaizhijun.blackjack.NotFoundTablesException
     */
    protected static Set<String> whoAtTables(Integer tableId) {

        if (!allTablesId().contains(tableId)) throw new NotFoundTablesException();

        return Collections.unmodifiableSet(mapping.get(tableId));
    }

    protected static boolean hadJoin(String player) {
        return wherePlayerIn(player) != null;
    }


    protected static Collection<Integer> allTablesId() {
        return mapping.keySet();
    }

    protected static void initSomeIds(Integer... ids) {
        if (null == ids) return;

        for (Integer id : ids) {
            mapping.put(id, new TreeSet<String>());
        }

    }
}
