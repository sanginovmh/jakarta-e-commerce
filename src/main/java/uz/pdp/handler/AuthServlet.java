package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.UserDAO;
import uz.pdp.entity.User;
import uz.pdp.entity.enums.Role;
import uz.pdp.util.Base64Util;

import java.io.IOException;
import java.util.Optional;
import java.util.Random;

@WebServlet("/auth/signup")
public class AuthServlet extends HttpServlet {
    UserDAO dao = UserDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/templates/auth.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        if (checkUserIfExists(email)) {
            req.setAttribute("message", "User with this email already exists: %s".formatted(email));
            req.getRequestDispatcher("/templates/auth.jsp").forward(req, resp);
            return;
        }
        String password = req.getParameter("password");

        User user = new User();
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPassword(Base64Util.encode(password));
        user.setRole(Role.USER);
        user.setEnabled(new Random().nextBoolean());
        dao.saveUser(user);

        req.setAttribute("message", "Successfully signed up.");
        req.getRequestDispatcher("/templates/auth.jsp").forward(req, resp);
    }

    private boolean checkUserIfExists(String email) {
        Optional<User> optionalUser = dao.findByEmail(email);
        return optionalUser.isPresent();
    }
}
