package controller;

import db.services.interfaces.FlightPlaceService;
import db.services.interfaces.InvoiceService;
import db.services.interfaces.TicketService;
import db.services.servicesimpl.FlightPlaceServiceImpl;
import db.services.servicesimpl.InvoiceServiceImpl;
import db.services.servicesimpl.TicketServiceImpl;
import pojo.Ticket;
import pojo.User;

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

@WebServlet(urlPatterns = {"/ticketDelete"})
public class DeleteTicketServlet extends HttpServlet {
    private static FlightPlaceService flightPlaceService;
    private static InvoiceService invoiceService;
    private static TicketService ticketService;

    public void init() {
        flightPlaceService = FlightPlaceServiceImpl.getInstance();
        invoiceService = InvoiceServiceImpl.getInstance();
        ticketService = TicketServiceImpl.getInstance();
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);

    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute("user");

        String ticketId = request.getParameter("ticketId");
        Optional<Ticket> ticketOptional = ticketService.get(Long.parseLong(ticketId));
        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();
            List<Ticket> tickets = new ArrayList<>();
            tickets.add(ticket);
            flightPlaceService.revertSittingPlaces(tickets);
            int ticketsInBucket = invoiceService.getNumberOfTicketsInInvoice(user);
            httpSession.setAttribute("ticketsInBucket", ticketsInBucket);
        }
        String redirectBackString = "/bucket";
        response.sendRedirect(redirectBackString);
    }
}
