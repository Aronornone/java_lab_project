package db.service;

import db.dao.TicketDao;
import pojo.Ticket;

import java.util.List;
import java.util.Optional;

public class TicketService implements TicketDao {
    @Override
    public long create(Ticket ticket) {
        return 0;
    }

    @Override
    public Optional<Ticket> get(int id) {
        return null;
    }

    @Override
    public void update(Ticket ticket) {

    }

    @Override
    public void remove(Ticket ticket) {

    }

    @Override
    public List<Ticket> getAll() {
        return null;
    }
}
