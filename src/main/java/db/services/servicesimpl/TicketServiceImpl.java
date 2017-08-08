package db.services.servicesimpl;

import db.dao.daoimpl.TicketDAOImpl;
import db.dao.interfaces.TicketDAO;
import db.services.interfaces.TicketService;
import pojo.Ticket;

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
