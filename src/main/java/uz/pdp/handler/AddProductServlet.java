package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.ProductDAO;
import uz.pdp.entity.Product;
import uz.pdp.entity.User;
import uz.pdp.helper.RedirectHelper;

import java.io.IOException;

@WebServlet("/cabinet/manage-products/add")
public class AddProductServlet extends HttpServlet {
    ProductDAO dao = ProductDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/cabinet/manage-products");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RedirectHelper redirectHelper = new RedirectHelper(req, resp);
        if (redirectHelper.redirectIfNotSeller()) return;

        String name = req.getParameter("name");
        if (name.isBlank()) {
            req.getSession().setAttribute("message", "Products must have a name.");
            resp.sendRedirect(req.getContextPath() + "/cabinet/manage-products");
            return;
        }
        String description = req.getParameter("description");
        double price = Double.parseDouble(req.getParameter("price"));
        if (price <= 0) {
            req.getSession().setAttribute("message", "Price must be positive.");
            resp.sendRedirect(req.getContextPath() + "/cabinet/manage-products");
            return;
        }
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        if (quantity <= 0) {
            req.getSession().setAttribute("message", "Quantity must be positive.");
            resp.sendRedirect(req.getContextPath() + "/cabinet/manage-products");
            return;
        }
        User user = (User) req.getSession().getAttribute("user");

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        dao.saveProduct(product, user.getId());
        req.getSession().setAttribute("message", "Successfully added.");
        resp.sendRedirect("/cabinet/manage-products");
    }
}
