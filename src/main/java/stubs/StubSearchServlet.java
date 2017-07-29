package stubs;

import db.DataSource;
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
import java.util.ArrayList;
import java.util.List;

//Заглушка для страницы поиска
@WebServlet(urlPatterns = {"/emptySearch"})
public class StubSearchServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User(1, "UserName XXX1", "@mail.ru", "123452Afr3", LocalDateTime.now());

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("user", user);

        //TODO: Здесь нужен запрос из БД сета departure и arrival для фильтров!
        //Отсюда и ....
        List<String> airports = new ArrayList<>();
        Connection connection = DataSource.getConnection();
        try {
            String sql = "SELECT name,city FROM airport";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                airports.add(result.getString("name") + " (" + result.getString("city") +")");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        //...до этого момента код должен быть для получения списка аэропортов из БД, пока заглушка

        request.setAttribute("departures", airports);
        request.setAttribute("arrivals", airports);

        request.getRequestDispatcher("/WEB-INF/pages/flights.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
