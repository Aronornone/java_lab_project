package db.dao.interfaces;

import pojo.Airport;
import pojo.Flight;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightDAO {
    void add(Flight flight);
    Optional<Flight> get(long id);
    void update(Flight flight);
    void delete(Flight flight);
    List<Flight> getAll();
    List<Flight> getFlights(long departure, long arrival, String dateFrom, String dateTo,
                            int requiredSeats, boolean business, int numberOfPage);
    int getAmountFlights(long arrival, long departure, String dateFrom, String dateTo,
                         int requiredSeats, boolean business);
}
