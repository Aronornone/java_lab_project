package db.services.servicesimpl;

import db.dao.daoimpl.TicketDAOImpl;
import db.dao.interfaces.TicketDAO;
import db.services.interfaces.TicketService;
import pojo.Ticket;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public final class TicketServiceImpl implements TicketService {
    private TicketDAO dao = TicketDAOImpl.getInstance();

    private final static TicketService instance = new TicketServiceImpl();

    public static TicketService getInstance() {
        return instance;
    }

    private TicketServiceImpl() {
    }

    @Override
    public void add(Ticket ticket) {
        dao.add(ticket);
    }

    @Override
    public Optional<Ticket> get(long id) {
        return dao.get(id);
    }

    @Override
    public List<Ticket> getTicketsByInvoice(long invoiceId) {
        return dao.getTicketsByInvoice(invoiceId);
    }

    @Override
    public void update(Ticket ticket) {
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
        TicketService ts = TicketServiceImpl.getInstance();
        boolean empty = false;
        if (ticketsIds != null && passengerNames != null && passports != null) {
            List<String> ticketsList = Arrays.asList(ticketsIds);
            List<String> passengersList = Arrays.asList(passengerNames);
            List<String> passportsList = Arrays.asList(passports);

            for (String string : ticketsList) {
                if (string.isEmpty()) empty = true;
            }
            for (String string : passengersList) {
                if (string.isEmpty()) empty = true;
            }
            for (String string : passportsList) {
                if (string.isEmpty()) empty = true;
            }

            List<String> luggagesList;
            boolean[] luggagesBoolean = new boolean[ticketsList.size()];

            if (luggages != null) {
                luggagesList = Arrays.asList(luggages);
                for (int i = 0; i < luggagesList.size(); i++) {
                    luggagesBoolean[i] = (luggagesList.get(i).equals("luggage"));
                }
            }
            ts.updateTicketWhilePay(ticketsIds, passengerNames, passports, luggagesBoolean);
        } else empty = true;
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
        for (int i = 0; i < ticketsIds.length; i++) {
            TicketService ts = TicketServiceImpl.getInstance();
            Ticket ticketToUpdate = ts.get(Long.parseLong(ticketsIds[i])).get();
            ticketToUpdate.setPassengerName(passengerNames[i]);
            ticketToUpdate.setPassport(passports[i]);
            ticketToUpdate.setLuggage(luggages[i]);
            ts.update(ticketToUpdate);
        }
    }

    @Override
    public void delete(Ticket ticket) {
        dao.delete(ticket);
    }

    @Override
    public List<Ticket> getAll() {
        return dao.getAll();
    }
}
