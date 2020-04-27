package SDI.server.service;
import SDI.server.repository.DataBase.ClientDBRepository;
import domain.Sort;
import domain.ValidatorException;
import Service.ClientServiceInterface;
import domain.Client;
import org.springframework.beans.factory.annotation.Autowired;

import java.rmi.RemoteException;
import java.sql.*;
import java.util.*;

public class ClientService implements ClientServiceInterface{
    @Autowired
    private ClientDBRepository repo;
    @Autowired
    private Sort sort;

    public ClientService() throws RemoteException {
    }

    @Override
    public Iterable<Client> findAll(String... args) throws SQLException , RemoteException {
        sort.setInfo(args);
        return sort.sortClient();
    }

    @Override
    public Optional<Client> findOne(Long id) throws SQLException,RemoteException {
        return repo.findOne(id);
    }

    @Override
    public List<Client> findAll() throws SQLException,RemoteException {
        return repo.findAll();
    }

    @Override
    public int save(Client entity) throws ValidatorException,RemoteException, SQLException {
        return repo.save(entity);
    }


    @Override
    public int delete(Long id) throws SQLException,RemoteException {
        return repo.delete(id);
    }

    @Override
    public int update(Client entity) throws ValidatorException, RemoteException,SQLException {
        return repo.update(entity);
    }

    /*POST:Returns all students whose name contain the given string.
     PRE: @param s
     */
    @Override
    public synchronized Set<Client> filterClientsByName(String s) throws SQLException,RemoteException {
        Iterable<Client> clients = null;
        try {
            clients = findAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        HashSet<domain.Client> filteredClients= new HashSet<>();
        clients.forEach(filteredClients::add);
        filteredClients.removeIf(student -> !student.getName().contains(s));
        return filteredClients;
    }
}
