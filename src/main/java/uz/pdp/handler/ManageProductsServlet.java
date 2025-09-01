package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.ProductDAO;
import uz.pdp.dao.UserDAO;
import uz.pdp.entity.Product;
import uz.pdp.entity.User;
import uz.pdp.entity.enums.Role;
import uz.pdp.helper.RedirectHelper;
import uz.pdp.helper.RequestMessageHelper;

import java.io.IOException;
import java.util.List;

@WebServlet("/cabinet/manage-products")
public class ManageProductsServlet extends HttpServlet {
    ProductDAO productDAO = ProductDAO.getInstance();
    UserDAO userDAO = UserDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = (User) req.getSession().getAttribute("user");

        RedirectHelper redirectHelper = new RedirectHelper(req, resp);
        if (redirectHelper.redirectIfNotAuthenticated()) {
            return;
        }

        RequestMessageHelper messageHelper = new RequestMessageHelper(req);
        messageHelper.assignSessionMsgToReq();

        if (user.getRole().equals(Role.SELLER)) {
            List<Product> productList = productDAO.getProductsByUserId(user.getId());
            req.setAttribute("productList", productList);

            req.getRequestDispatcher("/templates/manage-products.jsp").forward(req, resp);
        } else if (user.getRole().equals(Role.ADMIN)) {
            List<Product> productList = productDAO.getAllProductsIncludingOutOfStock();
            req.setAttribute("productList", productList);

            req.getRequestDispatcher(req.getContextPath() + "/templates/admin-manage-products.jsp").forward(req, resp);
        } else {
            redirectHelper.redirectDueToDeniedAccess();
        }
    }
}