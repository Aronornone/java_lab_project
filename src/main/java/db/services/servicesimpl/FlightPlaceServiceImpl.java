package db.services.servicesimpl;

import db.dao.daoimpl.FlightPlaceDAOImpl;
import db.dao.interfaces.FlightPlaceDAO;
import db.services.interfaces.FlightPlaceService;
import pojo.FlightPlace;

import java.util.List;
import java.util.Optional;

public class FlightPlaceServiceImpl implements FlightPlaceService {
    private FlightPlaceDAO dao = new FlightPlaceDAOImpl();

    @Override
    public void add(FlightPlace flightPlaces) {
        dao.add(flightPlaces);
    }

    @Override
    public Optional<FlightPlace> get(long id) {
        return dao.get(id);
    }

    @Override
    public Optional<FlightPlace> getByFlightId(long flightId) {
        return dao.getByFlightId(flightId);
    }

    @Override
    public void update(FlightPlace flightPlaces) {
        dao.update(flightPlaces);
    }

    @Override
    public void delete(FlightPlace flightPlaces) {
        dao.delete(flightPlaces);
    }

    @Override
    public List<FlightPlace> getAll() {
        return dao.getAll();
    }
}
