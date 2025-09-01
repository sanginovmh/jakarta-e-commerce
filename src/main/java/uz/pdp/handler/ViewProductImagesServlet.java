package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.ImageDAO;
import uz.pdp.dao.ProductDAO;
import uz.pdp.dao.ProductImageDAO;
import uz.pdp.entity.Image;
import uz.pdp.entity.User;
import uz.pdp.helper.RedirectHelper;

import java.io.IOException;
import java.util.List;

@WebServlet("/cabinet/manage-products/images")
public class ViewProductImagesServlet extends HttpServlet {
    ProductImageDAO productImageDAO = ProductImageDAO.getInstance();
    ProductDAO productDAO = ProductDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer productId = Integer.valueOf(req.getParameter("id"));

        RedirectHelper redirectHelper = new RedirectHelper(req, resp);
        if (redirectHelper.redirectIfNotAuthenticated() ||
                redirectHelper.redirectIfNotSellerOrAccessDenied(productDAO, productId)) {
            return;
        }

        List<Image> imageList = productImageDAO.getImagesByProductId(productId);
        req.setAttribute("imageList", imageList);
        req.getRequestDispatcher("/templates/manage-product-images.jsp").forward(req, resp);
    }
}
