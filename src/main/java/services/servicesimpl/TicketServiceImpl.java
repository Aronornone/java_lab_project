package services.servicesimpl;

import dao.daoimpl.TicketDAOImpl;
import dao.interfaces.TicketDAO;
import org.apache.log4j.Logger;
import pojo.Ticket;
import services.interfaces.TicketService;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

public final class TicketServiceImpl implements TicketService {
    private static Logger log = Logger.getLogger("DBLogger");
    private final TicketDAO dao = TicketDAOImpl.getInstance();

    private final static int LUGGAGE_AFFECT = 1000;
    private final static int BUSINESS_AFFECT = 30;
    private final static int MAX_DAYS_TO_FLIGHT = 120;
    private final static double MAX_PRICE_INCREASE = 0.3;


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
     * Method for check if all fields of tickets in invoice are contains information.
     * Then it saves ticket info, anyway.
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
                    break;
                }
            }
            for (String passengerName : passengerNames) {
                if (passengerName.isEmpty()) {
                    empty = true;
                    break;
                }
            }
            for (String passport : passports) {
                if (passport.isEmpty()) {
                    empty = true;
                    break;
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

    /**
     * Method recount price for individual ticket based on instant date and class of ticket.
     *
     * @param basePrice base price of ticket from flight (calculated)
     * @param dateTime  current date and time
     * @param business  if true class is business, if false class is econom
     * @return calculated price casting int for simplicity
     */
    public double recountPrice(double basePrice, LocalDateTime dateTime, boolean business) {
        double result = basePrice;
        if (business) {
            result = affectByBusiness(basePrice);
        }
        result = affectPriceByDate(result, dateTime);
        return (int) result;
    }

    /**
     * Method for increase price of ticket if it is business class
     *
     * @param basePrice from flight
     * @return calculated price with affect of business by procent
     */
    @Override
    public double affectByBusiness(double basePrice) {
        return affectPriceByPercents(basePrice, BUSINESS_AFFECT);
    }

    /**
     * Method for increase price of ticket if luggage is add by user
     *
     * @param basePrice from flight
     * @return base price increased by luggage constant price
     */
    @Override
    public double affectByLuggage(double basePrice) {
        return basePrice + LUGGAGE_AFFECT;
    }

    /**
     * Method for decrease price of ticket if luggage is cancel by user
     *
     * @param basePrice from flight
     * @return base price decreased by luggage constant price
     */
    @Override
    public double defectByLuggage(double basePrice) {
        return basePrice - LUGGAGE_AFFECT;
    }

    /**
     * Method for increase price of ticket by setting percents
     *
     * @param basePrice from flight
     * @return base price increased by percents
     */
    @Override
    public double affectPriceByPercents(double basePrice, int percents) {
        return basePrice * (1 + (double) percents / 100);
    }

    /**
     * Method fo calculate ticket increased price by distance to date of flight
     * Max price increase=basePrice*0.3(30%) (MAX_PRICE_INCREASE)
     * Price increases only if days until departure<120 days (MAX_DAYS_TO_FLIGHT)
     *
     * @param basePrice from flight
     * @param dateTime  date and time of flight to recount
     * @return calculated price with affect of distance to date of flight from current date
     */
    @Override
    public double affectPriceByDate(double basePrice, LocalDateTime dateTime) {
        long daysUntilDeparture = LocalDateTime.from(LocalDateTime.now()).until(dateTime, ChronoUnit.DAYS);
        if (daysUntilDeparture > MAX_DAYS_TO_FLIGHT) {
            return basePrice;
        }
        return basePrice + MAX_PRICE_INCREASE * basePrice * (1 - daysUntilDeparture / MAX_DAYS_TO_FLIGHT);
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
