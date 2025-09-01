package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.BasketDAO;
import uz.pdp.dao.LineItemDAO;
import uz.pdp.dao.ProductDAO;
import uz.pdp.entity.BrowserItem;
import uz.pdp.entity.LineItem;
import uz.pdp.entity.Product;
import uz.pdp.entity.User;
import uz.pdp.entity.enums.Role;
import uz.pdp.helper.RedirectHelper;
import uz.pdp.helper.RequestBasketConfirmationPopupHelper;
import uz.pdp.service.BrowserItemService;

import java.io.IOException;
import java.util.List;

@WebServlet("/cabinet/manage-baskets/view")
public class ViewBasketServlet extends HttpServlet {
    BasketDAO basketDAO = BasketDAO.getInstance();
    LineItemDAO lineItemDAO = LineItemDAO.getInstance();
    ProductDAO productDAO = ProductDAO.getInstance();
    BrowserItemService browserItemService = BrowserItemService.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().setAttribute("basketView", true);
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

        List<LineItem> lineItemList = lineItemDAO.getLineItemsByBasketId(basketId);
        List<Product> productList = productDAO.getAllProductsIncludingOutOfStock();
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

        req.getRequestDispatcher(req.getContextPath() + "/templates/view-basket.jsp").forward(req, resp);
    }
}
