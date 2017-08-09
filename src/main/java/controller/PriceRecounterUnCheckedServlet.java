package controller;

import db.services.interfaces.TicketService;
import db.services.servicesimpl.TicketServiceImpl;
import pojo.Ticket;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/priceRecountUnChecked")
public class PriceRecounterUnCheckedServlet extends HttpServlet {
    private static TicketService ticketService;

    public void init() {
        ticketService = TicketServiceImpl.getInstance();
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long ticketId = Long.parseLong(request.getParameter("ticketId"));

        Ticket ticket = ticketService.get(ticketId).get();

        double oldPrice = ticket.getPrice();
        double newPrice = ticketService.defectByLuggage(oldPrice);
        ticket.setLuggage(false);
        ticket.setPrice(newPrice);
        ticketService.update(ticket);

        response.setContentType("text/plain");
        response.getWriter().write(String.valueOf(newPrice));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
