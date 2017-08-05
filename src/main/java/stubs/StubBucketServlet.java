package stubs;

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
import java.util.*;

//Заглушка для страницы корзины
@WebServlet(urlPatterns = {"/bucket"})
public class StubBucketServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");
        HttpSession httpSession = request.getSession();
        Cookie[] cookies = request.getCookies();
        SessionUtils.checkCookie(cookies, request, httpSession);
        User user = (User) httpSession.getAttribute("user");

        if (user == null) {
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
        }
        InvoiceService invoiceService = new InvoiceService();
        TicketService ticketService = new TicketService();

        httpSession.setAttribute("lastServletPath",request.getServletPath());

        Optional<Invoice> invoiceOptional = invoiceService.getInvoiceByUser(user.getUserId(),
                Invoice.InvoiceStatus.CREATED);

        if (invoiceOptional.isPresent()) {
            Invoice invoice = invoiceOptional.get();
            httpSession.setAttribute("invoiceId",invoice.getInvoiceId());
            List<Ticket> tickets = ticketService.getTicketsByInvoice(invoice.getInvoiceId());

            if (tickets.size()==0) {
                request.setAttribute("cartEmpty", err.getString("cartEmpty"));
            }

            Set<Flight> flights = new LinkedHashSet<>();
            for(Ticket ticket: tickets) {
                flights.add(ticket.getFlight());
            }

            LinkedHashSet<Ticket> ticketsForFlight;
            for(Flight flight: flights) {
                ticketsForFlight = new LinkedHashSet<>();
                for(Ticket ticket: tickets)
                if(flight.getFlightId() == ticket.getFlight().getFlightId()) {
                    ticketsForFlight.add(ticket);
                }
                flight.setTickets(ticketsForFlight);
            }

            request.setAttribute("flights", flights);
            //Logic for calc ticket price with parameters of checkboxes, make it onclick action and jquery
            //boolean business = (boolean) request.getAttribute("business");
            //boolean luggage = (boolean) request.getAttribute("luggage");

            double sumTotal = 0;
            // calc total Sum of all tickets in invoice
            for(Ticket ticket: tickets) {
                sumTotal = sumTotal + ticket.getPrice();
            }
            request.setAttribute("totalSum", sumTotal);
        } else {
            request.setAttribute("cartEmpty", err.getString("cartEmpty"));
        }
        request.getRequestDispatcher("/WEB-INF/pages/bucket.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
