package common.command;

import common.domain.BaseEntity;

import java.util.Optional;
import java.util.concurrent.Future;

public interface CommandService<ID, T extends BaseEntity<ID>> {

    Future<Optional<T>> findOne(ID id);
    Future<Iterable<T>> findAll();
    Future<Optional<T>> save(T entity);
    Future<Optional<T>> delete(ID id);
    Future<Optional<T>> update(T entity);


}
