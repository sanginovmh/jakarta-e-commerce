package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.ProductDAO;
import uz.pdp.dao.ProductImageDAO;
import uz.pdp.entity.User;
import uz.pdp.entity.enums.Role;
import uz.pdp.helper.RedirectHelper;

import java.io.IOException;

@WebServlet("/cabinet/manage-products/delete")
public class DeleteProductServlet extends HttpServlet {
    ProductDAO productDAO = ProductDAO.getInstance();
    ProductImageDAO productImageDAO = ProductImageDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");
        Integer productId = Integer.valueOf(req.getParameter("id"));
        if (user.getRole().equals(Role.SELLER)) {
            RedirectHelper redirectHelper = new RedirectHelper(req, resp);
            if (redirectHelper.redirectIfNotSellerOrAccessDenied(productDAO, productId)) return;
        }
        if (!user.getRole().equals(Role.ADMIN)) {
            req.getSession().setAttribute("message", "Access denied.");
            resp.sendRedirect(req.getContextPath() + "/cabinet");
            return;
        }

        productImageDAO.deleteByProductId(productId);
        productDAO.removeFromStock(productId);

        req.getSession().setAttribute("message", "Successfully deleted.");
        resp.sendRedirect(req.getContextPath() + "/cabinet/manage-products");
    }
}
