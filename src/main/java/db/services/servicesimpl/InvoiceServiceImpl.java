package db.services.servicesimpl;

import db.dao.daoimpl.InvoiceDAOImpl;
import db.dao.interfaces.InvoiceDAO;
import db.services.interfaces.InvoiceService;
import db.services.interfaces.TicketService;
import org.apache.log4j.Logger;
import pojo.Invoice;
import pojo.Ticket;
import pojo.User;

import java.util.List;
import java.util.Optional;

public final class InvoiceServiceImpl implements InvoiceService {
    private static Logger log = Logger.getLogger("DBLogger");
    private final InvoiceDAO dao = InvoiceDAOImpl.getInstance();

    private final static InvoiceService instance = new InvoiceServiceImpl();
    public static InvoiceService getInstance() {
        log.info("getInstance(): Returning instance of FlightServiceImpl");
        return instance;
    }
    private InvoiceServiceImpl(){
    }

    @Override
    public void add(Invoice invoice) {
        log.info("add(invoice): Delegating an 'add invoice' query to DAO with the following 'invoice' object: " + invoice);
        dao.add(invoice);
    }

    @Override
    public Optional<Invoice> get(long id) {
        log.info("get(id): Delegating a 'get invoice by id' query to DAO with the following 'id' = " + id);
        return dao.get(id);
    }

    @Override
    public Optional<Invoice> getInvoiceByUser(long userId, Invoice.InvoiceStatus status) {
        log.info("getInvoiceByUser(userId, status): Delegating a 'get invoice by user id' query to DAO with the following 'userId' = " + userId);
        return dao.getInvoiceByUser(userId, status);
    }

    /**
     * Method for getting number of tickets in created invoice of user (exactly bucket)
     *
     * @param user user for whom we check number of tickets in invoice to view in bucket
     * @return number of Ticket in invoice
     */
    @Override
    public int getNumberOfTicketsInInvoice(User user) {
        Invoice invoice;
        int numberOfTicketsInInvoice = 0;
        log.info("getNumberOfTicketsInInvoice(user): Getting instances of InvoiceServiceImpl, TicketServiceImpl.");
        InvoiceService invoiceService = InvoiceServiceImpl.getInstance();
        TicketService ticketService = TicketServiceImpl.getInstance();
        log.info("getNumberOfTicketsInInvoice(user): Getting a list of tickets and list size if invoice 'CREATED' exists.");
        if (invoiceService.getInvoiceByUser(user.getUserId(), Invoice.InvoiceStatus.CREATED).isPresent()) {
            invoice = invoiceService.getInvoiceByUser(user.getUserId(), Invoice.InvoiceStatus.CREATED).get();
            List<Ticket> tickets = ticketService.getTicketsByInvoice(invoice.getInvoiceId());
            numberOfTicketsInInvoice = tickets.size();
        }
        log.info("getNumberOfTicketsInInvoice(user): Returning number of tickets in invoice.");
        return numberOfTicketsInInvoice;
    }

    @Override
    public void update(Invoice invoice) {
        log.info("update(invoice): Delegating an 'update invoice' query to DAO with the following 'invoice' object: " + invoice);
        dao.update(invoice);
    }

    @Override
    public void delete(Invoice invoice) {
        log.info("delete(invoice): Delegating a 'delete invoice' query to DAO with the following 'invoice' object: " + invoice);
        dao.delete(invoice);
    }

    @Override
    public List<Invoice> getAll() {
        log.info("getAll(): Delegating a 'get all invoices' query to DAO.");
        return dao.getAll();
    }

    @Override
    public List<Invoice> getAllInvoicesByUserAndStatus(long userId, Invoice.InvoiceStatus status) {
        log.info("getAllInvoicesByUserAndStatus(userId, status): Delegating a 'get all invoices by user and status' query to DAO.");
        return dao.getAllInvoicesByUserAndStatus(userId, status);
    }
}
