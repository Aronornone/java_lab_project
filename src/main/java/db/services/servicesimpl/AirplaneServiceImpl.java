package db.services.servicesimpl;

import db.dao.daoimpl.AirplaneDAOImpl;
import db.dao.interfaces.AirplaneDAO;
import db.services.interfaces.AirplaneService;
import org.apache.log4j.Logger;
import pojo.Airplane;

import java.util.List;
import java.util.Optional;

public final class AirplaneServiceImpl implements AirplaneService {
    private static Logger log = Logger.getLogger("DBLogger");
    private final AirplaneDAO dao = AirplaneDAOImpl.getInstance();

    private final static AirplaneService instance = new AirplaneServiceImpl();
    public static AirplaneService getInstance() {
        log.info("getInstance(): Returning instance of AirplaneServiceImpl");
        return instance;
    }
    private AirplaneServiceImpl(){
    }

    @Override
    public void add(Airplane airplane) {
        log.info("add(airplane): Delegating an 'add airplane' query to DAO with the following 'airplane' object: " + airplane);
        dao.add(airplane);
    }

    @Override
    public Optional<Airplane> get(long id) {
        log.info("get(id): Delegating a 'get airplane by id' query to DAO with the following 'id' = " + id);
        return dao.get(id);
    }

    @Override
    public void update(Airplane airplane) {
        log.info("update(airplane): Delegating an 'update airplane' query to DAO with the following 'airplane' object: " + airplane);
        dao.update(airplane);
    }

    @Override
    public void delete(Airplane airplane) {
        log.info("delete(airplane): Delegating a 'delete airplane' query to DAO with the following 'airplane' object: " + airplane);
        dao.delete(airplane);
    }

    @Override
    public List<Airplane> getAll() {
        log.info("getAll(): Delegating a 'get all airplanes' query to DAO.");
        return dao.getAll();
    }
}
