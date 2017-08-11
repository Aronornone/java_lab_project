package controller;

import com.google.gson.Gson;
import org.apache.log4j.Logger;
import pojo.Airport;
import pojo.Flight;
import services.interfaces.AirportService;
import services.interfaces.FlightService;
import services.interfaces.TicketService;
import services.servicesimpl.AirportServiceImpl;
import services.servicesimpl.FlightServiceImpl;
import services.servicesimpl.TicketServiceImpl;

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

/**
 * Servlet for search logic in base of flights
 */
@WebServlet(urlPatterns = {"/doSearch"})
public class DoSearchServlet extends HttpServlet {
    private static Logger log = Logger.getLogger("servletLogger");
    private static AirportService airportService;
    private static FlightService flightService;
    private static TicketService ticketService;
    private String dateFromString;
    private String dateToString;
    private String departure;
    private String arrival;
    private String numberTicketsFilterString;
    private String[] checkbox;
    private List<Flight> foundFlights;
    private List<Airport> airports;

    private Airport dep, arr;
    private LocalDate dateFrom, dateTo, dateToPlusDay;
    private boolean business = false;
    private boolean pageFirst ;
    private int numberTicketsFilter;
    private Integer pageNum;

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

        //get list of airports needed for filters
        airports = airportService.getAll();
        request.setAttribute("departures", airports);
        request.setAttribute("arrivals", airports);
        getFilters(request);
        saveFilters(httpSession);
        log.info("doGet(request, response): Checking filters before parsing.");
        if (filtersEmpty()) {
            notifyFiltersEmpty(request, response, err);
            return;
        }
        parseFilters();
        log.info("doGet(request, response): Searching for flight:" + dateFrom + " " + dateTo + " " +
                departure + " " + arrival + " " + numberTicketsFilter + " " + Arrays.toString(checkbox));

        pageFirst= false;
        setPageNum(request, httpSession);
        checkIfFirstPage();
        initFoundFlights(httpSession);

        //logic for view is here
        if (foundFlights.isEmpty()) {
            notifyNothingFound(request, response, err);
            return;
        }
        if (pageFirst) {
            forwardFirstPage(request, response, pageFirst);
            return;
        }
        respondJSON(response);
    }

    private void checkIfFirstPage() {
        if (pageNum == -1) {
            pageNum = 0;
            pageFirst = true;
        }
    }

    private void setPageNum(HttpServletRequest request, HttpSession httpSession) {
        try {
            pageNum = Integer.parseInt(request.getParameter("page"));
        } catch (NumberFormatException ex) {
            pageNum = -1;
        }
        httpSession.setAttribute("pageLast", pageNum);
    }

    private void notifyFiltersEmpty(HttpServletRequest request, HttpServletResponse response, ResourceBundle err) throws ServletException, IOException {
        request.setAttribute("setFilters", err.getString("setFilters"));
        request.getRequestDispatcher("/WEB-INF/pages/flights.jsp").forward(request, response);
    }

    private void setBusiness() {
        if (checkbox != null) {
            if (checkbox[0].equals("business")) {
                business = true;
            }
        }
    }

    private void notifyNothingFound(HttpServletRequest request, HttpServletResponse response, ResourceBundle err) throws ServletException, IOException {
        log.error("doGet(request, response): Flights not found!");
        request.setAttribute("nothingFound", err.getString("nothingFound"));
        request.getRequestDispatcher("/WEB-INF/pages/flights.jsp").forward(request, response);
    }

    private void respondJSON(HttpServletResponse response) throws IOException {
        log.info("doGet(request, response): 'pageNum' is greater then 0. Creating a json string and sending it as a response.");
        String json = new Gson().toJson(foundFlights);
        response.setContentType("json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json);
    }

    private void forwardFirstPage(HttpServletRequest request, HttpServletResponse response, boolean pageFirst) throws ServletException, IOException {
        request.setAttribute("flights", new ArrayList<Flight>());
        int numPages = getNumPages(numberTicketsFilter);
        request.setAttribute("numPages", numPages);
        request.setAttribute("ifPageFirst", pageFirst);
        request.getRequestDispatcher("").forward(request, response);
    }

    private void parseFilters() {
        setBusiness();
        parseDates();
        numberTicketsFilter = Integer.parseInt(numberTicketsFilterString);
        dep = new Airport();
        arr = new Airport();
        for (Airport a : airports) {
            if (a.getCode().equals(departure)) {
                dep = a;
            } else if (a.getCode().equals(arrival)) {
                arr = a;
            }
        }
    }

    private int getNumPages(int numberTicketsFilter) {
        return (int) ceil((double) flightService.getAmountFlights(arr.getAirportId(), dep.getAirportId(), dateFrom.toString(),
                dateToPlusDay.toString(), numberTicketsFilter, business) / 10);
    }

    private void initFoundFlights(HttpSession httpSession) {
        log.info("doGet(request, response): Creating a list of found flights.");
        foundFlights = flightService.getFlights(dep.getAirportId(), arr.getAirportId(),
                dateFrom.toString(), dateToPlusDay.toString(), numberTicketsFilter, business, pageNum);
        for (Flight f : foundFlights) {
            f.setArrivalAirport(arr);
            f.setDepartureAirport(dep);
            f.setBaseCost(ticketService.recountPrice(f.getBaseCost(), f.getDateTime(), business));
            httpSession.setAttribute("ticketCost", f.getBaseCost());
        }
    }

    private void parseDates() {
        dateFrom = LocalDate.parse(dateFromString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dateTo = LocalDate.parse(dateToString, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        dateToPlusDay = dateTo.plusDays(1);
    }

    private boolean filtersEmpty() {
        return (dateFromString.isEmpty()||
                dateToString.isEmpty() ||
                departure.isEmpty() ||
                arrival.isEmpty() ||
                numberTicketsFilterString == null);
    }

    private void getFilters(HttpServletRequest request) {
        log.info("doGet(request, response): Receiving setup filters.");
        dateFromString = request.getParameter("dateFrom");
        dateToString = request.getParameter("dateTo");
        departure = request.getParameter("selectedDeparture");
        arrival = request.getParameter("selectedArrival");
        numberTicketsFilterString = request.getParameter("numberTicketsFilter");
        checkbox = request.getParameterValues("box");
    }

    private void saveFilters(HttpSession httpSession) {
        log.info("doGet(request, response): Saving filters for future requests.");
        httpSession.setAttribute("numberTicketsFilter", numberTicketsFilterString);
        httpSession.setAttribute("dateFrom", dateFromString);
        httpSession.setAttribute("dateTo", dateToString);
        httpSession.setAttribute("departureF", departure);
        httpSession.setAttribute("arrivalF", arrival);
        httpSession.setAttribute("business", checkbox);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doPost(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        doGet(request, response);
    }
}
