package com.mv.schelokov.carent.actions.admin;

import com.mv.schelokov.carent.actions.AbstractAction;
import com.mv.schelokov.carent.actions.JspForward;
import com.mv.schelokov.carent.consts.Jsps;
import com.mv.schelokov.carent.exceptions.ActionException;
import com.mv.schelokov.carent.model.services.OrderService;
import com.mv.schelokov.carent.model.services.exceptions.ServiceException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author Maxim Chshelokov <schelokov.mv@gmail.com>
 */
public class ShowOpenedOrdersPage extends AbstractAction {
    
    private static final Logger LOG = Logger.getLogger(ShowOpenedOrdersPage.class);
    private static final String ERROR = "Unable to prepare opened orders page";

    @Override
    public JspForward execute(HttpServletRequest req, HttpServletResponse res)
            throws ActionException {
        
        JspForward forward = new JspForward();
        
        if (isAdmin(req)) {
            try {
                req.setAttribute("order_list", OrderService.getInstance()
                        .getOpenedOrders());
                
                forward.setUrl(Jsps.ADMIN_OPENED_ORDERS);
                
                return forward;
            } catch (ServiceException ex) {
                LOG.error(ERROR, ex);
                throw new ActionException(ERROR, ex);
            }

        } else {
            sendForbidden(res);
            return forward;
        }
    }
}