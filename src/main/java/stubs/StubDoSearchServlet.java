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
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//Заглушка для страницы рейсов
@WebServlet(urlPatterns = {"/doSearch"})
public class StubDoSearchServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute("user");

        List<Airport> airports = StubUtils.getAirports();
        List<Airplane> airplanes = StubUtils.getAirplanes();
        List<Flight> flights = StubUtils.getFlights(airports,airplanes);

        request.setAttribute("departures", airports);
        request.setAttribute("arrivals", airports);
        //получаем установленные фильтры
        String dateFromString = request.getParameter("dateFrom");
        String dateToString = request.getParameter("dateTo");
        String departure = request.getParameter("selectedDeparture");
        String arrival = request.getParameter("selectedArrival");
        int numberTickets = Integer.parseInt(request.getParameter("numberTickets"));

        //проверяем дату перед парсингом
        if ((dateFromString.length() == 0) || (dateToString.length() == 0) || departure.isEmpty() || arrival.isEmpty() || numberTickets == 0) {
            //TODO: в Локализацию
            String insertFilters = "Insert Date and Airports. Please try again.";
            request.setAttribute("insertFilters", insertFilters);
            request.getRequestDispatcher("/WEB-INF/pages/flights.jsp").forward(request, response);
        }
        else {
            LocalDate dateFrom = LocalDate.parse(dateFromString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate dateTo = LocalDate.parse(dateToString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate dateToPlusDay = dateTo.plusDays(1);

            //TODO: Добавить в логгер информацию о поиске
            System.out.println("Searching for flight:" + dateFrom + " " + dateTo + " " + departure + " " + arrival + " " + numberTickets);

            //Формируем список подходящих рейсов, TODO: надо сделать получением постранично!
            List<Flight> foundFlights = new ArrayList<>();
            for (Flight flight : flights) {
                if ((flight.getArrivalAirport().getName().equals(arrival)) && (flight.getDepartureAirport().getName().equals(departure)) &&
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
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
