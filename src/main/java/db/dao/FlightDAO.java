package db.dao;

import pojo.Flight;

import java.util.List;
import java.util.Optional;

public interface FlightDAO {
    long create(Flight flight);
    Optional<Flight> get(int id);
    void update(Flight flight);
    void remove(Flight flight);
    List<Flight> getAll();
}
