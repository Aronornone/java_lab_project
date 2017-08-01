package stubs;

import db.DataSource;
import org.apache.commons.codec.digest.DigestUtils;
import pojo.Airport;
import pojo.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.ResourceBundle;

import static utils.EncodingUtil.encode;

//Заглушка для страницы логина
@WebServlet(urlPatterns = {"/doLogin"})
public class StubDoLoginServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");

        String email = request.getParameter("email");
        String nonHashedPasswordReq = request.getParameter("password");
        String passwordHashReq = DigestUtils.md5Hex(nonHashedPasswordReq);

        Connection connection = DataSource.getConnection();
        String usernameDB = "";
        String passwordHashDB = "";
        Long userId;
        LocalDateTime registrationDate;
        User user = null;

        //TODO: этот кусок вынести в DAOimpl, кроме установки attribute и dispatcher
        try {
            String sql = "SELECT * FROM user WHERE user.email=?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, email);
            ResultSet result = statement.executeQuery();
            if (result.next()) {
                userId = Long.parseLong(result.getString("id"));
                usernameDB = result.getString("name");
                passwordHashDB = result.getString("password_hash");
                registrationDate = result.getTimestamp("registration_date").toLocalDateTime();
                user = new User(userId, usernameDB, email, passwordHashReq, registrationDate);
            } else {
                request.setAttribute("nonexistentLogin", encode(err.getString("nonexistentLogin")));
                request.setAttribute("email", email);
                request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (passwordHashDB.equals(passwordHashReq)) {
            HttpSession httpSession = request.getSession();
            httpSession.setAttribute("user", user);
            List<Airport> airports = StubUtils.getAirports();
            request.setAttribute("departures", airports);
            request.setAttribute("arrivals", airports);
            request.getRequestDispatcher("/WEB-INF/pages/flights.jsp").forward(request, response);
        } else {
            request.setAttribute("loginFailed", encode(err.getString("loginFailed")));
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
