package services.servicesimpl;

import dao.daoimpl.AirportDAOImpl;
import dao.interfaces.AirportDAO;
import org.apache.log4j.Logger;
import pojo.Airport;
import services.interfaces.AirportService;

import java.util.List;
import java.util.Optional;

public final class AirportServiceImpl implements AirportService {
    private static Logger log = Logger.getLogger("DBLogger");
    private final AirportDAO dao = AirportDAOImpl.getInstance();

    private final static AirportService instance = new AirportServiceImpl();

    public static AirportService getInstance() {
        log.info("getInstance(): Returning instance of AirportServiceImpl");
        return instance;
    }

    private AirportServiceImpl() {
    }

    @Override
    public void add(Airport airport) {
        log.info("add(airport): Delegating an 'add airport' query to DAO with the following 'airport' object: " + airport);
        dao.add(airport);
    }

    @Override
    public Optional<Airport> get(long id) {
        log.info("get(id): Delegating a 'get airport by id' query to DAO with the following 'id' = " + id);
        return dao.get(id);
    }

    @Override
    public void update(Airport airport) {
        log.info("update(airport): Delegating an 'update airport' query to DAO with the following 'airport' object: " + airport);
        dao.update(airport);
    }

    @Override
    public void delete(Airport airport) {
        log.info("delete(airport): Delegating a 'delete airport' query to DAO with the following 'airport' object: " + airport);
        dao.delete(airport);
    }

    @Override
    public List<Airport> getAll() {
        log.info("getAll(): Delegating a 'get all airports' query to DAO.");
        return dao.getAll();
    }
}
