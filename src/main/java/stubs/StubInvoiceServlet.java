package stubs;

import db.service.FlightService;
import db.service.InvoiceService;
import db.service.TicketService;
import pojo.Flight;
import pojo.Invoice;
import pojo.Ticket;
import pojo.User;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import static utils.EncodingUtil.encode;

//Заглушка для страницы корзины
@WebServlet(urlPatterns = {"/addFlightToInvoice"})
public class StubInvoiceServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");
        HttpSession httpSession = request.getSession();
        Cookie[] cookies = request.getCookies();
        SessionUtils.checkCookie(cookies, request, httpSession);
        User user = (User) httpSession.getAttribute("user");

        String dateFromString = (String) httpSession.getAttribute("dateFrom");
        String dateToString = (String) httpSession.getAttribute("dateTo");
        String departure = (String) httpSession.getAttribute("departureF");
        String arrival = (String) httpSession.getAttribute("arrivalF");
        String numberTicketsFilterString = (String) httpSession.getAttribute("numberTicketsFilter");

        String redirectBackString = "/doSearch?dateFrom=" + dateFromString + "&dateTo=" + dateToString +
                "&selectedDeparture=" + departure + "&selectedArrival=" + arrival +
                "&numberTicketsFilter=" + numberTicketsFilterString;

        if (user == null) {
            //заглушка, будет еще предупреждение, что нужно сначала войти + сохранение выбранных фильтров
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
        }
        int numberTicketsFlight;
        String numberTicketsFlightString = request.getParameter("numberTicketsFlight");
        numberTicketsFlight = Integer.parseInt(numberTicketsFlightString);

        String flightIdString = request.getParameter("flightId");
        Flight flight = null;
        FlightService flightService = new FlightService();
        Optional<Flight> flightOptional = flightService.get(Integer.parseInt(flightIdString));
        if (flightOptional.isPresent()) {
            flight = flightOptional.get();
        }

        Invoice invoice;
        InvoiceService invoiceService = new InvoiceService();

        if (numberTicketsFlight > (flight.getAvailablePlacesBusiness() + flight.getAvailablePlacesEconom())) {
            request.setAttribute("notEnoughPlaces", encode(err.getString("notEnoughPlaces")));
            request.getRequestDispatcher(redirectBackString).forward(request, response);
        } else {
            Optional<Invoice> invoiceOptional = invoiceService.getInvoiceByUser(user.getUserId(),
                    Invoice.InvoiceStatus.CREATED);
            if (invoiceOptional.isPresent()) {
                invoice = invoiceOptional.get();
            } else {
                invoice = new Invoice(user, Invoice.InvoiceStatus.CREATED, LocalDateTime.now());
                invoiceService.create(invoice);
            }

            int ticketsInBucket = 0;

            if (httpSession.getAttribute("ticketsInBucket") != null) {
                ticketsInBucket = (int) httpSession.getAttribute("ticketsInBucket");
            }

            TicketService ticketService = new TicketService();

            List<Ticket> tickets = new ArrayList<>();
            if (numberTicketsFlight != 0) {
                System.out.println("number of tickets to buy: " + numberTicketsFlight);
                for (int i = 0; i < numberTicketsFlight; i++) {
                    //request for available places and reserve of them
                    int sittingPlace = StubUtils.randomSittingPlace(flight.getFlightId(), false);
                    //new Ticket to DB
                    Ticket ticket = new Ticket(invoice, flight, "", "", sittingPlace,
                            false, false, flight.getBaseCost());
                    ticketService.create(ticket);
                    // Info about number of tickets in bucket
                    httpSession.setAttribute("ticketsInBucket", tickets.size() + ticketsInBucket);
                }
            }
            request.getRequestDispatcher(redirectBackString).forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
