package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.BasketDAO;
import uz.pdp.dao.LineItemDAO;
import uz.pdp.dao.ProductDAO;
import uz.pdp.entity.*;
import uz.pdp.entity.enums.Role;
import uz.pdp.helper.RedirectHelper;
import uz.pdp.helper.RequestMessageHelper;
import uz.pdp.service.BrowserItemService;

import java.io.IOException;
import java.util.List;

@WebServlet("/cabinet/browse")
public class BrowseServlet extends HttpServlet {
    BasketDAO basketDAO = BasketDAO.getInstance();
    ProductDAO productDAO = ProductDAO.getInstance();
    LineItemDAO lineItemDAO = LineItemDAO.getInstance();
    BrowserItemService browserItemService = BrowserItemService.getInstance();

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

        String basketIdParam = req.getParameter("basketId");
        if (basketIdParam != null && !basketIdParam.isEmpty()) {
            Integer basketId = Integer.valueOf(basketIdParam);
            req.setAttribute("selectedBasketId", basketId);

            fetchBrowserData(req, user, basketId);
        }

        List<Basket> basketList = basketDAO.getActiveBaskets(user.getId());
        req.setAttribute("basketList", basketList);
        req.getRequestDispatcher(req.getContextPath() + "/templates/browse.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer basketId = Integer.valueOf(req.getParameter("basketId"));
        User user = (User) req.getSession().getAttribute("user");

        RedirectHelper redirectHelper = new RedirectHelper(req, resp);
        if (redirectHelper.redirectIfNotAuthenticated()) {
            return;
        }

        if (!(user.getRole().equals(Role.USER)) && basketDAO.checkUserIdMatch(basketId, user.getId())) {
            redirectHelper.redirectDueToDeniedAccess();
            return;
        }

        RequestMessageHelper messageHelper = new RequestMessageHelper(req);
        messageHelper.assignSessionMsgToReq();

        fetchBrowserData(req, user, basketId);

        req.setAttribute("selectedBasketId", basketId);
        req.getRequestDispatcher(req.getContextPath() + "/templates/browse.jsp").forward(req, resp);
    }

    private void fetchBrowserData(HttpServletRequest req, User user, Integer basketId) {
        List<Basket> basketList = basketDAO.getActiveBaskets(user.getId());
        req.setAttribute("basketList", basketList);

        List<Product> productList = productDAO.getAllProducts();
        List<LineItem> lineItemList = lineItemDAO.getLineItemsByBasketId(basketId);
        List<BrowserItem> browserItemList = browserItemService
                .alignBrowserItems(productList, lineItemList);
        req.setAttribute("browserItem", browserItemList);
    }
}
