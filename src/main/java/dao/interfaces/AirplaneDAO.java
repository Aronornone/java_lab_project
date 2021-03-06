package dao.interfaces;

import pojo.Airplane;

import java.util.List;
import java.util.Optional;

public interface AirplaneDAO {
    void add(Airplane airplane);
    Optional<Airplane> get(long id);
    void update(Airplane airplane);
    void delete(Airplane airplane);
    List<Airplane> getAll();
}
