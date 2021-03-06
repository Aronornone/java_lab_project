package controller;

import org.apache.log4j.Logger;
import pojo.Invoice;
import pojo.Ticket;
import pojo.User;
import services.interfaces.InvoiceService;
import services.interfaces.TicketService;
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
import java.util.ResourceBundle;

/**
 * Servlet for list of tickets for user sorted by invoice
 */
@WebServlet(urlPatterns = {"/ticketsPrint"})
public class TicketPrintServlet extends HttpServlet {
    private static Logger log = Logger.getLogger("servletLogger");
    private static InvoiceService invoiceService;
    private static TicketService ticketService;

    public void init() {
        log.info("init(): Initializing 'invoiceService' and 'ticketService'.");
        invoiceService = InvoiceServiceImpl.getInstance();
        ticketService = TicketServiceImpl.getInstance();
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doGet(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");
        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute("user");
        httpSession.setAttribute("lastServletPath", request.getServletPath());

        //get all payed invoice for user
        log.info("doGet(request, response): Creating a list of all payed invoices.");
        List<Invoice> allPayedInvoices = invoiceService.getAllInvoicesByUserAndStatus(user.getUserId(), Invoice.InvoiceStatus.PAYED);
        List<Invoice> invoicesSortedForPrint = new ArrayList<>();
        //if invoices exists create sorted list for flights and tickets
        if (!allPayedInvoices.isEmpty()) {
            log.info("doGet(request, response): Creating all payed invoices.");
            List<Ticket> ticketsForPayedInvoice;
            for (Invoice invoice : allPayedInvoices) {
                ticketsForPayedInvoice = ticketService.getTicketsByInvoice(invoice.getInvoiceId());
                invoice.setTickets(ticketsForPayedInvoice);
                invoicesSortedForPrint.add(invoice);
            }
            request.setAttribute("invoices", invoicesSortedForPrint);
        }
        //if user doesn't have payed invoices show notification
        else {
            log.info("doGet(request, response): No payed invoices!");
            request.setAttribute("noPayedInvoices", err.getString("noPayedInvoices"));
        }
        request.getRequestDispatcher("/WEB-INF/pages/ticketPrints.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doPost(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        response.sendError(405);
    }
}
