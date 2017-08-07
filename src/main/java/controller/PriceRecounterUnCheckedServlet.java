package controller;

import db.service.TicketService;
import pojo.Ticket;
import utils.PriceRecounter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = "/priceRecountUnChecked")
public class PriceRecounterUnCheckedServlet extends HttpServlet {
    private static TicketService ts = new TicketService();

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        long ticketId = Long.parseLong(request.getParameter("ticketId"));

        Ticket ticket = ts.get(ticketId).get();

        double oldPrice = ticket.getPrice();
        double newPrice = PriceRecounter.defectByLuggage(oldPrice);
        ticket.setLuggage(false);
        ticket.setPrice(newPrice);
        ts.update(ticket);

        response.setContentType("text/plain");
        response.getWriter().write(String.valueOf(newPrice));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
