package utils;

import db.services.interfaces.FlightPlaceService;
import db.services.interfaces.InvoiceService;
import db.services.interfaces.TicketService;
import db.services.interfaces.UserService;
import db.services.servicesimpl.FlightPlaceServiceImpl;
import db.services.servicesimpl.InvoiceServiceImpl;
import db.services.servicesimpl.TicketServiceImpl;
import db.services.servicesimpl.UserServiceImpl;
import pojo.Invoice;
import pojo.Ticket;
import pojo.User;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;

public class SessionUtils {
    private static final InvoiceService is = InvoiceServiceImpl.getInstance();
    private static final TicketService ts = TicketServiceImpl.getInstance();
    private static final FlightPlaceService fps = FlightPlaceServiceImpl.getInstance();
    private static final UserService us = UserServiceImpl.getInstance();

    /**
     * Method for invalidate userSession if it destroyed by Logout or timeout
     * It gets only Invoice in status Created (it can be only one).
     * It loads FlightPlaces, Tickets and FlightService for this Invoice from DB,
     * return places in FlightService available places and bitSet (for business class if Ticket boolean business is true and econom
     * class if boolean is false), update FlightService and FlightPlace and delete Tickets from DB.
     * After all it updates Invoice.Status for CANCELLED (can be used for reports of numbers of cancelled invoices
     * by users) and invalidate httpSession.
     *
     * @param httpSession session from which we get User
     */
    public static void invalidateSession(HttpSession httpSession) {

        User user = (User) httpSession.getAttribute("user");
        Invoice invoice;

        if (is.getInvoiceByUser(user.getUserId(), Invoice.InvoiceStatus.CREATED).isPresent()) {
            invoice = is.getInvoiceByUser(user.getUserId(), Invoice.InvoiceStatus.CREATED).get();
            List<Ticket> tickets = ts.getTicketsByInvoice(invoice.getInvoiceId());
            fps.revertSittingPlaces(tickets);
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
                    Optional<User> userOptional = us.get(Long.parseLong(cookie.getValue()));
                    if (userOptional.isPresent()) {
                        user = userOptional.get();
                        httpSession.setAttribute("user", user);
                    }
                }
            }
        }
    }
}
