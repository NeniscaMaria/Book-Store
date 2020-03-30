package Service;

import domain.Client;
import domain.ValidatorException;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Future;

public interface ClientServiceInterface {
    public String GET_ALL_CLIENTS = "getAllClients";
    public String REMOVE_CLIENT = "removeClient";
    public String ADD_CLIENT = "addClient";
    public String UPDATE_CLIENT = "updateClient";
    public String FIND_ONE = "findOneClient";
    public String FILTER_NAME = "filterClientsByName";

    Future<Set<Client>> getAllClients() throws SQLException;
    Future<Optional<Client>> removeClient(Long id) throws SQLException;
    Future<Optional<Client>> addClient(Client entity) throws SQLException, ValidatorException;

    Future<Optional<Client>> updateClient(Client entity) throws SQLException, ValidatorException;
    Future<Optional<Client>> findOneClient(Long clientID) throws SQLException;
    Future<Set<domain.Client>> filterClientsByName(String s) throws SQLException;
}
