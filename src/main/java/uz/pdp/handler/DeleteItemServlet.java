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

import java.io.IOException;

@WebServlet("/cabinet/manage-baskets/view/delete")
public class DeleteItemServlet extends HttpServlet {
    BasketDAO basketDAO = BasketDAO.getInstance();
    LineItemDAO lineItemDAO = LineItemDAO.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        RedirectHelper redirectHelper = new RedirectHelper(req, resp);
        if (redirectHelper.redirectIfNotAuthenticated()) {
            return;
        }

        Integer basketId = Integer.valueOf(req.getParameter("basketId"));

        if (!(user.getRole().equals(Role.USER) && basketDAO.checkUserIdMatch(basketId, user.getId()))) {
            redirectHelper.redirectDueToDeniedAccess();
            return;
        }

        Integer productId = Integer.valueOf(req.getParameter("productId"));
        lineItemDAO.deleteLineItem(productId, basketId);

        resp.sendRedirect(req.getContextPath() + "/cabinet/manage-baskets/view?basketId=" + basketId);
    }
}
