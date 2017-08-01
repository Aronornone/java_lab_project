package stubs;

import db.service.TicketService;
import pojo.*;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

//Заглушка для страницы корзины
@WebServlet(urlPatterns = {"/bucket"})
public class StubBucketServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");
        HttpSession httpSession = request.getSession();
        Cookie[] cookies = request.getCookies();
        SessionUtils.checkCookie(cookies, request, httpSession);
        User user = (User) httpSession.getAttribute("user");

        String flightIdString = SessionUtils.checkFlightSession(httpSession, request);

        if (flightIdString != null) {
            Long flightId = Long.parseLong(flightIdString);
            httpSession.setAttribute("flightId", flightId);

            List<Airplane> airplanes = StubUtils.getAirplanes();
            List<Airport> airports = StubUtils.getAirports();
            List<Flight> flights = StubUtils.getFlights(airports, airplanes);

            for (Flight flight : flights) {
                if (flight.getFlightId() == flightId) {
                    Flight ticketFlight = flight;
                    request.setAttribute("flightNumber", ticketFlight.getFlightNumber());
                    request.setAttribute("departureAirport", ticketFlight.getDepartureAirport());
                    request.setAttribute("arrivalAirport", ticketFlight.getArrivalAirport());
                    request.setAttribute("dateTime", ticketFlight.getDateTime());
                    request.setAttribute("airplanename", ticketFlight.getAirplane().getName());
                }
            }
        }

        String numberTicketsFilterString = (String) httpSession.getAttribute("numberTicketsFilter");
        int numberTicketsFilter;
        if (numberTicketsFilterString == null) {
            numberTicketsFilter = 0;
            request.setAttribute("cartEmpty", err.getString("cartEmpty"));
        } else {
            numberTicketsFilter = Integer.parseInt(numberTicketsFilterString);
        }

        double sumTotal = 0;
        TicketService ticketService = new TicketService();
//        List<Ticket> tickets = ticketService.getFlightTickets(invoice,flight);
        List<Ticket> tickets = new ArrayList<>();
        for (Ticket ticket : tickets) {
            sumTotal = sumTotal + ticket.getPrice();
        }

        request.setAttribute("tickets", tickets);
        request.setAttribute("totalSum", sumTotal);

        request.getRequestDispatcher("/WEB-INF/pages/bucket.jsp").

                forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
