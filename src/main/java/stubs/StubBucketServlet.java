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

        InvoiceService invoiceService = new InvoiceService();
        TicketService ticketService = new TicketService();
        FlightService flightService = new FlightService();

        Optional<Invoice> invoiceOptional = invoiceService.getInvoiceByUser(user.getUserId(),
                Invoice.InvoiceStatus.CREATED);

        if (invoiceOptional.isPresent()) {
            Invoice invoice = invoiceOptional.get();
            //Для этого заказа найти все билеты, вытащить из них рейсы и сгруппировать билеты
            List<Ticket> tickets = ticketService.getTicketsByInvoice(invoice.getInvoiceId());

            tickets.sort(new Comparator<Ticket>() {
                @Override
                public int compare(Ticket o1, Ticket o2) {
                    return (int) (o1.getFlight().getFlightId()-o2.getFlight().getFlightId());
                }
            });

            System.out.println(tickets);

            List<Flight> flights = new ArrayList<>();
            for(Ticket ticket: tickets) {
                flights.add(ticket.getFlight());
            }

            System.out.println("ticketsForFlights:" + flights );

            request.setAttribute("ticketsFlights", flights);


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
