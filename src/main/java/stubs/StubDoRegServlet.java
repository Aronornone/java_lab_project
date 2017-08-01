package stubs;

import db.service.UserService;
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

import static utils.EncodingUtil.encode;

//Заглушка для страницы регистрации
@WebServlet(urlPatterns = "/doReg")
public class StubDoRegServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");

        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String nonHashedPasswordFirstReq = request.getParameter("password1");
        String nonHashedPasswordSecondReq = request.getParameter("password2");
        String password1HashReq = DigestUtils.md5Hex(nonHashedPasswordFirstReq);
        String password2HashReq = DigestUtils.md5Hex(nonHashedPasswordSecondReq);
        LocalDateTime registrationDate = LocalDateTime.now();

        if (nonHashedPasswordFirstReq.isEmpty() || nonHashedPasswordSecondReq.isEmpty() || username.isEmpty()) {
            request.setAttribute("fieldEmpty", encode(err.getString("fieldEmpty")));
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(request, response);
        } else if (!password1HashReq.equals(password2HashReq)) {
            request.setAttribute("passMismatch", encode(err.getString("passMismatch")));
            request.setAttribute("email", email);
            request.setAttribute("username", username);
            request.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(request, response);
        } else {
            UserService userService = new UserService();
            Optional<User> userOptional = userService.get(email);
            if (userOptional.isPresent()) {
                request.setAttribute("userAlreadyExists", encode(err.getString("userAlreadyExists")));
                request.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(request, response);
            } else {
                User user = new User(username, email, password1HashReq, registrationDate);
                userService.create(user);
                request.setAttribute("regSuccess", encode(err.getString("regSuccess")));
                request.setAttribute("email", email);
                request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
