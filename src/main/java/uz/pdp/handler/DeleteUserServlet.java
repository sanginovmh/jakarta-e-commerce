package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.BasketDAO;
import uz.pdp.dao.LineItemDAO;
import uz.pdp.dao.UserDAO;
import uz.pdp.entity.enums.Role;

import java.io.IOException;

@WebServlet("/cabinet/manage-users/delete")
public class DeleteUserServlet extends HttpServlet {
    UserDAO userDAO = UserDAO.getInstance();
    BasketDAO basketDAO = BasketDAO.getInstance();
    LineItemDAO lineItemDAO = LineItemDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = Integer.valueOf(req.getParameter("id"));
        if (userDAO.getRole(id).equals(Role.ADMIN.name())) {
            req.getSession().setAttribute("message", "Cannot delete an admin.");
            resp.sendRedirect(req.getContextPath() + "/cabinet/manage-users");
            return;
        }

        userDAO.disableUser(id);

        req.getSession().setAttribute("message", "Successfully deleted.");
        resp.sendRedirect("/cabinet/manage-users");
    }
}
