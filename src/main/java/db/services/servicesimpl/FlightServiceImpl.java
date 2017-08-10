package db.services.servicesimpl;

import db.dao.daoimpl.FlightDAOImpl;
import db.dao.interfaces.FlightDAO;
import db.services.interfaces.FlightService;
import org.apache.log4j.Logger;
import pojo.Flight;

import java.util.List;
import java.util.Optional;

public final class FlightServiceImpl implements FlightService {
    private static Logger log = Logger.getLogger("DBLogger");
    private final FlightDAO dao = FlightDAOImpl.getInstance();

    private final static FlightService instance = new FlightServiceImpl();
    public static FlightService getInstance() {
        log.info("getInstance(): Returning instance of FlightServiceImpl");
        return instance;
    }
    private FlightServiceImpl() {
    }

    @Override
    public void add(Flight flight) {
        log.info("add(airport): Delegating an 'add flight' query to DAO with the following 'flight' object: " + flight);
        dao.add(flight);
    }

    @Override
    public Optional<Flight> get(long id) {
        log.info("get(id): Delegating a 'get flight by id' query to DAO with the following 'id' = " + id);
        return dao.get(id);
    }

    @Override
    public void update(Flight flight) {
        log.info("update(flightPlaces): Delegating an 'update flight' query to DAO with the following 'flight' object: " + flight);
        dao.update(flight);
    }

    @Override
    public void delete(Flight flight) {
        log.info("delete(flight): Delegating a 'delete flight' query to DAO with the following 'flight' object: " + flight);
        dao.delete(flight);
    }

    @Override
    public List<Flight> getAll() {
        log.info("getAll(): Delegating a 'get all flights' query to DAO.");
        return dao.getAll();
    }

    @Override
    public List<Flight> getFlights(long departure, long arrival, String dateFrom, String dateTo, int requiredSeats, boolean business, int numberOfPage) {
        log.info("getFlights(...): Delegating a 'get flights by ...' query to DAO.");
        return dao.getFlights(departure, arrival, dateFrom, dateTo, requiredSeats, business, numberOfPage);
    }

    @Override
    public int getAmountFlights(long arrival, long departure, String dateFrom, String dateTo, int requiredSeats, boolean business) {
        log.info("getAmountFlights(...): Delegating a 'get amount of flights by ...' query to DAO.");
        return dao.getAmountFlights(arrival, departure, dateFrom, dateTo, requiredSeats, business);
    }
}
