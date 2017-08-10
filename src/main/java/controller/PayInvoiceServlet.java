package controller;

import db.services.interfaces.InvoiceService;
import db.services.interfaces.TicketService;
import db.services.servicesimpl.InvoiceServiceImpl;
import db.services.servicesimpl.TicketServiceImpl;
import org.apache.log4j.Logger;
import pojo.Invoice;
import pojo.User;
import utils.SessionUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

@WebServlet(urlPatterns = {"/invoicePay"})
public class PayInvoiceServlet extends HttpServlet {
    private static Logger log = Logger.getLogger("servLog");
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
        Cookie[] cookies = request.getCookies();
        SessionUtils.checkCookie(cookies, request, httpSession);
        User user = (User) httpSession.getAttribute("user");
        httpSession.setAttribute("lastServletPath", request.getServletPath());
        request.setCharacterEncoding("UTF-8");

        log.info("doGet(request, response): Initializing 'invoiceOptional' by user.");
        Optional<Invoice> invoiceOptional = invoiceService.getInvoiceByUser(user.getUserId(),
                Invoice.InvoiceStatus.CREATED);
        String[] ticketsIds = request.getParameterValues("ticketId");
        String[] passengerNames = request.getParameterValues("passengerName");
        String[] passports = request.getParameterValues("passport");
        String[] luggages = request.getParameterValues("lugBox");

        if (ticketService.isEmptyWhilePayAndSave(ticketsIds, passengerNames, passports, luggages)) {
            log.info("doGet(request, response): Ticket is empty.");
            request.setAttribute("setFields", err.getString("setFields"));
            request.setAttribute("changesSaved", err.getString("changesSaved"));
            request.getRequestDispatcher("/bucket").forward(request, response);
        } else if (invoiceOptional.isPresent()) {
            log.info("doGet(request, response): Setting invoice status to PAYED.");
            Invoice invoice = invoiceOptional.get();
            invoice.setInvoiceStatus(Invoice.InvoiceStatus.PAYED);
            invoiceService.update(invoice);
            int ticketsInBucket = invoiceService.getNumberOfTicketsInInvoice(user);
            httpSession.setAttribute("ticketsInBucket", ticketsInBucket);
            httpSession.setAttribute("invoiceView", null);
            request.getRequestDispatcher("/WEB-INF/pages/invoiceSuccess.jsp").forward(request, response);
        } else {
            log.info("doGet(request, response): Executing request.getRequestDispatcher(...).");
            request.getRequestDispatcher("/bucket").forward(request, response);
        }
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.info("doPost(request, response): Received the following 'request' = " + request.getQueryString() + ", 'response' = " + response.getStatus());
        doGet(request, response);
    }
}
