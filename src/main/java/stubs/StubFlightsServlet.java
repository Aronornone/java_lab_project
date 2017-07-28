package stubs;

import pojo.Airplane;
import pojo.Flight;
import pojo.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

//Заглушка для страницы рейсов
@WebServlet(urlPatterns = {"/flights"})
public class StubFlightsServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute("user");

        //TODO: Здесь нужен запрос из БД списка рейсов по переданным параметрам!
        //Отсюда и ....
        Airplane airplane = new Airplane(1, "Airbus 320", 202, 23);
        Flight flight1 = new Flight(1, airplane, "12-D", "SPB", "MSK", 10052.30, 202, 23, LocalDateTime.of(2017, 07, 10, 23, 59));
        Flight flight2 = new Flight(2, airplane, "15-3", "MSK", "HEL", 11002.30, 202, 23, LocalDateTime.of(2017, 07, 15, 12, 48));
        Flight flight3 = new Flight(3, airplane, "15-5", "SPB", "HEL", 11002.30, 202, 23, LocalDateTime.of(2017, 07, 26, 12, 48));
        List<Flight> allFlights = new ArrayList<>();
        allFlights.add(flight1);
        allFlights.add(flight2);
        allFlights.add(flight3);
        Set<String> departureAirports = new HashSet<>();
        Set<String> arrivalAirports = new HashSet<>();
        for (Flight flight : allFlights) {
            departureAirports.add(flight.getDepartureAirport());
            arrivalAirports.add(flight.getArrivalAirport());
        }
        //...до этого момента код должен быть для получения списка всех рейсов + списка аэропортов из БД, пока заглушка

        request.setAttribute("departures", departureAirports);
        request.setAttribute("arrivals", arrivalAirports);

        //получаем установленные фильтры
        String dateFromString = request.getParameter("dateFrom");
        String dateToString = request.getParameter("dateTo");
        String departure = request.getParameter("selectedDeparture");
        String arrival = request.getParameter("selectedArrival");
        int numberTickets = Integer.parseInt(request.getParameter("numberTickets"));

        //проверяем дату перед парсингом
        if ((dateFromString.length()==0) || (dateToString.length()==0)) {
            //TODO: в Локализацию
            String insertDate = "Insert Date. Please try again.";
            request.setAttribute("insertDate", insertDate);
            request.getRequestDispatcher("/WEB-INF/pages/flights.jsp").forward(request, response);
        }
        LocalDate dateFrom = LocalDate.parse(dateFromString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate dateTo = LocalDate.parse(dateToString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        LocalDate dateToPlusDay = dateTo.plusDays(1);

        //TODO: Добавить в логгер информацию о поиске
        System.out.println("Searching for flight:" + dateFrom + " " + dateTo + " " + departure + " " + arrival + " " + numberTickets);

        //Формируем список подходящих рейсов
        List<Flight> foundFlights = new ArrayList<>();
        for (Flight flight : allFlights) {
            if ((flight.getArrivalAirport().equals(arrival)) && (flight.getDepartureAirport().equals(departure)) &&
                    ((flight.getAvailablePlacesEconom() + flight.getAvailablePlacesBusiness()) >= numberTickets) &&
                    ((flight.getDateTime().isAfter(dateFrom.atStartOfDay())) && flight.getDateTime().isBefore(dateToPlusDay.atStartOfDay()))) {
                foundFlights.add(flight);
            }
        }

        //если список рейсов пустой, предупреждаем
        if (foundFlights.isEmpty()) {
            //TODO: в Локализацию
            String nothingFound = "Nothing found. Please try again.";
            request.setAttribute("nothingFound", nothingFound);
        } else request.setAttribute("flights", foundFlights);

        //Сохраняем для следующего запроса количество билетов
        httpSession.setAttribute("numberTickets", numberTickets);

        request.getRequestDispatcher("/WEB-INF/pages/flights.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
