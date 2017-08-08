package controller;

import db.services.interfaces.AirportService;
import db.services.interfaces.FlightService;
import db.services.servicesimpl.AirportServiceImpl;
import db.services.servicesimpl.FlightServiceImpl;
import pojo.Airport;
import pojo.Flight;
import utils.PriceRecounter;
import utils.ServletUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;

import static java.lang.StrictMath.ceil;

@WebServlet(urlPatterns = {"/doSearch"})
public class DoSearchServlet extends HttpServlet {
    private static AirportService aps = new AirportServiceImpl();
    private static FlightService fl=new FlightServiceImpl();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");

        HttpSession httpSession = request.getSession();
        List<Airport> airports = aps.getAll();

        request.setAttribute("departures", airports);
        request.setAttribute("arrivals", airports);

        // получаем установленные фильтры
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
            Airport dep = new Airport();
            Airport arr = new Airport();
            for (Airport a : airports) {
                if (a.getCode().equals(departure)) {
                    dep = a;
                } else if (a.getCode().equals(arrival)) {
                    arr = a;
                }
            }

            //TODO: Добавить в логгер информацию о поиске
            System.out.println("Searching for flight:" + dateFrom + " " + dateTo
                    + " " + departure + " " + arrival + " " + numberTicketsFilter
                    + checkbox);

            Integer pageNum;
            try {
                pageNum = Integer.parseInt(request.getParameter("pageNum"));
                System.out.println("try + pagenum" +pageNum);
            } catch (NumberFormatException ex) {
                pageNum = 0;
                System.out.println("catch");
            }
            List<Flight> foundFlights = fl.getFlights(dep.getAirportId(), arr.getAirportId(),
                    dateFrom.toString(), dateToPlusDay.toString(), numberTicketsFilter, business, pageNum);
            for (Flight f : foundFlights) {
                f.setArrivalAirport(arr);
                f.setDepartureAirport(dep);
                f.setBaseCost(PriceRecounter.recountPrice(f.getBaseCost(), f.getDateTime(), business));
                httpSession.setAttribute("ticketCost", f.getBaseCost());
            }
            //if flight list is empty, show notification
            if (foundFlights.isEmpty()) {
                request.setAttribute("nothingFound", err.getString("nothingFound"));
            } else request.setAttribute("flights", foundFlights);

            int numPages = (int) ceil((double) fl.getAmountFlights(arr.getAirportId(), dep.getAirportId(), dateFrom.toString(),
                    dateToPlusDay.toString(), numberTicketsFilter, business) / 10);
            request.setAttribute("numPages", numPages);
            request.setAttribute("pageNum", pageNum);
            request.getRequestDispatcher("/WEB-INF/pages/flights.jsp").forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
