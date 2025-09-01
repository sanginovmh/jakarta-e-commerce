package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.UserDAO;
import uz.pdp.entity.User;
import uz.pdp.util.Base64Util;

import java.io.IOException;
import java.util.Optional;

@WebServlet("/auth/signin")
public class SignInServlet extends HttpServlet {
    UserDAO dao = UserDAO.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/templates/signin.jsp").forward(req, resp);
    }

    @Override
    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        Optional<User> userByEmail = dao.findByEmail(email);
        if (userByEmail.isEmpty()) {
            req.setAttribute("message", "Bad credentials.");
            req.getRequestDispatcher("/templates/signin.jsp").forward(req, resp);
            return;
        }

        User user = userByEmail.get();
        String password = Base64Util.encode(req.getParameter("password"));
        if (password.equals(user.getPassword())) {
            req.getSession().setAttribute("user", user); // Set user attribute to be used for /cabinet
            String uAndP = user.getEmail() + ":" + user.getPassword();
            String encodedIdentity = Base64Util.encode(uAndP);
            resp.addCookie(new Cookie("identity", encodedIdentity));

            req.getSession().removeAttribute("message");
            resp.sendRedirect(req.getContextPath() + "/cabinet");
        }
        else {
            req.setAttribute("message", "Bad Credentials.");
            req.getRequestDispatcher("/templates/signin.jsp").forward(req, resp);
        }
    }
}
