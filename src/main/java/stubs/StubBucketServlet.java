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
        request.setCharacterEncoding("UTF-8");

        InvoiceService invoiceService = new InvoiceService();
        TicketService ticketService = new TicketService();

        httpSession.setAttribute("lastServletPath", request.getServletPath());

        Optional<Invoice> invoiceOptional = invoiceService.getInvoiceByUser(user.getUserId(),
                Invoice.InvoiceStatus.CREATED);
        if (!invoiceOptional.isPresent()) {
            httpSession.setAttribute("invoiceView", "noTickets");
            request.setAttribute("cartEmpty", err.getString("cartEmpty"));
        } else {
            Invoice invoice = invoiceOptional.get();
            httpSession.setAttribute("invoiceView", invoice.getInvoiceId());
            httpSession.setAttribute("invoiceId", invoice.getInvoiceId());
            List<Ticket> allTicketsOfInvoice = ticketService.getTicketsByInvoice(invoice.getInvoiceId());

            if (allTicketsOfInvoice.size() == 0) {
                httpSession.setAttribute("invoiceView", "noTickets");
                request.setAttribute("cartEmpty", err.getString("cartEmpty"));
            }

            Set<Flight> flights = new HashSet<>();
            for (Ticket ticket : allTicketsOfInvoice) {
                flights.add(ticket.getFlight());
            }

            Set<Ticket> ticketsForFlight;
            for (Flight flight : flights) {
                ticketsForFlight = new HashSet<>();

                for (Ticket ticket : allTicketsOfInvoice)
                    if (flight.getFlightId() == ticket.getFlight().getFlightId()) {
                        ticketsForFlight.add(ticket);
                    }
                flight.setTickets(ticketsForFlight);
            }

            request.setAttribute("flights", flights);

            double sumTotal = 0;
            // calc total Sum of all allTicketsOfInvoice in invoice
            for (Ticket ticket : allTicketsOfInvoice) {
                sumTotal = sumTotal + ticket.getPrice();
            }
            request.setAttribute("totalSum", sumTotal);
        }
        request.getRequestDispatcher("/WEB-INF/pages/bucket.jsp").forward(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
