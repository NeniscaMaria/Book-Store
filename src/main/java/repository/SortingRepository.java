package repository;


import domain.BaseEntity;
import repository.DataBase.implementation.Sort;

import java.io.Serializable;


public interface SortingRepository<ID extends Serializable,
        T extends BaseEntity<ID>>
        extends Repository<ID, T> {

    Iterable<T> findAll(Sort sort);

    //TODO: insert sorting-related code here
}