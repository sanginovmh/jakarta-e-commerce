package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.BasketDAO;
import uz.pdp.dao.OrderItemDAO;
import uz.pdp.entity.Basket;
import uz.pdp.entity.Order;
import uz.pdp.entity.User;
import uz.pdp.entity.enums.Role;
import uz.pdp.helper.RedirectHelper;
import uz.pdp.helper.RequestMessageHelper;
import uz.pdp.service.OrderService;

import java.io.IOException;
import java.util.List;

@WebServlet("/cabinet/manage-orders")
public class ManageOrdersServlet extends HttpServlet {
    BasketDAO basketDAO = BasketDAO.getInstance();
    OrderItemDAO orderItemDAO = OrderItemDAO.getInstance();
    OrderService orderService = OrderService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        RedirectHelper redirectHelper = new RedirectHelper(req, resp);
        if (redirectHelper.redirectIfNotAuthenticated()) {
            return;
        }

        if (!(user.getRole().equals(Role.USER))) {
            redirectHelper.redirectDueToDeniedAccess();
            return;
        }

        RequestMessageHelper messageHelper = new RequestMessageHelper(req);
        messageHelper.assignSessionMsgToReq();

        List<Basket> basketList = basketDAO.getOrderedBaskets(user.getId());
        List<Order> orderList = orderService.getOrderList(basketList, orderItemDAO::getOrderItemsByBasketId);
        req.setAttribute("orderList", orderList);
        req.getRequestDispatcher("/templates/manage-orders.jsp").forward(req, resp);
    }
}
