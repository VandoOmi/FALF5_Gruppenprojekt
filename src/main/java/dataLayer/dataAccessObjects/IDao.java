package dataLayer.dataAccessObjects;

import java.util.List;
import java.util.Optional;

public interface IDao<T, ID> {

    void create();

    void create(T entity);

    Optional<T> findById(ID id);

    List<T> findAll();

    void update(T entity);

    void deleteById(ID id);
}
