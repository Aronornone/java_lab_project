package controller;

import org.apache.log4j.Logger;
import pojo.Ticket;
import pojo.User;
import services.interfaces.FlightPlaceService;
import services.interfaces.InvoiceService;
import services.interfaces.TicketService;
import services.servicesimpl.FlightPlaceServiceImpl;
import services.servicesimpl.InvoiceServiceImpl;
import services.servicesimpl.TicketServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
/**
 *  Servlet fort logic of deleting one ticket from user bucket (cart)
 */
@WebServlet(urlPatterns = {"/ticketDelete"})
public class DeleteTicketServlet extends HttpServlet {
    private static Logger log = Logger.getLogger("servletLogger");
    private static FlightPlaceService flightPlaceService;
    private static InvoiceService invoiceService;
    private static TicketService ticketService;

    public void init() {
        log.info("init(): Initializing 'flightPlaceService', 'invoiceService' and 'ticketService'.");
        flightPlaceService = FlightPlaceServiceImpl.getInstance();
        invoiceService = InvoiceServiceImpl.getInstance();
        ticketService = TicketServiceImpl.getInstance();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doGet(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute("user");

        // get ticketId from request, revert places from it to flight and flightplace
        // (and delete ticket in inner method), refresh tickets count in cart
        String ticketId = request.getParameter("ticketId");
        log.info("doGet(request, response): Initiaziling 'ticketOptional'.");
        Optional<Ticket> ticketOptional = ticketService.get(Long.parseLong(ticketId));
        ticketOptional.ifPresent(ticket1 -> {
            List<Ticket> tickets = new ArrayList<>();
            tickets.add(ticket1);
            flightPlaceService.revertSittingPlaces(tickets);
            int ticketsInBucket = invoiceService.getNumberOfTicketsInInvoice(user);
            httpSession.setAttribute("ticketsInBucket", ticketsInBucket);
        });
        String redirectBackString = "/bucket";
        log.info("doGet(request, response): Sending redirect.");
        response.sendRedirect(redirectBackString);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doPost(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        response.sendError(405);
    }
}
