package controller;

import org.apache.log4j.Logger;
import pojo.Ticket;
import services.interfaces.TicketService;
import services.servicesimpl.TicketServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet for recount price of ticket in cart if we check parameters(dynamically)
 */
@WebServlet(urlPatterns = "/priceRecountChecked")
public class PriceRecounterCheckedServlet extends HttpServlet {
    private static Logger log = Logger.getLogger("servletLogger");
    private static TicketService ticketService;

    public void init() {
        log.info("init(): Initializing 'ticketService'.");
        ticketService =  TicketServiceImpl.getInstance();
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doGet(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        long ticketId = Long.parseLong(request.getParameter("ticketId"));

        //recount price
        Ticket ticket = ticketService.get(ticketId).orElse(null);
        // egt old price from ticket
        double oldPrice = ticket.getPrice();
        // if parameter is checked recount price and set luggage to true
        double newPrice = ticketService.affectByLuggage(oldPrice);
        ticket.setLuggage(true);
        ticket.setPrice(newPrice);
        log.info("doGet(request, response): Updating ticket price to " + newPrice);
        ticketService.update(ticket);

        response.setContentType("text/plain");
        response.getWriter().write(String.valueOf(newPrice));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doPost(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        response.sendError(405);
    }
}
