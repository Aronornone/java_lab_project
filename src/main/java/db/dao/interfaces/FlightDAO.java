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
}
