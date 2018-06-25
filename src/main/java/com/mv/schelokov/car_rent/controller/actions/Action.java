package com.mv.schelokov.car_rent.controller.actions;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Maxim Chshelokov <schelokov.mv@gmail.com>
 */
public interface Action {
    
    JspRedirect execute(HttpServletRequest req, HttpServletResponse res);
    
}