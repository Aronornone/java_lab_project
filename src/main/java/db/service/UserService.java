package db.service;

import db.dao.UserDAO;
import pojo.User;

import java.util.List;
import java.util.Optional;

public class UserService implements UserDAO {
    @Override
    public long create(User user) {
        return 0;
    }

    @Override
    public Optional<User> get(int id) {
        return null;
    }

    @Override
    public void update(User user) {

    }

    @Override
    public void remove(User user) {

    }

    @Override
    public List<User> getAll() {
        return null;
    }
}
