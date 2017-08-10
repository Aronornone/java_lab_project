package controller;

import com.google.gson.Gson;
import db.services.interfaces.AirportService;
import db.services.interfaces.FlightService;
import db.services.interfaces.TicketService;
import db.services.servicesimpl.AirportServiceImpl;
import db.services.servicesimpl.FlightServiceImpl;
import db.services.servicesimpl.TicketServiceImpl;
import org.apache.log4j.Logger;
import pojo.Airport;
import pojo.Flight;

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
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.StrictMath.ceil;

@WebServlet(urlPatterns = {"/doSearch"})
public class DoSearchServlet extends HttpServlet {
    private static Logger log = Logger.getLogger("servLog");
    private static AirportService airportService;
    private static FlightService flightService;
    private static TicketService ticketService;

    public void init() {
        log.info("init(): Initializing 'airportService', 'flightService', 'ticketService'.");
        airportService = AirportServiceImpl.getInstance();
        flightService = FlightServiceImpl.getInstance();
        ticketService = TicketServiceImpl.getInstance();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doGet(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");

        log.info("doGet(request, response): Getting a list of all airports from 'airportService'.");
        HttpSession httpSession = request.getSession();
        List<Airport> airports = airportService.getAll();

        request.setAttribute("departures", airports);
        request.setAttribute("arrivals", airports);

        // получаем установленные фильтры
        log.info("doGet(request, response): Receiving setup filters.");
        String dateFromString = request.getParameter("dateFrom");
        String dateToString = request.getParameter("dateTo");
        String departure = request.getParameter("selectedDeparture");
        String arrival = request.getParameter("selectedArrival");
        String numberTicketsFilterString = request.getParameter("numberTicketsFilter");
        String[] checkbox = request.getParameterValues("box");

        //Сохраняем фильтры для следующих запросов в рамках этой же сессии
        log.info("doGet(request, response): Saving filters for future requests.");
        httpSession.setAttribute("numberTicketsFilter", numberTicketsFilterString);
        httpSession.setAttribute("dateFrom", dateFromString);
        httpSession.setAttribute("dateTo", dateToString);
        httpSession.setAttribute("departureF", departure);
        httpSession.setAttribute("arrivalF", arrival);
        httpSession.setAttribute("business", checkbox);

        boolean business = false;
        if (checkbox != null) {
            if (checkbox[0].equals("business")) {
                business = true;
            }
        }

        //проверяем фильтры перед парсингом
        log.info("doGet(request, response): Checking filters before parcing.");
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
            Airport dep = new Airport();
            Airport arr = new Airport();
            for (Airport a : airports) {
                if (a.getCode().equals(departure)) {
                    dep = a;
                } else if (a.getCode().equals(arrival)) {
                    arr = a;
                }
            }

            log.info("doGet(request, response): Searching for flight:" + dateFrom + " " + dateTo + " " +
                    departure + " " + arrival + " " + numberTicketsFilter + " " + Arrays.toString(checkbox));

            Integer pageNum;
            try {
                pageNum = Integer.parseInt(request.getParameter("page"));
            } catch (NumberFormatException ex) {
                pageNum = -1;
            }
            httpSession.setAttribute("pageLast", pageNum);

            boolean pageFirst = false;
            if (pageNum == -1) {
                pageNum = 0;
                pageFirst = true;
            }
            log.info("doGet(request, response): Creating a list of found flights.");
            List<Flight> foundFlights = flightService.getFlights(dep.getAirportId(), arr.getAirportId(),
                    dateFrom.toString(), dateToPlusDay.toString(), numberTicketsFilter, business, pageNum);
            for (Flight f : foundFlights) {
                f.setArrivalAirport(arr);
                f.setDepartureAirport(dep);
                f.setBaseCost(ticketService.recountPrice(f.getBaseCost(), f.getDateTime(), business));
                httpSession.setAttribute("ticketCost", f.getBaseCost());
            }

            //if flight list is empty, show notification
            if (foundFlights.isEmpty()) {
                log.info("doGet(request, response): 'foundFlights' is empty.");
                request.setAttribute("nothingFound", err.getString("nothingFound"));
                request.getRequestDispatcher("/WEB-INF/pages/flights.jsp").forward(request, response);
            } else if (pageFirst) { // if first page, foundFlights are sent as attribute, also number of found pages is sent
                request.setAttribute("flights", new ArrayList<Flight>());
                int numPages = (int) ceil((double) flightService.getAmountFlights(arr.getAirportId(), dep.getAirportId(), dateFrom.toString(),
                        dateToPlusDay.toString(), numberTicketsFilter, business) / 10);
                request.setAttribute("numPages", numPages);
                request.setAttribute("ifPageFirst",pageFirst);

                request.getRequestDispatcher("").forward(request, response);
            } else { // if its not first page&foundFlights is not empty, foundFlights is sent as json in response
                log.info("doGet(request, response): 'pageNum' is greater then 0. Creating a json string and send it as a response.");
                String json = new Gson().toJson(foundFlights);
                response.setContentType("json");
                response.setCharacterEncoding("UTF-8");
                response.getWriter().write(json);
            }
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doPost(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        doGet(request, response);
    }
}
