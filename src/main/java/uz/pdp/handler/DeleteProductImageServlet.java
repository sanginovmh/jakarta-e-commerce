package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.ImageDAO;
import uz.pdp.dao.ProductDAO;
import uz.pdp.dao.ProductImageDAO;
import uz.pdp.entity.User;
import uz.pdp.helper.RedirectHelper;

import java.io.IOException;

@WebServlet("/cabinet/manage-products/images/delete")
public class DeleteProductImageServlet extends HttpServlet {
    ProductDAO productDAO = ProductDAO.getInstance();
    ImageDAO imageDAO = ImageDAO.getInstance();
    ProductImageDAO productImageDAO = ProductImageDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer productId = Integer.valueOf(req.getParameter("id"));
        Integer imageId = Integer.valueOf(req.getParameter("imageId"));
        User user = (User) req.getSession().getAttribute("user");

        RedirectHelper redirectHelper = new RedirectHelper(req, resp);
        if (redirectHelper.redirectIfNotAuthenticated() ||
                redirectHelper.redirectIfNotSellerOrAccessDenied(productDAO, productId)) {
            return;
        }

        productImageDAO.deleteProductImage(productId, imageId);
        imageDAO.deleteImage(imageId);

        req.getSession().setAttribute("message", "Successfully removed.");
        resp.sendRedirect(req.getContextPath() + "/cabinet/manage-products/images?id=" + productId);
    }
}
