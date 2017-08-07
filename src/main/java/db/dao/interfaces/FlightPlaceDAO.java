package db.dao.interfaces;

import pojo.FlightPlace;

import java.util.List;
import java.util.Optional;

public interface FlightPlaceDAO {
    void add(FlightPlace flightPlace);
    Optional<FlightPlace> get(long id);
    Optional<FlightPlace> getByFlightId(long id);
    void update(FlightPlace flightPlace);
    void delete(FlightPlace flightPlace);
    List<FlightPlace> getAll();
}
