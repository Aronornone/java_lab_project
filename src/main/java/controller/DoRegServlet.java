package controller;

import db.services.interfaces.UserService;
import db.services.servicesimpl.UserServiceImpl;
import org.apache.commons.codec.digest.DigestUtils;
import pojo.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

@WebServlet(urlPatterns = "/doReg")
public class DoRegServlet extends HttpServlet {
    private static UserService userService;

    public void init() {
        userService = UserServiceImpl.getInstance();
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");

        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String nonHashedPasswordFirstReq = request.getParameter("password1");
        String nonHashedPasswordSecondReq = request.getParameter("password2");
        String password1HashReq = "";
        String password2HashReq = "";
        LocalDateTime registrationDate = LocalDateTime.now();

        if (nonHashedPasswordFirstReq==null ||
                nonHashedPasswordSecondReq==null ||
                username==null ||
                email ==null ||
                nonHashedPasswordFirstReq.isEmpty() ||
                nonHashedPasswordSecondReq.isEmpty() ||
                username.isEmpty() ||
               email.isEmpty()) {
            request.setAttribute("fieldEmpty", err.getString("fieldEmpty"));
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(request, response);
        }
        else {
            password1HashReq = DigestUtils.md5Hex(nonHashedPasswordFirstReq);
            password2HashReq = DigestUtils.md5Hex(nonHashedPasswordSecondReq);

            if (!password1HashReq.equals(password2HashReq)) {
                request.setAttribute("passMismatch", err.getString("passMismatch"));
                request.setAttribute("email", email);
                request.setAttribute("username", username);
                request.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(request, response);
            } else {
                Optional<User> userOptional = userService.get(email);
                if (userOptional.isPresent()) {
                    request.setAttribute("userAlreadyExists", err.getString("userAlreadyExists"));
                    request.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(request, response);
                } else {
                    User user = new User(username, email, password1HashReq, registrationDate);
                    userService.add(user);
                    request.setAttribute("regSuccess", err.getString("regSuccess"));
                    request.setAttribute("email", email);
                    request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
                }
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
