package db.dao;

import pojo.Airport;
import pojo.Flight;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface FlightDAO {
    long create(Flight flight);
    Optional<Flight> get(int id);
    void update(Flight flight);
    void remove(Flight flight);
    List<Flight> getAll();
}
