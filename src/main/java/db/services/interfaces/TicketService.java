package db.services.interfaces;

import pojo.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    void add(Ticket ticket);
    Optional<Ticket> get(long id);
    List<Ticket> getTicketsByInvoice(long invoiceId);
    void update(Ticket ticket);
    void delete(Ticket ticket);
    List<Ticket> getAll();
}
