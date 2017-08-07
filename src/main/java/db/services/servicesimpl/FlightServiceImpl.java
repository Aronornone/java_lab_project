package db.services.servicesimpl;

import db.dao.interfaces.FlightDAO;
import db.services.interfaces.FlightService;
import pojo.Flight;

import java.util.List;
import java.util.Optional;

public class FlightServiceImpl implements FlightService {
    private FlightDAO dao = new db.dao.daoimpl.FlightServiceImpl();

    @Override
    public void add(Flight flight) {
        dao.add(flight);
    }

    @Override
    public Optional<Flight> get(long id) {
        return dao.get(id);
    }

    @Override
    public void update(Flight flight) {
        dao.update(flight);
    }

    @Override
    public void delete(Flight flight) {
        dao.delete(flight);
    }

    @Override
    public List<Flight> getAll() {
        return dao.getAll();
    }
}
