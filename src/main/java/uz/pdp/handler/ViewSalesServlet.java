package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.OrderItemDAO;
import uz.pdp.dao.ProductDAO;
import uz.pdp.entity.OrderItem;
import uz.pdp.entity.User;
import uz.pdp.helper.RedirectHelper;
import uz.pdp.helper.RequestMessageHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/cabinet/view-sales")
public class ViewSalesServlet extends HttpServlet {
    ProductDAO productDAO = ProductDAO.getInstance();
    OrderItemDAO orderItemDAO = OrderItemDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        RedirectHelper redirectHelper = new RedirectHelper(req, resp);
        if (redirectHelper.redirectIfNotAuthenticated() || redirectHelper.redirectIfNotSeller()) {
            return;
        }

        List<Integer> productIdsByUserId = productDAO.getProductIdsByUserId(user.getId());
        List<OrderItem> orderedItemList = new ArrayList<>();
        productIdsByUserId
                .forEach(id -> orderedItemList.addAll(orderItemDAO.getOrderItemsByProductId(id)));
        req.setAttribute("orderedItemList", orderedItemList);

        RequestMessageHelper messageHelper = new RequestMessageHelper(req);
        messageHelper.assignSessionMsgToReq();

        req.getRequestDispatcher("/templates/sales.jsp").forward(req, resp);
    }
}
