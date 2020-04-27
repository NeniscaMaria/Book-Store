package repository.DataBase;

import domain.Book;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import org.postgresql.util.PSQLException;
import org.xml.sax.SAXException;
import repository.SortingRepository;
import repository.DataBase.implementation.Sort;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class BookDataBaseRepository implements SortingRepository<Long, Book> {

//    private static final String URL = "jdbc:postgresql://localhost:5432/bookstore";
    private static final String URL = "jdbc:postgresql://localhost:5432/bookstore?currentSchema=bookstore&user=postgres&password=password";
    private static final String USER = System.getProperty("postgres");
//    private static final String URL = "jdbc:postgresql://localhost:5432/bookstore";

    private static final String PASSWORD = System.getProperty("password");
    private Validator<Book> validator;

    public BookDataBaseRepository(Validator<Book> validator) {
        this.validator=validator;
    }

    @Override
    public Iterable<Book> findAll(Sort sort) {
        return null;
    }

    @Override
    public Optional<Book> findOne(Long id) {
        String cmd = "select * from Books where id = ?";
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(cmd);
            preparedStatement.setLong(1, id);

            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                String serialNumber = resultSet.getString("serialNumber");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                int year = resultSet.getInt("year");
                double price = resultSet.getDouble("price");
                int stock = resultSet.getInt("inStock");

                Book b = new Book(serialNumber, title, author, year, price, stock);
                b.setId(id);

                return Optional.of(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    @Override
    public List<Book> findAll() {
        List<Book> result = new ArrayList<>();
        String cmd = "select * from Books";

        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(cmd);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("id");
                String serialNumber = resultSet.getString("serialNumber");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                int year = resultSet.getInt("year");
                double price = resultSet.getDouble("price");
                int stock = resultSet.getInt("inStock");

                Book b = new Book(serialNumber, title, author, year, price, stock);
                b.setId(id);
                result.add(b);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;

    }

    @Override
    public Optional<Book> save(Book entity) throws ValidatorException, ParserConfigurationException, IOException, SAXException, TransformerException {
        String cmd = "insert into Books(id, serialNumber, title, author, year, price, inStock)" +
                "values(?, ?, ?, ?, ?, ?, ?)";

        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(cmd);
            preparedStatement.setLong(1, entity.getId());
            preparedStatement.setString(2, entity.getSerialNumber());
            preparedStatement.setString(3, entity.getTitle());
            preparedStatement.setString(4, entity.getAuthor());
            preparedStatement.setInt(5, entity.getYear());
            preparedStatement.setDouble(6, entity.getPrice());
            preparedStatement.setInt(7, entity.getInStock());
            try{
                preparedStatement.executeUpdate();
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
    public Optional<Book> delete(Long id) {
        String sql = "delete from Books where id=?";

        Connection connection = null;
        try {
            Optional<Book> b = findOne(id);
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            int rows = preparedStatement.executeUpdate();
            if (rows > 0){
                return b;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

//        return findOne(id);
        return Optional.empty();
    }

    @Override
    public Optional<Book> update(Book entity) throws ValidatorException {

        String cmd = "update Books " +
                "set serialNumber = ?, " +
                "title = ?, " +
                "author = ?, " +
                "year = ?, " +
                "price = ?, " +
                "inStock = ? " +
                "where id = ?;";

        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(cmd);
            preparedStatement.setString(1, entity.getSerialNumber());
            preparedStatement.setString(2, entity.getTitle());
            preparedStatement.setString(3, entity.getAuthor());
            preparedStatement.setInt(4, entity.getYear());
            preparedStatement.setDouble(5, entity.getPrice());
            preparedStatement.setInt(6, entity.getInStock());
            preparedStatement.setLong(7, entity.getId());
            int rows = preparedStatement.executeUpdate();
            if (rows > 0){
                return Optional.of(entity);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }


        return Optional.empty();
    }



    @Override
    public void removeEntitiesWithClientID(Long id) throws ParserConfigurationException, IOException, SAXException {

    }
}
