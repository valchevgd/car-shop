package web;

import org.modelmapper.ModelMapper;
import services.UserService;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/users/register")
public class UsersRegisterServlet extends HttpServlet {

    private final UserService userService;
    private final ModelMapper mapper;

    @Inject
    public UsersRegisterServlet(UserService userService, ModelMapper modelMapper) {
        this.userService = userService;
        this.mapper = modelMapper;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        req.getRequestDispatcher("/users-register.jsp")
                .forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String username = req.getParameter("username");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");
        String error = "";

        try {
            userService.register(username, email, password, confirmPassword);
        } catch (IllegalArgumentException e) {
            error = e.getMessage();

            resp.sendRedirect("/users/register?message=" + error);
        }

        if (error.equals("")) {

            resp.sendRedirect("/users/login");
        }
    }
}
