package db.dao;

import pojo.FlightPlace;

import java.util.List;
import java.util.Optional;

public interface FlightPlaceDAO {
    long create(FlightPlace flightPlace);
    Optional<FlightPlace> get(int id);
    Optional<FlightPlace> getByFlightId(int id);
    void update(FlightPlace flightPlace);
    void remove(FlightPlace flightPlace);
    List<FlightPlace> getAll();
}
