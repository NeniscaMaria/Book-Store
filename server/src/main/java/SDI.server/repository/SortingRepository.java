package SDI.server.repository;

import domain.BaseEntity;
import domain.Sort;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.sql.SQLException;


public interface SortingRepository<ID extends Serializable,
        T extends BaseEntity<ID>>
        extends Repository<ID, T> {

    Iterable<T> findAll(Sort sort) throws SQLException, RemoteException;

    //TODO: insert sorting-related code here
}