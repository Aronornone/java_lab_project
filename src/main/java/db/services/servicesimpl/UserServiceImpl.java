package db.services.servicesimpl;

import db.dao.daoimpl.UserDAOImpl;
import db.dao.interfaces.UserDAO;
import db.services.interfaces.UserService;
import pojo.User;

import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private UserDAO dao = new UserDAOImpl();

    @Override
    public void add(User user) {
        dao.add(user);
    }

    @Override
    public Optional<User> get(long id) {
        return dao.get(id);
    }

    @Override
    public Optional<User> get(String email) {
        return dao.get(email);
    }

    @Override
    public void update(User user) {
        dao.update(user);
    }

    @Override
    public void delete(User user) {
        dao.delete(user);
    }

    @Override
    public List<User> getAll() {
        return dao.getAll();
    }
}
