package Service;
import domain.Client;
import domain.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;

import javax.xml.parsers.ParserConfigurationException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class ClientServiceClient implements ClientServiceInterface{
    @Autowired
    private ClientServiceInterface clientService;

    @Override
    public synchronized int delete(Long id) throws SQLException, RemoteException {
        return clientService.delete(id);
    }

    @Override
    public synchronized int save(Client entity) throws SQLException,RemoteException, ValidatorException, ParserConfigurationException {
        return clientService.save(entity);
    }

    @Override
    public synchronized List<Client> findAll() throws SQLException,RemoteException {
        return clientService.findAll();
    }

    @Override
    public synchronized int update(Client entity) throws SQLException,RemoteException, ValidatorException {
        return clientService.update(entity);
    }

    @Override
    public synchronized Optional<Client> findOne(Long clientID) throws SQLException,RemoteException {
        return clientService.findOne(clientID);
    }

    @Override
    public synchronized Set<Client> filterClientsByName(String s) throws SQLException,RemoteException {
        return clientService.filterClientsByName(s);
    }

    @Override
    public Iterable<Client> findAll(String... sort) throws SQLException,RemoteException {
        return clientService.findAll(sort);
    }

}
