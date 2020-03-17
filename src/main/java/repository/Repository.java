package repository;

import domain.validators.ValidatorException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.Optional;

public interface Repository<ID, T extends domain.BaseEntity<ID>> {
    /*
        DESCR:Find the entity with the given {@code id}.
        PRE:param id must be not null.
        POST: return an {@code Optional} encapsulating the entity with the given id.
        THROWS: IllegalArgumentException if the given id is null.
     */
    Optional<T> findOne(ID id);

    Iterable<T> findAll();//return all entities

    /*
     DESCR: Saves the given entity.
     PRE: param entity must not be null.
     POST: return an {@code Optional} - null if the entity was saved otherwise (e.g. id already exists) returns the entity.
     THROWS: IllegalArgumentException if the given entity is null.
            ValidatorException if the entity is not valid.
     */
    Optional<T> save(T entity) throws ValidatorException, ParserConfigurationException, IOException, SAXException, TransformerException;

    /*DESCR:Removes the entity with the given id.
     PRE: param id must not be null.
     POST: return an {@code Optional} - null if there is no entity with the given id, otherwise the removed entity.
     THROWS: IllegalArgumentException if the given id is null.
     */
    Optional<T> delete(ID id);

    /*DESCR:Updates the given entity.
     PRE: param entity must not be null.
     POST: @return an {@code Optional} - null if the entity was updated otherwise (e.g. id does not exist) returns the
              entity.
     THROWS: IllegalArgumentException if the given entity is null.
             ValidatorException if the entity is not valid.
     */
    Optional<T> update(T entity) throws ValidatorException;

    void removeEntitiesWithClientID(ID id) throws ParserConfigurationException, IOException, SAXException;
}
