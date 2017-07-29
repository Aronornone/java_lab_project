package stubs;

import db.DataSource;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

//Заглушка для страницы рейсов
@WebServlet(urlPatterns = {"/flights"})
public class StubFlightsServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute("user");

        //TODO: Здесь нужен запрос из БД списка рейсов по переданным параметрам!
        //Отсюда и ....

        List<Airport> airportsList = new ArrayList<>();
        Connection connection = DataSource.getConnection();
        try {
            String sql = "SELECT * FROM airport";
            PreparedStatement statement = connection.prepareStatement(sql);
            ResultSet result = statement.executeQuery();
            while(result.next()) {
                airportsList.add(new Airport(Long.parseLong(result.getString("id")),result.getString("name"),result.getString("city")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        List<String> airports = new ArrayList<>();
        for(Airport airport:airportsList) {
            airports.add(airport.getName()+ " (" + airport.getCity()+")");
        }
        request.setAttribute("departures", airports);
        request.setAttribute("arrivals", airports);

        Airplane airplane = new Airplane(1, "Airbus 320", 202, 23);
        Airport airport1 = airportsList.get(686);
        Airport airport2 = airportsList.get(326);
        Airport airport3 = airportsList.get(510);
        Flight flight1 = new Flight(1, airplane, "12-D", airport1, airport2, 10052.30, 202, 23, LocalDateTime.of(2017, 7, 10, 23, 59));
        Flight flight2 = new Flight(2, airplane, "15-3", airport2, airport3, 11002.30, 202, 23, LocalDateTime.of(2017, 7, 15, 12, 48));
        Flight flight3 = new Flight(3, airplane, "15-5", airport1, airport3, 11002.30, 202, 23, LocalDateTime.of(2017, 7, 26, 12, 48));
        List<Flight> allFlights = new ArrayList<>();
        allFlights.add(flight1);
        allFlights.add(flight2);
        allFlights.add(flight3);


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

        //Формируем список подходящих рейсов, TODO: надо сделать получением постранично!
        List<Flight> foundFlights = new ArrayList<>();
        for (Flight flight : allFlights) {
            if ((flight.getArrivalAirport().getName().equals(arrival.substring(0,3))) && (flight.getDepartureAirport().getName().equals(departure.substring(0,3))) &&
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
