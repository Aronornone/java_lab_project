package utils;

import db.service.FlightService;
import db.service.InvoiceService;
import db.service.TicketService;
import db.service.UserService;
import pojo.Invoice;
import pojo.Ticket;
import pojo.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

public class SessionUtils {

    public static void invalidateSession(HttpSession httpSession) {
        InvoiceService invoiceService = new InvoiceService();
        TicketService ticketService = new TicketService();
        FlightService flightService = new FlightService();
        User user = (User) httpSession.getAttribute("user");

        //get only invoice in status Created
        Optional<Invoice> invoiceOptional = invoiceService.getInvoiceByUser(user.getUserId(),
                Invoice.InvoiceStatus.CREATED);

        if (invoiceOptional.isPresent()) {
            Invoice invoice = invoiceOptional.get();
            List<Ticket> tickets = ticketService.getTicketsByInvoice(invoice.getInvoiceId());

            for (Ticket ticket : tickets) {
                if (ticket.isBusinessClass()) {
                    ticket.getFlight().setAvailablePlacesBusiness(ticket.getFlight().getAvailablePlacesBusiness() + 1);
                } else
                    ticket.getFlight().setAvailablePlacesEconom(ticket.getFlight().getAvailablePlacesEconom() + 1);
                //update changed flights
                flightService.update(ticket.getFlight());
                //delete tickets
                ticketService.remove(ticket);
            }
            //update Invoice Status
            invoice.setInvoiceStatus(Invoice.InvoiceStatus.CANCELLED);
            invoiceService.update(invoice);
        }
        httpSession.invalidate();
    }

    public static void checkCookie(Cookie[] cookies, HttpServletRequest request,
                                   HttpSession httpSession) {
        User user;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("userId")) {
                    UserService userService = new UserService();
                    Optional<User> userOptional = userService.get(Integer.parseInt(cookie.getValue()));
                    if (userOptional.isPresent()) {
                        user = userOptional.get();
                        httpSession.setAttribute("user", user);
                    }
                }
            }
        }
    }

}
