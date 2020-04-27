package SDI.server.repository.DataBase;

import SDI.server.repository.SortingRepository;
import SDI.server.validators.Validator;
import domain.Sort;
import domain.ValidatorException;
import domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.*;

@Component
public class BookDataBaseRepository implements SortingRepository<Long, Book> {

    @Autowired
    private Validator<Book> valid;
    @Autowired
    private JdbcOperations jdbcOperations;
    @Autowired
    private Sort sort;

    public BookDataBaseRepository() throws RemoteException {

    }

    @Override
    public List<Book> findAll(Sort sort) throws SQLException, RemoteException {

        return sort.sortBook();

    }

    @Override
    public Optional<Book> findOne(Long id) throws SQLException {
        String cmd = "select * from Books where id=?";

        return Optional.ofNullable(jdbcOperations.queryForObject(cmd, new Object[]{id}, (results, row) -> {
            Long idbook = results.getLong("id");
            String sn = results.getString("serialNumber");
            String title = results.getString("title");
            String author = results.getString("author");
            int year = results.getInt("year");
            double price = results.getDouble("price");
            int stock = results.getInt("instock");
            Book b = new Book(sn, title, author, year, price, stock);
            b.setId(id);
            return b;
        }));
    }

    @Override
    public List<Book> findAll() throws SQLException {
        String cmd = "select * from Books";
        return jdbcOperations.query(cmd, (results, row) -> {
            Long id = results.getLong("id");
            String sn = results.getString("serialNumber");
            String title = results.getString("title");
            String author = results.getString("author");
            int year = results.getInt("year");
            double price = results.getDouble("price");
            int stock = results.getInt("instock");
            Book b = new Book(sn, title, author, year, price, stock);
            b.setId(id);
            return b;
        });
    }

    @Override
    public int save(Book entity) throws ValidatorException, ParserConfigurationException, IOException, SAXException, TransformerException, SQLException {
        String cmd = "insert into Books(id, serialNumber, title, author, year, price, instock)" +
                "values(?, ?, ?, ?, ?, ?, ?)";
        valid.validate(entity);
        return jdbcOperations.update(cmd, entity.getId(), entity.getSerialNumber(), entity.getTitle(),
                entity.getAuthor(), entity.getYear(), entity.getPrice(), entity.getInStock());
    }


    @Override
    public int delete(Long id) throws SQLException {
        String cmd = "delete from Books where id=?";
        return jdbcOperations.update(cmd, id);
    }

    @Override
    public int update(Book entity) throws ValidatorException, SQLException {
        String cmd = "update Books set serialNumber = ?, " +
                "title = ?, " +
                "author = ?, " +
                "year = ?, " +
                "price = ?, " +
                "instock = ? " +
                "where id = ?";
        valid.validate(entity);
        return jdbcOperations.update(cmd, entity.getSerialNumber(), entity.getTitle(),
                entity.getAuthor(), entity.getYear(), entity.getPrice(), entity.getInStock(), entity.getId());

    }



    @Override
    public void removeEntitiesWithClientID(Long id) throws ParserConfigurationException, IOException, SAXException {

    }


}
