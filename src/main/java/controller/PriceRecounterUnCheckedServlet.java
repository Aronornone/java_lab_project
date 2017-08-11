package controller;

import db.services.interfaces.TicketService;
import db.services.servicesimpl.TicketServiceImpl;
import org.apache.log4j.Logger;
import pojo.Ticket;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* Servlet for recount price of ticket in cart if we UNcheck parameters(dynamically)
*/
@WebServlet(urlPatterns = "/priceRecountUnChecked")
public class PriceRecounterUnCheckedServlet extends HttpServlet {
    private static Logger log = Logger.getLogger("servletLogger");
    private static TicketService ticketService;

    public void init() {
        log.info("init(): Initializing 'ticketService'.");
        ticketService = TicketServiceImpl.getInstance();
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doGet(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        long ticketId = Long.parseLong(request.getParameter("ticketId"));

        //get ticket from cart
        Ticket ticket = ticketService.get(ticketId).orElse(null);
        // get old Price of ticket
        double oldPrice = ticket.getPrice();
        // if parameter is unchecked recount price back and uncheck luggage
        double newPrice = ticketService.defectByLuggage(oldPrice);
        ticket.setLuggage(false);
        ticket.setPrice(newPrice);
        log.info("doGet(request, response): Updating ticket price to " + newPrice);
        ticketService.update(ticket);

        response.setContentType("text/plain");
        response.getWriter().write(String.valueOf(newPrice));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doPost(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        doGet(request, response);
    }
}
