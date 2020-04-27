package SDI.server.repository.DataBase;

import SDI.server.repository.Repository;
import SDI.server.repository.SortingRepository;
import SDI.server.validators.PurchaseValidator;
import domain.Sort;
import domain.ValidatorException;
import domain.Book;
import domain.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.List;
import java.util.Optional;

@Component
public class PurchaseDataBaseRepository implements SortingRepository<Long, Purchase> {

    @Autowired
    private JdbcOperations jdbcOperations;
    @Autowired
    private PurchaseValidator validator;
    @Autowired
    private Repository<Long, Book> books;

    public PurchaseDataBaseRepository(){}

    @Override
    public Iterable<Purchase> findAll(Sort sort) throws SQLException, RemoteException {
        return sort.sortPurchase();
    }

    @Override
    public Optional<Purchase> findOne(Long id) throws SQLException{
        String cmd = "select * from Purchases where id = ?";
        return Optional.ofNullable(jdbcOperations.queryForObject(cmd, new Object[]{id}, (results, row) -> {
            Long idp = results.getLong("id");
            Long idClient = results.getLong("clientid");
            Long idBook = results.getLong("bookid");
            int nrBooks = results.getInt("nrbooks");
            Purchase purchase = new Purchase(idClient, idBook, nrBooks);
            purchase.setId(idp);
            return purchase;
        }));
    }

    @Override
    public List<Purchase> findAll() throws SQLException {
        String cmd = "select * from Purchases";
        return jdbcOperations.query(cmd, (results, row) -> {
            Long id = results.getLong("id");
            Long idClient = results.getLong("clientid");
            Long idBook = results.getLong("bookid");
            int nrBooks = results.getInt("nrbooks");
            Purchase purchase = new Purchase(idClient, idBook, nrBooks);
            purchase.setId(id);
            return purchase;
        });

    }

    @Override
    public int save(Purchase entity) throws ValidatorException, ParserConfigurationException, IOException, SAXException, TransformerException, SQLException {
        String cmd = "insert into Purchases(id, clientid, bookid, nrbooks) values(?, ?, ?, ?)";
        validator.validate(entity);
        //update stock
        books.findOne(entity.getBookID()).ifPresent(book->{
            book.setInStock(book.getInStock() - entity.getNrBooks());
            try {
                books.update(book);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return jdbcOperations.update(cmd, entity.getId(), entity.getClientID(), entity.getBookID(), entity.getNrBooks());
    }


    @Override
    public int delete(Long id) throws SQLException {
        String cmd = "delete from Purchases where id=?";
        findOne(id).ifPresent(entity->{
            try {
                books.findOne(entity.getBookID()).ifPresent(book->{
                    book.setInStock(book.getInStock() + entity.getNrBooks());
                    try {
                        books.update(book);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return jdbcOperations.update(cmd, id);
    }

    @Override
    public int update(Purchase entity) throws ValidatorException, SQLException {

        int oldnrbooks = findOne(entity.getId()).get().getNrBooks();
        String cmd = "update Purchases set clientid = ?, bookid = ?, nrbooks = ? where id = ?";
        validator.validate(entity);
        books.findOne(entity.getBookID()).ifPresent(book->{
            book.setInStock(book.getInStock() - (entity.getNrBooks() - oldnrbooks));
            try {
                books.update(book);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        });
        return jdbcOperations.update(cmd, entity.getClientID(), entity.getBookID(), entity.getNrBooks(), entity.getId());


    }

    @Override
    public void removeEntitiesWithClientID(Long clientID) throws ParserConfigurationException, IOException, SAXException {
        try {
            findAll().forEach(p-> {
                if (p.getClientID().equals(clientID)) {
                    try {
                        delete(p.getId());
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
