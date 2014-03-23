package com.zhaizhijun.blackjack.core.card;

import com.zhaizhijun.blackjack.BlackjackException;
import com.zhaizhijun.blackjack.core.playerAction.*;
import groovy.lang.Binding;
import groovy.lang.GroovyShell;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * 默认实现的牌型计算器<p/>
 * 原理是将一手牌（HeadCard）、完成能进行的操作这些基础数据注入到groovy脚本中<p/>
 * 用户在脚本使用这些基础数据来自定义自己的牌型
 * User: zjzhai
 * Date: 2/27/14
 */
public class GroovyCardCategoryCalculateEngine implements CardCategoryCalculateEngine {
    // 牌型计算规则脚本的路径
    private static final String CARD_CATEGORY_CALCULATE_SCRIPT_PATH = "/";

    public CardCategory calculate(HeadCard headCard) {
        Binding binding = new Binding();
        binding.setVariable("_headCard", headCard);
        injectPlayerActionTo(binding);
        GroovyShell shell = new GroovyShell(binding);
        try {
            shell.evaluate(getScriptFileBy("Special.groovy"));
        } catch (IOException e) {
            throw new BlackjackException("CardCategory calculate exception", e);
        }
        List<PlayerAction> actions = (List<PlayerAction>) shell.getVariable("actions");
        String name = (String) shell.getVariable("name");
        if (null == name) {
            return new GeneralCardCategory();
        }
        return new CardCategory(name, actions);
    }

    /**
     * 将变量注入到脚本中
     *
     * @param binding
     */
    private static void injectPlayerActionTo(Binding binding) {
        binding.setVariable("_doubleDown", new DoubleDownAction());
        binding.setVariable("_hit", new HitAction());
        binding.setVariable("_report", new ReportAction());
        binding.setVariable("_split", new SplitAction());
        binding.setVariable("_stand", new StandAction());
        binding.setVariable("_stop", new StopAction());
        binding.setVariable("_surrender", new SurrenderAction());
    }


    private static File getScriptFileBy(String scriptFileName) {
        return new File(getScriptBasePath() + scriptFileName);
    }


    private static String getScriptBasePath() {
        return CardCategoryCalculateEngine.class.getResource(CARD_CATEGORY_CALCULATE_SCRIPT_PATH).getFile();
    }
}
