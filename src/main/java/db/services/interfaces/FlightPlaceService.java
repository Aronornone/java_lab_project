package db.services.interfaces;

import pojo.FlightPlace;
import pojo.Ticket;

import java.util.List;
import java.util.Optional;

public interface FlightPlaceService {
    void add(FlightPlace flightPlace);

    Optional<FlightPlace> get(long id);

    Optional<FlightPlace> getByFlightId(long id);

    void update(FlightPlace flightPlace);

    void revertSittingPlaces(List<Ticket> tickets);

    int getRandomSittingPlace(long flightId, boolean business);

    void delete(FlightPlace flightPlace);

    List<FlightPlace> getAll();
}
