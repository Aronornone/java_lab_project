package db.services.interfaces;

import pojo.Ticket;

import java.util.List;
import java.util.Optional;

public interface TicketService {
    void add(Ticket ticket);
    Optional<Ticket> get(long id);
    List<Ticket> getTicketsByInvoice(long invoiceId);
    void update(Ticket ticket);
    void updateTicketWhilePay(String[] ticketsIds, String[] passengerNames,
                              String[] passports, boolean[] luggages);
    boolean isEmptyWhilePayAndSave(String[] ticketsIds, String[] passengerNames,
                                   String[] passports, String[] luggages);
    void delete(Ticket ticket);
    List<Ticket> getAll();
}
