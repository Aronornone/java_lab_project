package db.service;

import db.dao.FlightPlaceDAO;
import pojo.FlightPlace;

import java.util.List;
import java.util.Optional;

public class FlightPlaceService implements FlightPlaceDAO {
    @Override
    public long create(FlightPlace flightPlace) {
        return 0;
    }

    @Override
    public Optional<FlightPlace> get(int id) {
        return null;
    }

    @Override
    public void update(FlightPlace flightPlace) {

    }

    @Override
    public void remove(FlightPlace flightPlace) {

    }

    @Override
    public List<FlightPlace> getAll() {
        return null;
    }
}
