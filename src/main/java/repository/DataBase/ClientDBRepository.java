package repository.DataBase;

import domain.Client;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import org.xml.sax.SAXException;
import repository.SortingRepository;
import repository.DataBase.implementation.Sort;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class ClientDBRepository implements SortingRepository<Long, Client> {

//    private static final String URL = "jdbc:postgresql://localhost:5432/Clientstore";
//    private static final String USER = System.getProperty("username");
//    private static final String PASSWORD = System.getProperty("password");
    private static final String URL = "jdbc:postgresql://localhost:5432/bookstore?currentSchema=bookstore&user=postgres&password=password";
    private static final String USER = System.getProperty("postgres");
    private static final String PASSWORD = System.getProperty("password");
    private Validator<Client> validator;

    public ClientDBRepository(Validator<Client> validator) {
        this.validator=validator;
    }

    @Override
    public Iterable<Client> findAll(Sort sort) {
        return null;
    }

    @Override
    public Optional<Client> findOne(Long id) throws SQLException {
        String cmd = "select * from Clients where id = ?";
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(cmd);
        preparedStatement.setLong(1,id);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String name = resultSet.getString("name");
            String serialNumber = resultSet.getString("serialNumber");
            Client client = new Client(serialNumber, name);
            client.setId(id);
            return Optional.of(client);
        }

        return Optional.empty();
    }

    @Override
    public Iterable<Client> findAll() throws SQLException {
        List<Client> result = new ArrayList<>();
        String cmd = "select * from Clients";

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(cmd);
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            Long id = resultSet.getLong("id");
            String serialNumber = resultSet.getString("serialNumber");
            String name = resultSet.getString("name");

            Client client = new Client(serialNumber, name);
            client.setId(id);
            result.add(client);
        }
        return result;
    }

    @Override
    public Optional<Client> save(Client entity) throws ValidatorException, ParserConfigurationException, IOException, SAXException, TransformerException, SQLException {
        String cmd = "insert into Clients(id,serialNumber,name)" +
                "values(?, ?, ?)";
        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(cmd);
        preparedStatement.setLong(1, entity.getId());
        preparedStatement.setString(2, entity.getSerialNumber());
        preparedStatement.setString(3, entity.getName());
        if(preparedStatement.executeUpdate()==0)
            return Optional.empty();
        else
            return Optional.of(new Client());
    }


    @Override
    public Optional<Client> delete(Long id) throws SQLException {
        String sql = "delete from Clients where id=?";

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setLong(1, id);
        if(preparedStatement.executeUpdate()==0)
            return Optional.empty();
        else
            return Optional.of(new Client());
    }

    @Override
    public Optional<Client> update(Client entity) throws ValidatorException, SQLException {

        String sql = "update Clients set name=?, serialnumber=? where id=?";

        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        preparedStatement.setString(1, entity.getName());
        preparedStatement.setString(2, entity.getSerialNumber());
        preparedStatement.setLong(3, entity.getId());
        if(preparedStatement.executeUpdate()==0)
            return Optional.empty();
        else
            return Optional.of(new Client());
    }

    @Override
    public void removeEntitiesWithClientID(Long id) throws ParserConfigurationException, IOException, SAXException {

    }
}
