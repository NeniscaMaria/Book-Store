package Service;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;

public interface Service<T> {

    String GET_ALL = "findAll";
    String ADD = "add";
    String UPDATE = "update";
    String REMOVE = "remove";
    String FIND_ONE = "findOne";

    Future<Set<T>> findAll() throws SQLException;
    Future<Optional<T>> remove(Long id) throws SQLException;
    Future<Optional<T>> add(T entity) throws SQLException;
    Future<Optional<T>> update(T entity) throws SQLException;
    Future<Optional<T>> findOne(Long id) throws SQLException;

}
