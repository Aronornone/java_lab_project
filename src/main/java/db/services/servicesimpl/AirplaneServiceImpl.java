package db.services.servicesimpl;

import db.dao.daoimpl.AirplaneDAOImpl;
import db.dao.interfaces.AirplaneDAO;
import db.services.interfaces.AirplaneService;
import pojo.Airplane;

import java.util.List;
import java.util.Optional;

public final class AirplaneServiceImpl implements AirplaneService {
    private AirplaneDAO dao = new AirplaneDAOImpl();

    private final static AirplaneServiceImpl instance = new AirplaneServiceImpl();
    public static AirplaneServiceImpl getInstance() {
        return instance;
    }
    private AirplaneServiceImpl(){
    }

    @Override
    public void add(Airplane airplane) {
        dao.add(airplane);
    }

    @Override
    public Optional<Airplane> get(long id) {
        return dao.get(id);
    }

    @Override
    public void update(Airplane airplane) {
        dao.update(airplane);
    }

    @Override
    public void delete(Airplane airplane) {
        dao.delete(airplane);
    }

    @Override
    public List<Airplane> getAll() {
        return dao.getAll();
    }
}
