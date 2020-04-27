package repository.DataBase;

import domain.Book;
import domain.Purchase;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import org.postgresql.util.PSQLException;
import org.xml.sax.SAXException;
import repository.DataBase.implementation.Sort;
import repository.Repository;
import repository.SortingRepository;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PurchaseDataBaseRepository implements SortingRepository<Long, Purchase> {

    private static final String URL = "jdbc:postgresql://localhost:5432/bookstore?currentSchema=bookstore&user=postgres&password=password";
    private static final String USER = System.getProperty("postgres");
    private static final String PASSWORD = System.getProperty("password");
    private Validator<Purchase> validator;
    private Repository<Long, Book> books;

    public PurchaseDataBaseRepository(Validator<Purchase> validator, Repository<Long, Book> books) {
        this.validator = validator;
        this.books = books;
    }


    @Override
    public Iterable<Purchase> findAll(Sort sort) {
        return null;
    }

    @Override
    public Optional<Purchase> findOne(Long id) {
        String cmd = "select * from Purchases where id = ?";
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(cmd);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                Long clientID = resultSet.getLong("clientID");
                Long bookID = resultSet.getLong("bookID");
                int nrBooks = resultSet.getInt("nrBooks");

                Purchase p = new Purchase(clientID, bookID, nrBooks);
                p.setId(id);

                return Optional.of(p);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Purchase> findAll() {
        List<Purchase> result = new ArrayList<>();
        String cmd = "select * from Purchases";

        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(cmd);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                Long clientID = resultSet.getLong("clientID");
                Long bookID = resultSet.getLong("bookID");
                int nrBooks = resultSet.getInt("nrBooks");

                Purchase p = new Purchase(clientID, bookID, nrBooks);
                p.setId(id);
                result.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;

    }

    @Override
    public Optional<Purchase> save(Purchase entity) throws ValidatorException, ParserConfigurationException, IOException, SAXException, TransformerException {
        String cmd = "insert into Purchases(id, clientID, bookID, nrBooks)" +
                "values(?, ?, ?, ?)";

        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(cmd);
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setLong(2, entity.getClientID());
            preparedStatement.setLong(3, entity.getBookID());
            preparedStatement.setInt(4, entity.getNrBooks());

            try{
                preparedStatement.executeUpdate();
                Optional<Book> book = books.findOne(entity.getBookID());
                book.ifPresent(b->{
                    b.setInStock(b.getInStock() - entity.getNrBooks());
                    try {
                        books.update(b);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
            }
            catch (PSQLException e){
                return Optional.of(entity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }


    @Override
    public Optional<Purchase> delete(Long id) {
        String sql = "delete from Purchases where id=?";

        Connection connection = null;
        try {
            Optional<Purchase> p = findOne(id);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            int rows = preparedStatement.executeUpdate();
            if (rows > 0){

                Optional<Book> book = books.findOne(p.get().getBookID());
                book.ifPresent(b->{
                    b.setInStock(b.getInStock() + p.get().getNrBooks());
                    try {
                        books.update(b);
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                });
                return p;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    @Override
    public Optional<Purchase> update(Purchase entity) throws ValidatorException {

        String cmd = "update Purchases " +
                "set clientID = ?, " +
                "bookID = ?, " +
                "nrBooks = ? " +
                "where id = ?;";

        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);

            Optional<Purchase> pur = findOne(entity.getId());

            pur.ifPresent(p->{
                int oldBooks = p.getNrBooks();
                try {
                    PreparedStatement preparedStatement = connection.prepareStatement(cmd);
                    preparedStatement.setLong(1, entity.getClientID());
                    preparedStatement.setLong(2, entity.getBookID());
                    preparedStatement.setInt(3, entity.getNrBooks());
                    preparedStatement.setLong(4, entity.getId());
                    preparedStatement.executeUpdate();

                    Book book = books.findOne(entity.getBookID()).get();
                    book.setInStock(book.getInStock() - (entity.getNrBooks() - oldBooks));

                    books.update(book);

                } catch (SQLException e) {
                    e.printStackTrace();
                }

            });
            return pur;

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return Optional.empty();
    }

    @Override
    public void removeEntitiesWithClientID(Long clientID) throws ParserConfigurationException, IOException, SAXException {

        findAll().forEach(p-> {
            if (p.getClientID().equals(clientID))
                delete(p.getId());
        });

    }
}
