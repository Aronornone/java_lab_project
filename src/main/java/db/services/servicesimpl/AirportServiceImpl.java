package db.services.servicesimpl;

import db.dao.daoimpl.AirportDAOImpl;
import db.dao.interfaces.AirportDAO;
import db.services.interfaces.AirportService;
import pojo.Airport;

import java.util.List;
import java.util.Optional;

public final class AirportServiceImpl implements AirportService {
    private AirportDAO dao = AirportDAOImpl.getInstance();

    private final static AirportService instance = new AirportServiceImpl();

    public static AirportService getInstance() {
        return instance;
    }

    private AirportServiceImpl() {
    }

    @Override
    public void add(Airport airport) {
        dao.add(airport);
    }

    @Override
    public Optional<Airport> get(long id) {
        return dao.get(id);
    }

    @Override
    public void update(Airport airport) {
        dao.update(airport);
    }

    @Override
    public void delete(Airport airport) {
        dao.delete(airport);
    }

    @Override
    public List<Airport> getAll() {
        return dao.getAll();
    }
}