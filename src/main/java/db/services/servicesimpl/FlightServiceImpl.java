package db.services.servicesimpl;

import db.dao.daoimpl.FlightDAOImpl;
import db.dao.interfaces.FlightDAO;
import db.services.interfaces.FlightService;
import pojo.Flight;

import java.util.List;
import java.util.Optional;

public final class FlightServiceImpl implements FlightService {
    private FlightDAO dao = new FlightDAOImpl();

    private final static FlightServiceImpl instance = new FlightServiceImpl();
    public static FlightServiceImpl getInstance() {
        return instance;
    }
    private FlightServiceImpl(){
    }

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

    @Override
    public List<Flight> getFlights(long departure, long arrival, String dateFrom, String dateTo, int requiredSeats, boolean business, int numberOfPage) {
        return dao.getFlights(departure,arrival,dateFrom,dateTo,requiredSeats,business,numberOfPage);
    }

    @Override
    public int getAmountFlights(long arrival, long departure, String dateFrom, String dateTo, int requiredSeats, boolean business) {
        return dao.getAmountFlights(arrival,departure,dateFrom,dateTo,requiredSeats,business);
    }
}
