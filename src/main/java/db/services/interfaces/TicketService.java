package db.services.interfaces;

import pojo.Ticket;

import java.time.LocalDateTime;
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
    double recountPrice(double basePrice, LocalDateTime dateTime, boolean business);
    double affectByBusiness(double basePrice);
    double affectByLuggage(double basePrice);
    double defectByLuggage(double basePrice);
    double affectPriceByPercents(double basePrice, int percents);
    double affectPriceByDate(double basePrice, LocalDateTime dateTime);

    void delete(Ticket ticket);
    List<Ticket> getAll();
}
