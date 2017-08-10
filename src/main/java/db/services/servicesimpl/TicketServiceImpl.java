package db.services.servicesimpl;

import db.dao.daoimpl.TicketDAOImpl;
import db.dao.interfaces.TicketDAO;
import db.services.interfaces.TicketService;
import org.apache.log4j.Logger;
import pojo.Ticket;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public final class TicketServiceImpl implements TicketService {
    private static Logger log = Logger.getLogger("DBLogger");
    private final TicketDAO dao = TicketDAOImpl.getInstance();

    private final static TicketService instance = new TicketServiceImpl();

    public static TicketService getInstance() {
        log.info("getInstance(): Returning instance of TicketServiceImpl");
        return instance;
    }

    private TicketServiceImpl() {
    }

    @Override
    public void add(Ticket ticket) {
        log.info("add(ticket): Delegating an 'add ticket' query to DAO with the following 'ticket' object: " + ticket);
        dao.add(ticket);
    }

    @Override
    public Optional<Ticket> get(long id) {
        log.info("get(id): Delegating a 'get ticket by id' query to DAO with the following 'id' = " + id);
        return dao.get(id);
    }

    @Override
    public List<Ticket> getTicketsByInvoice(long invoiceId) {
        log.info("getTicketsByInvoice(invoiceId): Delegating a 'get ticket by invoice id' query to DAO with the following 'invoiceId' = " + invoiceId);
        return dao.getTicketsByInvoice(invoiceId);
    }

    @Override
    public void update(Ticket ticket) {
        log.info("update(ticket): Delegating an 'update ticket' query to DAO with the following 'ticket' object: " + ticket);
        dao.update(ticket);
    }

    /**
     * Method for check if all fields of tickets in invoice are contains information. Then it saves ticket info, anyway.
     *
     * @param ticketsIds     array of Strings tickets ids
     * @param passengerNames array of Strings passenger names
     * @param passports      array of Strings passports
     * @return true if some of fields are empty, false if all fields are fill right.
     */
    @Override
    public boolean isEmptyWhilePayAndSave(String[] ticketsIds, String[] passengerNames,
                                          String[] passports, String[] luggages) {
        log.info("isEmptyWhilePayAndSave(...): Getting an instance of TicketServiceImpl.");
        TicketService ticketService = TicketServiceImpl.getInstance();
        boolean empty = false;
        log.info("isEmptyWhilePayAndSave(...): Checking method arguments.");
        if (ticketsIds != null && passengerNames != null && passports != null) {
            for (String ticketsId : ticketsIds) {
                if (ticketsId.isEmpty()) {
                    empty = true;
                }
            }
            for (String passengerName : passengerNames) {
                if (passengerName.isEmpty()) {
                    empty = true;
                }
            }
            for (String passport : passports) {
                if (passport.isEmpty()) {
                    empty = true;
                }
            }
            log.info("isEmptyWhilePayAndSave(...): Creating an array of luggages.");
            boolean[] luggagesBoolean = new boolean[ticketsIds.length];

            if (luggages != null) {
                for (int i = 0; i < luggages.length; i++) {
                    luggagesBoolean[i] = (luggages[i].equals("luggage"));
                }
            }
            log.info("isEmptyWhilePayAndSave(...): Updating a ticket information.");
            ticketService.updateTicketWhilePay(ticketsIds, passengerNames, passports, luggagesBoolean);
        } else {
            log.info("isEmptyWhilePayAndSave(...): 'empty' = false");
            empty = true;
        }

        log.info("isEmptyWhilePayAndSave(...): Returning an 'empty' variable: " + empty);
        return empty;
    }

    /**
     * Method for update information of ticket based on what client set in fields
     *
     * @param ticketsIds     array of Strings tickets ids
     * @param passengerNames array of Strings passenger names
     * @param passports      array of Strings passports
     */
    @Override
    public void updateTicketWhilePay(String[] ticketsIds, String[] passengerNames,
                                     String[] passports, boolean[] luggages) {
        log.info("updateTicketWhilePay(...): Getting instances of TicketServiceImpl and updating them.");
        for (int i = 0; i < ticketsIds.length; i++) {
            TicketService ticketService = TicketServiceImpl.getInstance();
            Ticket ticketToUpdate = ticketService.get(Long.parseLong(ticketsIds[i])).orElse(null);
            ticketToUpdate.setPassengerName(passengerNames[i]);
            ticketToUpdate.setPassport(passports[i]);
            ticketToUpdate.setLuggage(luggages[i]);
            ticketService.update(ticketToUpdate);
        }
    }

    public double recountPrice(double basePrice, LocalDateTime dateTime, boolean business) {
        double result = basePrice;
        if (business) {
            result = affectByBusiness(basePrice);
        }
        result = affectPriceByDate(result, dateTime);
        return (int) result; // casting int for simplicity
    }

    @Override
    public double affectByBusiness(double basePrice) {
        return affectPriceByPercents(basePrice, 30);
    }

    @Override
    public double affectByLuggage(double basePrice) {
        return basePrice + 1000;
    }

    @Override
    public double defectByLuggage(double basePrice) {
        return basePrice - 1000;
    }

    @Override
    public double affectPriceByPercents(double basePrice, int percents) {
        return basePrice * (1 + (double) percents / 100);
    }

    //max price increase=basePrice*0.3(30%)
    //price increases only if days until departure<120 days
    @Override
    public double affectPriceByDate(double basePrice, LocalDateTime dateTime) {
        long daysUntilDeparture = LocalDateTime.from(LocalDateTime.now()).until(dateTime, ChronoUnit.DAYS);
        if (daysUntilDeparture > 120) {
            return basePrice;
        }
        return basePrice + 0.3 * basePrice * (1 - daysUntilDeparture / 120);
    }

    @Override
    public void delete(Ticket ticket) {
        log.info("delete(ticket): Delegating a 'delete ticket' query to DAO with the following 'ticket' object: " + ticket);
        dao.delete(ticket);
    }

    @Override
    public List<Ticket> getAll() {
        log.info("getAll(): Delegating a 'get all tickets' query to DAO.");
        return dao.getAll();
    }

}
