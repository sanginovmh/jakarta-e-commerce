package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.BasketDAO;
import uz.pdp.entity.Basket;
import uz.pdp.entity.User;
import uz.pdp.entity.enums.Role;
import uz.pdp.helper.RedirectHelper;
import uz.pdp.helper.RequestMessageHelper;

import java.io.IOException;

@WebServlet("/cabinet/manage-baskets/add")
public class AddBasketServlet extends HttpServlet {
    BasketDAO dao = BasketDAO.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        RedirectHelper redirectHelper = new RedirectHelper(req, resp);
        if (redirectHelper.redirectIfNotAuthenticated()) {
            return;
        }
        if (!user.getRole().equals(Role.USER)) {
            redirectHelper.redirectDueToDeniedAccess();
            return;
        }

        Basket basket = new Basket();
        String name = req.getParameter("name");
        basket.setName(name);
        basket.setUser(user);
        dao.saveBasket(basket);

        req.getSession().setAttribute("message", "Successfully added.");
        resp.sendRedirect(req.getContextPath() + "/cabinet/manage-baskets");
    }
}
