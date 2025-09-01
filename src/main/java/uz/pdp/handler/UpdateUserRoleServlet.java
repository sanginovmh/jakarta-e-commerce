package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.UserDAO;
import uz.pdp.entity.enums.Role;

import java.io.IOException;

@WebServlet("/cabinet/manage-users/update-role")
public class UpdateUserRoleServlet extends HttpServlet {
    UserDAO dao = UserDAO.getInstance();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Integer id = Integer.parseInt(req.getParameter("id"));
        if (dao.getRole(id).equals(Role.ADMIN.name())) {
            req.getSession().setAttribute("message", "Cannot update an admin.");
            resp.sendRedirect(req.getContextPath() + "/cabinet/manage-users");
            return;
        }
        String role = req.getParameter("role");
        dao.updateRole(role, id);
        req.getSession().setAttribute("message", "Successfully updated.");
        resp.sendRedirect("/cabinet/manage-users");
    }
}
