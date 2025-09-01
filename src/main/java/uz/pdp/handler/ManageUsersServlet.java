package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.UserDAO;
import uz.pdp.entity.User;
import uz.pdp.service.RoleService;

import java.io.IOException;
import java.util.List;

@WebServlet("/cabinet/manage-users")
public class ManageUsersServlet extends HttpServlet {
    UserDAO dao = UserDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<User> userList = dao.getAllUsers();
        req.setAttribute("userList", userList);
        req.setAttribute("roles", RoleService.getRoleList());

        String msg = (String) req.getSession().getAttribute("message");
        if (msg != null) {
            req.setAttribute("message", msg);
            req.getSession().removeAttribute("message");
        }

        req.getRequestDispatcher("/templates/manage-users.jsp").forward(req, resp);
    }
}
