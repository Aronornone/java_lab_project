package services.servicesimpl;

import dao.daoimpl.UserDAOImpl;
import dao.interfaces.UserDAO;
import org.apache.log4j.Logger;
import pojo.User;
import services.interfaces.UserService;

import java.util.List;
import java.util.Optional;

public final class UserServiceImpl implements UserService {
    private static Logger log = Logger.getLogger("DBLogger");
    private final UserDAO dao = UserDAOImpl.getInstance();

    private final static UserService instance = new UserServiceImpl();

    public static UserService getInstance() {
        log.info("getInstance(): Returning instance of UserServiceImpl");
        return instance;
    }

    private UserServiceImpl() {
    }

    @Override
    public void add(User user) {
        log.info("add(user): Delegating an 'add user' query to DAO with the following 'user' object: " + user);
        dao.add(user);
    }

    @Override
    public Optional<User> get(long id) {
        log.info("get(id): Delegating a 'get user by id' query to DAO with the following 'id' = " + id);
        return dao.get(id);
    }

    @Override
    public Optional<User> get(String email) {
        log.info("get(id): Delegating a 'get user by email' query to DAO with the following 'email' = " + email);
        return dao.get(email);
    }

    @Override
    public void update(User user) {
        log.info("update(user): Delegating an 'update user' query to DAO with the following 'user' object: " + user);
        dao.update(user);
    }

    @Override
    public void delete(User user) {
        log.info("delete(user): Delegating a 'delete user' query to DAO with the following 'user' object: " + user);
        dao.delete(user);
    }

    @Override
    public List<User> getAll() {

        return dao.getAll();
    }
}
