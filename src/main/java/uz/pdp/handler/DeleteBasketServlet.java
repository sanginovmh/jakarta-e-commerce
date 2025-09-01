package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.BasketDAO;
import uz.pdp.dao.LineItemDAO;
import uz.pdp.entity.User;
import uz.pdp.entity.enums.Role;
import uz.pdp.helper.RedirectHelper;
import uz.pdp.helper.RequestMessageHelper;

import java.io.IOException;

@WebServlet("/cabinet/manage-baskets/delete")
public class DeleteBasketServlet extends HttpServlet {
    BasketDAO basketDAO = BasketDAO.getInstance();
    LineItemDAO lineItemDAO = LineItemDAO.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer basketId = Integer.valueOf(req.getParameter("id"));
        User user = (User) req.getSession().getAttribute("user");
        RedirectHelper redirectHelper = new RedirectHelper(req, resp);
        if (redirectHelper.redirectIfNotAuthenticated()) {
            return;
        }

        if (!(user.getRole().equals(Role.USER) && basketDAO.checkUserIdMatch(basketId, user.getId()))) {
            redirectHelper.redirectDueToDeniedAccess();
            return;
        }

        RequestMessageHelper messageHelper = new RequestMessageHelper(req);
        messageHelper.assignSessionMsgToReq();

        lineItemDAO.deleteLineItemsByBasketId(basketId);
        basketDAO.deleteBasket(basketId);
        req.getSession().setAttribute("message", "Successfully deleted.");
        resp.sendRedirect(req.getContextPath() + "/cabinet/manage-baskets");
    }
}
