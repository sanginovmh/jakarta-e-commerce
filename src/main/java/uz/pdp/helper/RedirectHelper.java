package uz.pdp.helper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import uz.pdp.dao.LineItemDAO;
import uz.pdp.dao.ProductDAO;
import uz.pdp.entity.BrowserItem;
import uz.pdp.entity.User;
import uz.pdp.entity.enums.Role;

import java.util.List;
import java.util.function.Predicate;

@RequiredArgsConstructor
public class RedirectHelper {
    private final HttpServletRequest req;
    private final HttpServletResponse resp;

    public boolean redirectIfNotSellerOrAccessDenied(ProductDAO productDAO, Integer productId) {
        User user = (User) req.getSession().getAttribute("user");
        return redirectIfNotSeller() || redirectIfAccessToProductDenied(productDAO, productId, user.getId());
    }

    @SneakyThrows
    public boolean redirectIfNotSeller() {
        User user = (User) req.getSession().getAttribute("user");
        if (!user.getRole().equals(Role.SELLER)) {
            req.getSession().setAttribute("message", "You are not a seller.");
            resp.sendRedirect(req.getContextPath() + "/cabinet");
            return true;
        }
        return false;
    }

    @SneakyThrows
    public boolean redirectIfNotAuthenticated() {
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            req.getSession().setAttribute("message", "You are not signed in.");
            resp.sendRedirect(req.getContextPath() + "/auth/signin");
            return true;
        }
        return false;
    }

    @SneakyThrows
    public void redirectDueToDeniedAccess() {
        req.getSession().setAttribute("message", "Access denied.");
        resp.sendRedirect(req.getContextPath() + "/cabinet");
    }

    @SneakyThrows
    private boolean redirectIfAccessToProductDenied(ProductDAO productDAO,
                                                    Integer productId,
                                                    Integer userId) {
        if (!productDAO.checkUserIdMatch(productId, userId)) {
            req.getSession().setAttribute("message", "Access to product denied.");
            resp.sendRedirect(req.getContextPath() + "/cabinet/manage-products");
            return true;
        }
        return false;
    }
}
