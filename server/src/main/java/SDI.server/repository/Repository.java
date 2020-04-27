package SDI.server.repository;

import domain.ValidatorException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface Repository<ID extends Serializable, T extends domain.BaseEntity<ID>> {
    /*
        DESCR:Find the entity with the given {@code id}.
        PRE:param id must be not null.
        POST: return an {@code Optional} encapsulating the entity with the given id.
        THROWS: IllegalArgumentException if the given id is null.
     */
    Optional<T> findOne(ID id) throws SQLException;

    List<T> findAll() throws SQLException;//return all entities

    int save(T entity) throws ValidatorException, ParserConfigurationException, IOException, SAXException, TransformerException, SQLException;

    int delete(ID id) throws SQLException;

    int update(T entity) throws ValidatorException, SQLException;

    void removeEntitiesWithClientID(ID id) throws ParserConfigurationException, IOException, SAXException;

}
