package services.interfaces;

import pojo.Airport;

import java.util.List;
import java.util.Optional;

public interface AirportService {
    void add(Airport airport);
    Optional<Airport> get(long id);
    void update(Airport airport);
    void delete(Airport airport);
    List<Airport> getAll();
}
