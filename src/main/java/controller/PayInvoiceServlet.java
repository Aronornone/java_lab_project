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

/**
 * Servlet for pay of invoice in bucket (all tickets in it)
 */
@WebServlet(urlPatterns = {"/invoicePay"})
public class PayInvoiceServlet extends HttpServlet {
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
        Cookie[] cookies = request.getCookies();
        SessionUtils.checkCookie(cookies, request, httpSession);
        User user = (User) httpSession.getAttribute("user");
        httpSession.setAttribute("lastServletPath", request.getServletPath());
        // set encoding for information of user in Russian. Fields of name are validated to be in English in HTML, but
        // it can be needed for any other purpose
        request.setCharacterEncoding("UTF-8");

        log.info("doGet(request, response): Initializing 'invoiceOptional' by user.");
        Optional<Invoice> invoiceOptional = invoiceService.getInvoiceByUser(user.getUserId(),
                Invoice.InvoiceStatus.CREATED);
        // get set fields from request by array parameters
        String[] ticketsIds = request.getParameterValues("ticketId");
        String[] passengerNames = request.getParameterValues("passengerName");
        String[] passports = request.getParameterValues("passport");
        String[] luggages = request.getParameterValues("lugBox");

        // check if ticket fields are all filled, and save in inner method this info to DB.
        // If not all fields filled show notification, and save info any way from filled
        if (ticketService.isEmptyWhilePayAndSave(ticketsIds, passengerNames, passports, luggages)) {
            log.info("doGet(request, response): Ticket is empty.");
            request.setAttribute("setFields", err.getString("setFields"));
            request.setAttribute("changesSaved", err.getString("changesSaved"));
            request.getRequestDispatcher("/bucket").forward(request, response);
        }
        // if all fields are filled pay invoice, update it in DB and set new count of tickets in cart,
        // redirect to page with invoice
        else if (invoiceOptional.isPresent()) {
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
