package controller;

import db.services.interfaces.InvoiceService;
import db.services.interfaces.TicketService;
import db.services.servicesimpl.InvoiceServiceImpl;
import db.services.servicesimpl.TicketServiceImpl;
import org.apache.log4j.Logger;
import pojo.Flight;
import pojo.Invoice;
import pojo.Ticket;
import pojo.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@WebServlet(urlPatterns = {"/bucket"})
public class BucketServlet extends HttpServlet {
    private static Logger log = Logger.getLogger("servLog");
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
        if (invoiceOptional.isPresent()) {
            Invoice invoice = invoiceOptional.get();
            httpSession.setAttribute("invoiceId", invoice.getInvoiceId());
            List<Ticket> tickets = ticketService.getTicketsByInvoice(invoice.getInvoiceId());

            if (tickets.size() == 0) {
                request.setAttribute("cartEmpty", err.getString("cartEmpty"));
            }
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
            request.setAttribute("flights", flights);

            double sumTotal = 0;
            // calc total Sum of all tickets in invoice
            for (Ticket ticket : tickets) {
                sumTotal = sumTotal + ticket.getPrice();
            }
            request.setAttribute("totalSum", sumTotal);
        } else {
            request.setAttribute("cartEmpty", err.getString("cartEmpty"));
        }
        log.info("doGet(request, response): Executing request.getRequestDispatcher(...).");
        request.getRequestDispatcher("/WEB-INF/pages/bucket.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doPost(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        doGet(request, response);
    }
}
