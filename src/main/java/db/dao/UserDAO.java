package db.dao;

import pojo.User;

import java.util.List;
import java.util.Optional;

public interface UserDAO {
    long create(User user);
    Optional<User> get(long id);
    Optional<User> get(String email);
    void update(User user);
    void remove(User user);
    List<User> getAll();
}
