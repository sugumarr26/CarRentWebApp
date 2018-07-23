package com.mv.schelokov.car_rent.controller.actions;

import com.mv.schelokov.car_rent.controller.exceptions.ActionException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Maxim Chshelokov <schelokov.mv@gmail.com>
 */
public class ChangeLocale implements Action {
    private static final int ONE_MONTH = 60 * 60 * 24 * 30;

    @Override
    public JspForward execute(HttpServletRequest req, HttpServletResponse res)
            throws ActionException {
        String localeString = req.getParameter("locale");
        if ("en".equals(localeString) || "ru".equals(localeString)) {
            Cookie cookie = new Cookie("lang", localeString);
            cookie.setMaxAge(ONE_MONTH);
            res.addCookie(cookie);
        }
        return new JspForward("action/home", true);
    }
}
