package stubs;

import db.service.FlightService;
import db.service.InvoiceService;
import db.service.TicketService;
import pojo.Ticket;
import pojo.User;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

//Заглушка для страницы корзины
@WebServlet(urlPatterns = {"/ticketDelete"})
public class StubDeleteTicketServlet extends HttpServlet {
    private static FlightService fs = new FlightService();
    private static InvoiceService is = new InvoiceService();
    private static TicketService ts = new TicketService();


    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");
        HttpSession httpSession = request.getSession();
        Cookie[] cookies = request.getCookies();
        SessionUtils.checkCookie(cookies, request, httpSession);
        User user = (User) httpSession.getAttribute("user");
        request.setCharacterEncoding("UTF-8");

        if (user == null) {
            request.getRequestDispatcher("/WEB-INF/pages/login.jsp").forward(request, response);
        }
        String ticketId = (String) request.getParameter("ticketId");
        Optional<Ticket> ticketOptional = ts.get(Long.parseLong(ticketId));
        if (ticketOptional.isPresent()) {
            Ticket ticket = ticketOptional.get();
            List<Ticket> tickets = new ArrayList<>();
            tickets.add(ticket);
            StubUtils.revertSittingPlaces(tickets);
            int ticketsInBucket = StubUtils.getNumberOfTicketsInInvoice(user);
            httpSession.setAttribute("ticketsInBucket", ticketsInBucket);
        }
        String redirectBackString = "/bucket";
        response.sendRedirect(redirectBackString);
    }


    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
