package stubs;

import pojo.Airplane;
import pojo.Airport;
import pojo.Flight;
import pojo.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//Заглушка для страницы поиска
@WebServlet(urlPatterns = {"/emptySearch"})
public class StubSearchServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = new User(1, "UserName XXX1", "@mail.ru", "123452Afr3", LocalDateTime.now());

        HttpSession httpSession = request.getSession();
        httpSession.setAttribute("user", user);

        //TODO: Здесь нужен запрос из БД сета departure и arrival для фильтров!
        //Отсюда и ....
        Airplane airplane = new Airplane(1, "Airbus 320", 202, 23);
        Airport SPB = new Airport(1,"SPB","Saint-Petersburg");
        Airport MSK = new Airport(2,"MSK","Moscow");
        Airport HEL = new Airport(3,"HEL","Helsinki");
        Flight flight1 = new Flight(1, airplane, "12-D", SPB, MSK, 10052.30, 202, 23, LocalDateTime.of(2017, 7, 10, 23, 59));
        Flight flight2 = new Flight(2, airplane, "15-3", MSK, HEL, 11002.30, 202, 23, LocalDateTime.of(2017, 7, 15, 12, 48));
        Flight flight3 = new Flight(3, airplane, "15-5", SPB, HEL, 11002.30, 202, 23, LocalDateTime.of(2017, 7, 26, 12, 48));
        List<Flight> flights = new ArrayList<>();
        flights.add(flight1);
        flights.add(flight2);
        flights.add(flight3);

        Set<String> departureAirports = new HashSet<>();
        Set<String> arrivalAirports = new HashSet<>();
        for (Flight flight : flights) {
            departureAirports.add(flight.getDepartureAirport().getName() +" (" + flight.getDepartureAirport().getCity()+ ")");
            arrivalAirports.add(flight.getArrivalAirport().getName()+" (" + flight.getArrivalAirport().getCity()+ ")");
        }
        //...до этого момента код должен быть для получения списка аэропортов из БД, пока заглушка

        request.setAttribute("departures", departureAirports);
        request.setAttribute("arrivals", arrivalAirports);

        request.getRequestDispatcher("/WEB-INF/pages/flights.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
