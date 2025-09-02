package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import uz.pdp.dao.ImageDAO;
import uz.pdp.dao.ProductDAO;
import uz.pdp.dao.ProductImageDAO;
import uz.pdp.helper.RedirectHelper;

import java.io.IOException;

@WebServlet("/cabinet/manage-products/images/upload")
public class UploadProductImageServlet extends HttpServlet {
    ProductDAO productDAO = ProductDAO.getInstance();
    ImageDAO imageDAO = ImageDAO.getInstance();
    ProductImageDAO productImageDAO = ProductImageDAO.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer productId = Integer.valueOf(req.getParameter("id"));

        RedirectHelper redirectHelper = new RedirectHelper(req, resp);
        if (redirectHelper.redirectIfNotAuthenticated() ||
                redirectHelper.redirectIfNotSellerOrAccessDenied(productDAO, productId)) {
            return;
        }

        Part part = req.getPart("image-file");
        Integer imageId = imageDAO.uploadAndSaveImage(part);
        productImageDAO.saveProductImage(productId, imageId);

        req.getSession().setAttribute("message", "Image uploaded.");
        resp.sendRedirect(req.getContextPath() + "/cabinet/manage-products/images?id=" + productId);
    }
}
