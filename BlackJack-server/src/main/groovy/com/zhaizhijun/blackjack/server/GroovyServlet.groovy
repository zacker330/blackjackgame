package com.zhaizhijun.blackjack.server

import javax.servlet.ServletException
import javax.servlet.http.HttpServlet
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

/**
 * User: zjzhai
 * Date: 2/19/14
 * Time: 1:22 PM
 */
class GroovyServlet extends HttpServlet {

    @Override
    void init() throws ServletException {

        Application.casino = Casino.create()
                .actionLimitedTime(0)
                .tablesCount(50)
                .randomOneOfPlayerDealerElectPloy();

    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("json/application")
        resp.getWriter().println('groovy servlet say hell')
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp)
    }

}
