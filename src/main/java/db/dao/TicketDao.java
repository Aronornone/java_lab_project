package db.dao;

import pojo.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketDao {
    long create(Ticket ticket);
    Optional<Ticket> get(long id);
    void update(Ticket ticket);
    void remove(Ticket ticket);
    List<Ticket> getAll();
}
