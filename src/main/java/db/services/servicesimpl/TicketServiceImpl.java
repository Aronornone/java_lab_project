package db.services.servicesimpl;

import db.dao.interfaces.TicketDao;
import db.services.interfaces.TicketService;
import pojo.*;

import java.util.List;
import java.util.Optional;

public class TicketServiceImpl implements TicketService {
    private TicketDao dao = new db.dao.daoimpl.TicketServiceImpl();

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

    @Override
    public void delete(Ticket ticket) {
        dao.delete(ticket);
    }

    @Override
    public List<Ticket> getAll() {
        return dao.getAll();
    }
}
