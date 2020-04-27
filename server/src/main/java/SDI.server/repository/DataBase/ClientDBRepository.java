package SDI.server.repository.DataBase;

import SDI.server.repository.SortingRepository;
import SDI.server.validators.Validator;
import domain.Sort;
import domain.ValidatorException;
import domain.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.*;
import java.util.*;

@Component
public class ClientDBRepository implements SortingRepository<Long, Client> {
    @Autowired
    private JdbcOperations jdbcOperations;
    @Autowired
    private Validator<Client> validator;

    public ClientDBRepository() {

    }


    @Override
    public Iterable<Client> findAll(Sort sort) throws SQLException, RemoteException {
        return sort.sortClient();
    }

    @Override
    public Optional<Client> findOne(Long id) throws SQLException {
        String cmd = "select * from Clients where id = ?";
        Client result = jdbcOperations.query(cmd, new Object[]{id}, (resultSet, rowNum) -> {
            String name = resultSet.getString("name");
            String serialNumber = resultSet.getString("serialNumber");
            Client client = new Client(serialNumber, name);
            client.setId(id);
            return client;
        }).get(0);
        return Optional.of(result);
    }

    @Override
    public List<Client> findAll() throws SQLException {
        String cmd = "select * from Clients";
        return jdbcOperations.query(cmd, (resultSet, rowNum) -> {
            Long id = resultSet.getLong("id");
            String serialNumber = resultSet.getString("serialNumber");
            String name = resultSet.getString("name");
            Client client = new Client(serialNumber, name);
            client.setId(id);
            return client;
        });
    }

    @Override
    public int save(Client entity) throws ValidatorException, SQLException {
        String cmd = "insert into Clients(id,serialNumber,name) values(?, ?, ?)";
        validator.validate(entity);
        return jdbcOperations.update(cmd, entity.getId(), entity.getSerialNumber(),entity.getName());
    }


    @Override
    public int delete(Long id) throws SQLException {
        String cmd = "delete from Clients where id=?";
        return jdbcOperations.update(cmd, id);
    }

    @Override
    public int update(Client entity) throws ValidatorException, SQLException {
        validator.validate(entity);
        String cmd = "update Clients set name=?, serialnumber=? where id=?";
        return jdbcOperations.update(cmd,entity.getName(),entity.getSerialNumber(),entity.getId());
    }

    @Override
    public void removeEntitiesWithClientID(Long id) throws ParserConfigurationException, IOException, SAXException {

    }
}
