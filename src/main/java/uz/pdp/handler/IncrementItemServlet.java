package uz.pdp.handler;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.BasketDAO;
import uz.pdp.dao.LineItemDAO;
import uz.pdp.dao.ProductDAO;
import uz.pdp.entity.Product;
import uz.pdp.entity.User;
import uz.pdp.entity.enums.Role;
import uz.pdp.helper.RedirectHelper;

import java.io.IOException;

@WebServlet("/cabinet/browse/increment")
public class IncrementItemServlet extends HttpServlet {
    BasketDAO basketDAO = BasketDAO.getInstance();
    LineItemDAO lineItemDAO = LineItemDAO.getInstance();
    ProductDAO productDAO = ProductDAO.getInstance();

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
        if (lineItemDAO.getAmount(productId, basketId) < productDAO.getQuantity(productId)) {
            lineItemDAO.incrementAmount(productId, basketId);
        } else {
            req.getSession().setAttribute("message", "Invalid action.");
        }

        Boolean basketView = (Boolean) req.getSession().getAttribute("basketView");
        if (basketView != null) {
            resp.sendRedirect(req.getContextPath() + "/cabinet/manage-baskets/view?basketId=" + basketId);
        } else {
            resp.sendRedirect(req.getContextPath() + "/cabinet/browse?basketId=" + basketId);
        }
    }
}
