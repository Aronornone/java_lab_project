package stubs;

import db.service.UserService;
import pojo.Airport;
import pojo.User;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

//Заглушка для страницы поиска
@WebServlet(urlPatterns = {"", "/flights"})
public class StubFlightsServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();

        Cookie[] cookies = request.getCookies();
        SessionUtils.checkCookie(cookies, request, httpSession);

        httpSession.setAttribute("currentLocale", Locale.getDefault());
        getServletContext().setAttribute("errors", ResourceBundle.
                getBundle("ErrorsBundle", Locale.getDefault()));

        List<Airport> airports = StubUtils.getAirports();
        request.setAttribute("departures", airports);
        request.setAttribute("arrivals", airports);

        request.getRequestDispatcher("/WEB-INF/pages/flights.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
