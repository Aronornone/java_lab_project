package controller;

import db.services.interfaces.InvoiceService;
import db.services.interfaces.TicketService;
import db.services.servicesimpl.InvoiceServiceImpl;
import db.services.servicesimpl.TicketServiceImpl;
import pojo.Invoice;
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
import java.util.ResourceBundle;

@WebServlet(urlPatterns = {"/ticketsPrint"})
public class TicketPrintServlet extends HttpServlet {
    private static InvoiceService invoiceService;
    private static TicketService ticketService;

    public void init() {
        invoiceService = InvoiceServiceImpl.getInstance();
        ticketService = TicketServiceImpl.getInstance();
    }
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        ResourceBundle err = (ResourceBundle) getServletContext().getAttribute("errors");
        HttpSession httpSession = request.getSession();
        User user = (User) httpSession.getAttribute("user");
        httpSession.setAttribute("lastServletPath", request.getServletPath());

        List<Invoice> allPayedInvoices = invoiceService.getAllInvoicesByUserAndStatus(user.getUserId(), Invoice.InvoiceStatus.PAYED);
        List<Invoice> invoicesSortedForPrint = new ArrayList<>();
        if (!allPayedInvoices.isEmpty()) {
            List<Ticket> ticketsForPayedInvoice;
            for (Invoice invoice : allPayedInvoices) {
                ticketsForPayedInvoice = ticketService.getTicketsByInvoice(invoice.getInvoiceId());
                invoice.setTickets(ticketsForPayedInvoice);
                invoicesSortedForPrint.add(invoice);
            }
            request.setAttribute("invoices", invoicesSortedForPrint);
        } else {
            request.setAttribute("noPayedInvoices", err.getString("noPayedInvoices"));
        }
        request.getRequestDispatcher("/WEB-INF/pages/ticketPrints.jsp").forward(request, response);
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
