package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.ProductDAO;
import uz.pdp.entity.User;
import uz.pdp.entity.enums.Role;

import java.io.IOException;

@WebServlet("/cabinet/manage-users/clear-products")
public class ClearUserProductsServlet extends HttpServlet {
    ProductDAO dao = ProductDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User admin = (User) req.getSession().getAttribute("user");
        Integer userId = Integer.valueOf(req.getParameter("id"));
        if (admin == null) {
            req.getSession().setAttribute("message", "You are not logged in.");
            resp.sendRedirect(req.getContextPath() + "/auth/signin");
            return;
        }
        if (!admin.getRole().equals(Role.ADMIN)) {
            req.getSession().setAttribute("message", "Access denied.");
            resp.sendRedirect(req.getContextPath() + "/cabinet");
            return;
        }
        int deletedCount = dao.removeFromStockByUserId(userId);
        if (deletedCount == 0) {
            req.getSession().setAttribute("message", "No products to clear.");
            resp.sendRedirect(req.getContextPath() + "/cabinet/manage-users");
            return;
        }
        req.getSession().setAttribute("message", "Cleared products: " + deletedCount);
        resp.sendRedirect(req.getContextPath() + "/cabinet/manage-users");
    }
}
