package db.dao;

import pojo.Airport;

import java.util.List;
import java.util.Optional;

public interface AirportDAO {
    long add(Airport airport);
    Optional<Airport> get(int id);
    void update(Airport airport);
    void remove(Airport airport);
    List<Airport> getAll();
}
