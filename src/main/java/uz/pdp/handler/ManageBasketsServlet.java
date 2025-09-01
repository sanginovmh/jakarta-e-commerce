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
import java.util.List;

@WebServlet("/cabinet/manage-baskets")
public class ManageBasketsServlet extends HttpServlet {
    BasketDAO dao = BasketDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().removeAttribute("basketView");
        User user = (User) req.getSession().getAttribute("user");

        RedirectHelper redirectHelper = new RedirectHelper(req, resp);
        if (redirectHelper.redirectIfNotAuthenticated()) {
            return;
        }
        if (!user.getRole().equals(Role.USER)) {
            redirectHelper.redirectDueToDeniedAccess();
            return;
        }

        RequestMessageHelper messageHelper = new RequestMessageHelper(req);
        messageHelper.assignSessionMsgToReq();

        List<Basket> basketList = dao.getActiveBaskets(user.getId());
        req.setAttribute("basketList", basketList);

        req.getRequestDispatcher(req.getContextPath() + "/templates/manage-baskets.jsp").forward(req, resp);
    }
}
