package controller;

import db.services.servicesimpl.FlightServiceImpl;
import db.services.servicesimpl.InvoiceServiceImpl;
import db.services.servicesimpl.TicketServiceImpl;
import pojo.Flight;
import pojo.Invoice;
import pojo.Ticket;
import pojo.User;
import utils.ServletUtils;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.ResourceBundle;

@WebServlet(urlPatterns = {"/addFlightToInvoice"})
public class InvoiceServlet extends HttpServlet {
    private static FlightServiceImpl fs = new FlightServiceImpl();
    private static InvoiceServiceImpl is = new InvoiceServiceImpl();
    private static TicketServiceImpl ts = new TicketServiceImpl();

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
        String[] checkBox = (String[]) httpSession.getAttribute("business");

        if (user == null) {
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
        }

        String redirectBackString;
        if (checkBox != null) {
            redirectBackString = "/doSearch?dateFrom=" + dateFromString + "&dateTo=" + dateToString +
                    "&selectedDeparture=" + departure + "&selectedArrival=" + arrival +
                    "&numberTicketsFilter=" + numberTicketsFilterString + "&box=" + checkBox[0];
        } else {
            redirectBackString = "/doSearch?dateFrom=" + dateFromString + "&dateTo=" + dateToString +
                    "&selectedDeparture=" + departure + "&selectedArrival=" + arrival +
                    "&numberTicketsFilter=" + numberTicketsFilterString;
        }

        String numberTicketsFlightString = request.getParameter("numberTicketsFlight");
        int numberTicketsFlight = Integer.parseInt(numberTicketsFlightString);

        String flightIdString = request.getParameter("flightId");
        Flight flight = null;
        Optional<Flight> flightOptional = fs.get(Long.parseLong(flightIdString));
        if (flightOptional.isPresent()) {
            flight = flightOptional.get();
        }

        String[] checkbox = (String[]) httpSession.getAttribute("business");
        boolean business = false;
        int availableForClass = flight.getAvailablePlacesEconom();
        if (checkbox != null) {
            if (checkbox[0].equals("business")) {
                business = true;
                availableForClass = flight.getAvailablePlacesBusiness();
            }
        }

        Invoice invoice = getInvoiceForUser(request, response, err, user, redirectBackString, numberTicketsFlight, availableForClass);

        if (numberTicketsFlight != 0) {
            for (int i = 0; i < numberTicketsFlight; i++) {
                //request for available places and reserve of them
                int sittingPlace = ServletUtils.getRandomSittingPlace(flight.getFlightId(), business);
                if (sittingPlace == 0) {
                    request.setAttribute("notEnoughPlaces", err.getString("notEnoughPlaces"));
                    request.getRequestDispatcher(redirectBackString).forward(request, response);
                } else {
                    //new Ticket to DB

                    Ticket ticket = new Ticket(invoice, flight, "", "", sittingPlace,
                            false, business, (double)httpSession.getAttribute("ticketCost"));//price not from getBaseCost)() but from attribute
                    ts.add(ticket);
                }
            }
            int ticketsInBucket = ServletUtils.getNumberOfTicketsInInvoice(user);
            httpSession.setAttribute("ticketsInBucket", ticketsInBucket);
            request.setAttribute("ticketsAdd", err.getString("ticketsAdd"));
        }
        response.sendRedirect(redirectBackString);
    }

    private Invoice getInvoiceForUser(HttpServletRequest request, HttpServletResponse response, ResourceBundle err, User user,
                                      String redirectBackString, int numberTicketsFlight, int availableForClass) throws ServletException, IOException {
        Invoice invoice = null;
        if (numberTicketsFlight > availableForClass) {
            request.setAttribute("notEnoughPlaces", err.getString("notEnoughPlaces"));
            request.getRequestDispatcher(redirectBackString).forward(request, response);
        } else {
            //check if invoice already created in status Created for this user
            Optional<Invoice> invoiceOptional = is.getInvoiceByUser(user.getUserId(),
                    Invoice.InvoiceStatus.CREATED);
            if (invoiceOptional.isPresent()) {
                invoice = invoiceOptional.get();
            } else {
                //if invoice isn't created, add it
                invoice = new Invoice(user, Invoice.InvoiceStatus.CREATED, LocalDateTime.now());
                is.add(invoice);
            }
        }
        invoice = is.getInvoiceByUser(user.getUserId(), Invoice.InvoiceStatus.CREATED).get();
        return invoice;
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
