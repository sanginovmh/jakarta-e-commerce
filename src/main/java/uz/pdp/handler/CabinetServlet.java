package uz.pdp.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import uz.pdp.dao.UserDAO;
import uz.pdp.entity.User;

import java.io.IOException;
import java.util.List;

@WebServlet("/cabinet")
public class CabinetServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getSession().removeAttribute("basketView");
        User user = (User) req.getSession().getAttribute("user");
        if (user == null) {
            req.setAttribute("message","You are not signed in.");
            req.getRequestDispatcher("/templates/signin.jsp").forward(req,resp);
            return;
        }

        req.setAttribute("fullName", user.getFullName());
        switch (user.getRole()) {
            case USER -> req.getRequestDispatcher("/templates/user-cabinet.jsp").forward(req, resp);
            case SELLER -> req.getRequestDispatcher("/templates/seller-cabinet.jsp").forward(req, resp);
            case ADMIN -> req.getRequestDispatcher("/templates/admin-cabinet.jsp").forward(req, resp);
        }
    }
}
