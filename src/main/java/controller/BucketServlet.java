package controller;

import org.apache.log4j.Logger;
import pojo.Flight;
import pojo.Invoice;
import pojo.Ticket;
import pojo.User;
import services.interfaces.InvoiceService;
import services.interfaces.TicketService;
import services.servicesimpl.InvoiceServiceImpl;
import services.servicesimpl.TicketServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

/**
 * Servlet for bucket (cart) logic
 */

@WebServlet(urlPatterns = {"/bucket"})
public class BucketServlet extends HttpServlet {
    private static Logger log = Logger.getLogger("servletLogger");
    private static InvoiceService invoiceService;
    private static TicketService ticketService;

    public void init() {
        log.info("init(): Initializing 'invoiceService' and 'ticketService'.");
        invoiceService = InvoiceServiceImpl.getInstance();
        ticketService = TicketServiceImpl.getInstance();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doGet(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");
        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute("user");
        request.setCharacterEncoding("UTF-8");
        httpSession.setAttribute("lastServletPath", request.getServletPath());

        Optional<Invoice> invoiceOptional = invoiceService.getInvoiceByUser(user.getUserId(),
                Invoice.InvoiceStatus.CREATED);

        log.info("doGet(request, response): Counting total price.");
        Invoice invoice;
        List<Ticket> tickets;
        if (invoiceOptional.isPresent()) {
            invoice = invoiceOptional.get();
            httpSession.setAttribute("invoiceId", invoice.getInvoiceId());
            tickets = ticketService.getTicketsByInvoice(invoice.getInvoiceId());
        } else {
            log.info("doGet(request, response): Cart is empty!");
            request.setAttribute("cartEmpty", err.getString("cartEmpty"));
            request.getRequestDispatcher("/WEB-INF/pages/bucket.jsp").forward(request, response);
            return;
        }

        if (tickets.isEmpty()) {
            request.setAttribute("cartEmpty", err.getString("cartEmpty"));
            request.getRequestDispatcher("/WEB-INF/pages/bucket.jsp").forward(request, response);
            return;
        }

        Set<Flight> flights = getTicketsSortedByFlights(tickets);
        request.setAttribute("flights", flights);

        double sumTotal = getSumTotalOfTickets(tickets);
        request.setAttribute("totalSum", sumTotal);

        log.info("doGet(request, response): Executing request.getRequestDispatcher(...).");
        request.getRequestDispatcher("/WEB-INF/pages/bucket.jsp").
                forward(request, response);

    }

    /**
     * Method calculate total sum of tickets in invoice
     *
     * @param tickets all tickets of invoice
     * @return total sum from all tickets in cart
     */
    private double getSumTotalOfTickets(List<Ticket> tickets) {
        // calc total sum of all tickets in invoice
        double sumTotal = 0;
        for (Ticket ticket : tickets) {
            sumTotal = sumTotal + ticket.getPrice();
        }
        return sumTotal;
    }

    /**
     * Method groups ticket in flights lists
     *
     * @param tickets list of all tickets too group
     * @return set of flights include lists of tickets
     */
    private Set<Flight> getTicketsSortedByFlights(List<Ticket> tickets) {
        Set<Flight> flights = new LinkedHashSet<>();
        for (Ticket ticket : tickets) {
            flights.add(ticket.getFlight());
        }

        LinkedHashSet<Ticket> ticketsForFlight;
        for (Flight flight : flights) {
            ticketsForFlight = new LinkedHashSet<>();
            for (Ticket ticket : tickets)
                if (flight.getFlightId() == ticket.getFlight().getFlightId()) {
                    ticketsForFlight.add(ticket);
                }
            flight.setTickets(ticketsForFlight);
        }
        return flights;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doPost(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        doGet(request, response);
    }
}
