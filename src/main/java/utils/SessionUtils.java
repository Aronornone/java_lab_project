package utils;

import db.service.InvoiceService;
import db.service.TicketService;
import db.service.UserService;
import pojo.Invoice;
import pojo.Ticket;
import pojo.User;
import stubs.StubUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

public class SessionUtils {
    private static InvoiceService is = new InvoiceService();
    private static TicketService ts = new TicketService();

    /**
     * Method for invalidate userSession if it destroyed by Logout or timeout
     * It gets only Invoice in status Created (it can be only one).
     * It loads FlightPlaces, Tickets and Flight for this Invoice from DB,
     * return places in Flight available places and bitSet (for business class if Ticket boolean business is true and econom
     * class if boolean is false), update Flight and FlightPlace and remove Tickets from DB.
     * After all it updates Invoice.Status for CANCELLED (can be used for reports of numbers of cancelled invoices
     * by users) and invalidate httpSession.
     *
     * @param httpSession session from which we get User
     */
    public static void invalidateSession(HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("user");
        Invoice invoice = null;

        if (is.getInvoiceByUser(user.getUserId(), Invoice.InvoiceStatus.CREATED).isPresent()) {
            invoice = is.getInvoiceByUser(user.getUserId(), Invoice.InvoiceStatus.CREATED).get();
            List<Ticket> tickets = ts.getTicketsByInvoice(invoice.getInvoiceId());
            StubUtils.revertSittingPlaces(tickets);
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
                    Optional<User> userOptional = userService.get(Long.parseLong(cookie.getValue()));
                    if (userOptional.isPresent()) {
                        user = userOptional.get();
                        httpSession.setAttribute("user", user);
                    }
                }
            }
        }
    }

}
