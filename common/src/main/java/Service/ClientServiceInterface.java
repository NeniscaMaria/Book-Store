package Service;

import domain.Client;
import domain.ValidatorException;

import javax.xml.parsers.ParserConfigurationException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ClientServiceInterface extends Remote {
    int delete(Long id) throws SQLException, RemoteException;
    int save(Client entity) throws SQLException, RemoteException,ValidatorException, ParserConfigurationException;
    List<Client> findAll() throws SQLException,RemoteException;
    int update(Client entity) throws SQLException,RemoteException, ValidatorException;
    Optional<Client> findOne(Long clientID) throws SQLException,RemoteException;
    Set<Client> filterClientsByName(String s) throws SQLException,RemoteException;
//    Iterable<Client> findAll(Sort sort) throws SQLException,RemoteException;
    Iterable<Client> findAll(String... sort) throws SQLException,RemoteException;

}
