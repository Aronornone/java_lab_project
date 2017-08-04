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
import java.util.ResourceBundle;

//Заглушка для страницы рейсов
@WebServlet(urlPatterns = {"/doSearch"})
public class StubDoSearchServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");

        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute("user");

        List<Airplane> airplanes = StubUtils.getAirplanes();
        List<Airport> airports = StubUtils.getAirports();
        List<Flight> flights = StubUtils.getFlights(airports, airplanes);

        request.setAttribute("departures", airports);
        request.setAttribute("arrivals", airports);
        //получаем установленные фильтры

        String dateFromString = request.getParameter("dateFrom");
        String dateToString = request.getParameter("dateTo");
        String departure = request.getParameter("selectedDeparture");
        String arrival = request.getParameter("selectedArrival");
        String numberTicketsFilterString = request.getParameter("numberTicketsFilter");
        String[] checkbox = request.getParameterValues("box");

        //Сохраняем фильтры для следующих запросов в рамках этой же сессии
        httpSession.setAttribute("numberTicketsFilter", numberTicketsFilterString);
        httpSession.setAttribute("dateFrom", dateFromString);
        httpSession.setAttribute("dateTo", dateToString);
        httpSession.setAttribute("departureF", departure);
        httpSession.setAttribute("arrivalF", arrival);
        httpSession.setAttribute("business", checkbox);

        boolean business = false;
        if (checkbox != null) {
            if (checkbox[0].equals("business"))
                business = true;
        }

        //проверяем фильтры перед парсингом
        if ((dateFromString.isEmpty()) ||
                (dateToString.isEmpty()) ||
                departure.isEmpty() ||
                arrival.isEmpty() ||
                numberTicketsFilterString == null) {
            request.setAttribute("setFilters", err.getString("setFilters"));
            request.getRequestDispatcher("/WEB-INF/pages/flights.jsp").forward(request, response);
        } else {
            LocalDate dateFrom = LocalDate.parse(dateFromString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate dateTo = LocalDate.parse(dateToString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            LocalDate dateToPlusDay = dateTo.plusDays(1);
            int numberTicketsFilter = Integer.parseInt(numberTicketsFilterString);

            //TODO: Добавить в логгер информацию о поиске
            System.out.println("Searching for flight:" + dateFrom + " " + dateTo
                    + " " + departure + " " + arrival + " " + numberTicketsFilter
                    + checkbox);


            //Формируем список подходящих рейсов, TODO: надо сделать получением постранично!
            List<Flight> foundFlights = new ArrayList<>();
            for (Flight flight : flights) {
                if ((flight.getArrivalAirport().getCode().equals(arrival)) &&
                        (flight.getDepartureAirport().getCode().equals(departure)) &&
                        ((flight.getAvailablePlacesEconom() + flight.getAvailablePlacesBusiness()) >= numberTicketsFilter) &&
                        ((flight.getDateTime().isAfter(dateFrom.atStartOfDay())) &&
                                flight.getDateTime().isBefore(dateToPlusDay.atStartOfDay()))) {
                    foundFlights.add(flight);
                }
            }

            //если список рейсов пустой, предупреждаем
            if (foundFlights.isEmpty()) {
                request.setAttribute("nothingFound", err.getString("nothingFound"));
            } else request.setAttribute("flights", foundFlights);

            request.getRequestDispatcher("/WEB-INF/pages/flights.jsp").forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
