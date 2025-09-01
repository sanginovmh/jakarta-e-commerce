package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.ProductDAO;
import uz.pdp.entity.Product;
import uz.pdp.helper.RedirectHelper;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/cabinet/manage-products/edit")
public class UpdateProductServlet extends HttpServlet {
    ProductDAO productDAO = ProductDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer productId = Integer.valueOf(req.getParameter("id"));
        Optional<Product> optionalProduct = productDAO.findById(productId);
        if (optionalProduct.isPresent()) {
            req.setAttribute("product", optionalProduct.get());
            req.getRequestDispatcher("/templates/edit-product.jsp").forward(req, resp);
            return;
        }
        req.getSession().setAttribute("message", "Couldn't find product.");
        resp.sendRedirect(req.getContextPath() + "/cabinet/manage-products");
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RedirectHelper redirectHelper = new RedirectHelper(req, resp);
        Integer productId = Integer.valueOf(req.getParameter("id"));
        if (redirectHelper.redirectIfNotSellerOrAccessDenied(productDAO, productId)) return;

        String name = req.getParameter("name");
        if (name.isBlank()) {
            req.getSession().setAttribute("message", "Products must have a name.");
            resp.sendRedirect(req.getContextPath() + "/cabinet/manage-products/edit?id=" + productId);
        }
        String description = req.getParameter("description");
        double price = Double.parseDouble(req.getParameter("price"));
        if (price <= 0) {
            req.getSession().setAttribute("message", "Price must be positive.");
            resp.sendRedirect(req.getContextPath() + "/cabinet/manage-products/edit?id=" + productId);
            return;
        }
        int quantity = Integer.parseInt(req.getParameter("quantity"));
        if (quantity <= 0) {
            req.getSession().setAttribute("message", "Quantity must be positive.");
            resp.sendRedirect(req.getContextPath() + "/cabinet/manage-products/edit?id=" + productId);
            return;
        }

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setPrice(price);
        product.setQuantity(quantity);
        productDAO.updateProduct(productId, product);
        req.getSession().setAttribute("message", "Successfully edited.");
        resp.sendRedirect("/cabinet/manage-products");
    }
}
