package db.dao;

import pojo.Airplane;

import java.util.List;
import java.util.Optional;

public interface AirplaneDAO {
    long create(Airplane airplane);
    Optional<Airplane> get(long id);
    void update(Airplane airplane);
    void remove(Airplane airplane);
    List<Airplane> getAll();
}
