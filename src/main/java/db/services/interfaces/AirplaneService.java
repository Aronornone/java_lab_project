package db.services.interfaces;

import pojo.Airplane;

import java.util.List;
import java.util.Optional;

public interface AirplaneService {
    void add(Airplane airplane);
    Optional<Airplane> get(long id);
    void update(Airplane airplane);
    void delete(Airplane airplane);
    List<Airplane> getAll();
}
