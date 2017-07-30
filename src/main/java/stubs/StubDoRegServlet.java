package stubs;

import db.DataSource;
import org.apache.commons.codec.digest.DigestUtils;
import pojo.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.time.LocalDateTime;

//Заглушка для страницы регистрации
@WebServlet(urlPatterns = "/doReg")
public class StubDoRegServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String nonHashedPasswordFirstReq = request.getParameter("password1");
        String nonHashedPasswordSecondReq = request.getParameter("password2");
        String password1HashReq = DigestUtils.md5Hex(nonHashedPasswordFirstReq);
        String password2HashReq = DigestUtils.md5Hex(nonHashedPasswordSecondReq);
        LocalDateTime registrationDate = LocalDateTime.now();

        if (nonHashedPasswordFirstReq.isEmpty() || nonHashedPasswordSecondReq.isEmpty() || username.isEmpty()) {
            request.setAttribute("passwordsEmpty", "Passwords and username mustn't be empty! Please, check.");
            request.setAttribute("email", email);
            request.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(request, response);
        } else if (!password1HashReq.equals(password2HashReq)) {
            request.setAttribute("passwordsNotEqual", "Passwords are not equal! Please, check.");
            request.setAttribute("email", email);
            request.setAttribute("username", username);
            request.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(request, response);
        } else {
            //TODO: этот кусок вынести в DAOimpl, кроме установки attribute и dispatcher
            Connection connection = DataSource.getConnection();
            try {
                String sql = "SELECT user.name,user.email,user.password_hash FROM user WHERE user.email=?";
                PreparedStatement statementSelect = connection.prepareStatement(sql);
                statementSelect.setString(1, email);
                ResultSet result = statementSelect.executeQuery();
                if (result.next()) {
                    request.setAttribute("userAlreadyExist", "User already exist. Please, login");
                    request.getRequestDispatcher("/WEB-INF/pages/registration.jsp").forward(request, response);
                } else {
                    User user = new User(username, email, password1HashReq, registrationDate);
                    try {
                        String sqlInsert = "INSERT INTO user (name, email, password_hash, registration_date) VALUES(?,?,?,?)";
                        PreparedStatement statementInsert = connection.prepareStatement(sqlInsert);
                        statementInsert.setString(1, username);
                        statementInsert.setString(2, email);
                        statementInsert.setString(3, password1HashReq);
                        statementInsert.setTimestamp(4, Timestamp.valueOf(registrationDate));

                        statementInsert.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                    request.setAttribute("regSuccess", "Registration successful! Please, login");
                    request.setAttribute("email", email);
                    request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }


    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
