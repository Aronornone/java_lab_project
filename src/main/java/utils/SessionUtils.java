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
    private static InvoiceService is = new InvoiceService();
    private static TicketService ts = new TicketService();
    private static FlightService fs = new FlightService();

    public static void invalidateSession(HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("user");

        //get only invoice in status Created
        Optional<Invoice> invoiceOptional = is.getInvoiceByUser(user.getUserId(),
                Invoice.InvoiceStatus.CREATED);

        if (invoiceOptional.isPresent()) {
            Invoice invoice = invoiceOptional.get();
            List<Ticket> tickets = ts.getTicketsByInvoice(invoice.getInvoiceId());

            for (Ticket ticket : tickets) {
                //need to add repair of flightplace bitset
                if (ticket.isBusinessClass()) {
                    ticket.getFlight().setAvailablePlacesBusiness(ticket.getFlight().getAvailablePlacesBusiness() + 1);
                } else
                    ticket.getFlight().setAvailablePlacesEconom(ticket.getFlight().getAvailablePlacesEconom() + 1);
                //update changed flights
                fs.update(ticket.getFlight());
                //delete tickets
                ts.remove(ticket);
            }
            //update Invoice Status
            invoice.setInvoiceStatus(Invoice.InvoiceStatus.CANCELLED);
            is.update(invoice);
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
