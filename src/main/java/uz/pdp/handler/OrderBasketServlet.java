package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.BasketDAO;
import uz.pdp.dao.LineItemDAO;
import uz.pdp.dao.OrderItemDAO;
import uz.pdp.dao.ProductDAO;
import uz.pdp.entity.*;
import uz.pdp.entity.enums.Role;
import uz.pdp.helper.RedirectHelper;
import uz.pdp.helper.RequestBasketConfirmationPopupHelper;
import uz.pdp.helper.RequestMessageHelper;
import uz.pdp.service.BrowserItemService;
import uz.pdp.service.OrderItemService;

import java.io.IOException;
import java.util.List;

@WebServlet("/cabinet/manage-basket/view/order")
public class OrderBasketServlet extends HttpServlet {
    OrderItemDAO orderItemDAO = OrderItemDAO.getInstance();
    BasketDAO basketDAO = BasketDAO.getInstance();
    LineItemDAO lineItemDAO = LineItemDAO.getInstance();
    ProductDAO productDAO = ProductDAO.getInstance();
    BrowserItemService browserItemService = BrowserItemService.getInstance();
    OrderItemService orderItemService = OrderItemService.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer basketId = Integer.valueOf(req.getParameter("basketId"));
        User user = (User) req.getSession().getAttribute("user");
        RedirectHelper redirectHelper = new RedirectHelper(req, resp);
        if (redirectHelper.redirectIfNotAuthenticated()) {
            return;
        }

        if (!(user.getRole().equals(Role.USER) && basketDAO.checkUserIdMatch(basketId, user.getId()))) {
            redirectHelper.redirectDueToDeniedAccess();
            return;
        }
        if (lineItemDAO.getLineItemsByBasketId(basketId).isEmpty()) {
            req.getSession().setAttribute("message", "Basket is empty.");
            resp.sendRedirect(req.getContextPath() + "/cabinet/manage-baskets");
            return;
        }

        RequestMessageHelper messageHelper = new RequestMessageHelper(req);
        messageHelper.assignSessionMsgToReq();

        List<Product> productList = productDAO.getAllProducts();
        List<LineItem> lineItemList = lineItemDAO.getLineItemsByBasketId(basketId);
        List<BrowserItem> browserItemList = browserItemService.collectBrowserItems(productList, lineItemList);
        req.setAttribute("browserItemList", browserItemList);

        List<BrowserItem> invalidItemsList = browserItemService.getInvalidItemsList(
                browserItemList,
                (amount, productId) -> amount > productDAO.getQuantity(productId));
        if (!invalidItemsList.isEmpty()) { // for a popup and user confirmation before ordering
            RequestBasketConfirmationPopupHelper popupHelper = new RequestBasketConfirmationPopupHelper(req, resp);
            popupHelper.forwardWithConfirmationPopup(invalidItemsList, lineItemDAO, basketId, productDAO);
            return;
        }

        List<OrderItem> orderItemList = orderItemService.convertToOrderItems(basketId, browserItemList);
        orderItemDAO.saveOrderItems(orderItemList);
        basketDAO.orderBasket(basketId);
        lineItemDAO.deleteLineItemsByBasketId(basketId);
        orderItemList.forEach(i -> productDAO.reduceQuantityBy(i.getProductId(), i.getAmount()));

        req.getSession().setAttribute("message", "Successfully ordered.");
        resp.sendRedirect(req.getContextPath() + "/cabinet/manage-baskets");
    }
}
