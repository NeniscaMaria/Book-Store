package repository.DataBase;

import domain.Book;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import org.xml.sax.SAXException;
import repository.SortingRepository;
import repository.DataBase.implementation.Sort;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.*;

public class BookDataBaseRepository implements SortingRepository<Long, Book> {

    private static final String URL = "jdbc:postgresql://localhost:5432/bookstore";
    private static final String USER = System.getProperty("username");
    private static final String PASSWORD = System.getProperty("password");
    protected Map<Long, Book> entities;
    private Validator<Book> validator;

    public BookDataBaseRepository(Validator<Book> validator) {
        this.validator=validator;
        entities = new HashMap<>();
    }

    @Override
    public Iterable<Book> findAll(Sort sort) {
        return null;
    }

    @Override
    public Optional<Book> findOne(Long id) {
        String cmd = "select * from Books where id = ?";

        return Optional.empty();
    }

    @Override
    public Iterable<Book> findAll() {
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
                int stock = resultSet.getInt("stock");

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
        String cmd = "insert into Books(serialNumber, title, author, year, price, stock)" +
                "values(?, ?, ?, ?, ?, ?)";

        saveToDataBase(entity, cmd);
//        try {
//            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//            PreparedStatement preparedStatement = connection.prepareStatement(cmd);
//            preparedStatement.setString(1, entity.getSerialNumber());
//            preparedStatement.setString(2, entity.getTitle());
//            preparedStatement.setString(3, entity.getAuthor());
//            preparedStatement.setInt(4, entity.getYear());
//            preparedStatement.setDouble(5, entity.getPrice());
//            preparedStatement.setInt(6, entity.getInStock());
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        return Optional.empty();
    }


    @Override
    public Optional<Book> delete(Long id) {
        String sql = "delete from Books where id=?";

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement =
                    connection.prepareStatement(sql);
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return Optional.empty();
    }

    @Override
    public Optional<Book> update(Book entity) throws ValidatorException {

        String cmd = "update Books" +
                "set serialNumber = ?," +
                "title = ?" +
                "author = ?" +
                "year = ?" +
                "price = ?" +
                "stock = ?" +
                "where id = ?";

        saveToDataBase(entity, cmd);


        return Optional.empty();
    }

    private void saveToDataBase(Book entity, String cmd){
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            PreparedStatement preparedStatement = connection.prepareStatement(cmd);
            preparedStatement.setString(1, entity.getSerialNumber());
            preparedStatement.setString(2, entity.getTitle());
            preparedStatement.setString(3, entity.getAuthor());
            preparedStatement.setInt(4, entity.getYear());
            preparedStatement.setDouble(5, entity.getPrice());
            preparedStatement.setInt(6, entity.getInStock());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void removeEntitiesWithClientID(Long id) throws ParserConfigurationException, IOException, SAXException {

    }
}
