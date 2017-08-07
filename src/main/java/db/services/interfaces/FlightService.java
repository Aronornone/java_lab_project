package db.services.interfaces;

import pojo.Flight;

import java.util.List;
import java.util.Optional;

public interface FlightService {
    void add(Flight flight);
    Optional<Flight> get(long id);
    void update(Flight flight);
    void delete(Flight flight);
    List<Flight> getAll();
}
