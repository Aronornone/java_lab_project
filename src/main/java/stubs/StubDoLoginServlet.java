package stubs;

import db.service.UserService;
import org.apache.commons.codec.digest.DigestUtils;
import pojo.Airport;
import pojo.User;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

//Заглушка для страницы логина
@WebServlet(urlPatterns = {"/doLogin"})
public class StubDoLoginServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");

        String email = request.getParameter("email");
        String nonHashedPasswordReq = request.getParameter("password");
        String passwordHashReq = DigestUtils.md5Hex(nonHashedPasswordReq);
        String passwordHashDB = "";
        User user = null;

        UserService userService = new UserService();
        Optional<User> userOptional = userService.get(email);
        if (userOptional.isPresent()) {
            user = userOptional.get();
            passwordHashDB = user.getPasswordHash();
        } else {
            request.setAttribute("nonexistentLogin", err.getString("nonexistentLogin"));
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
        }

        if (passwordHashDB.equals(passwordHashReq)) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("user", user);

            Cookie cookieUserId;
            cookieUserId = new Cookie("userId", String.valueOf(user.getUserId()));
            cookieUserId.setMaxAge(60 * 60 * 24 * 7); //one week
            response.addCookie(cookieUserId);

            List<Airport> airports = StubUtils.getAirports();
            request.setAttribute("departures", airports);
            request.setAttribute("arrivals", airports);
            request.getRequestDispatcher("/WEB-INF/pages/flights.jsp").forward(request, response);
        } else {
            request.setAttribute("loginFailed", err.getString("loginFailed"));
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
