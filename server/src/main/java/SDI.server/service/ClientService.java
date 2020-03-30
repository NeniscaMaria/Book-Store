package SDI.server.service;

import SDI.server.repository.DataBase.ClientDBRepository;
import SDI.server.repository.DataBase.implementation.Sort;
import SDI.server.repository.Repository;
import domain.Message;
import domain.ValidatorException;
import Service.ClientServiceInterface;
import domain.Client;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientService implements ClientServiceInterface {
    private Repository<Long, Client> repository;
    private ExecutorService executorService;

    public ClientService(Repository<Long, domain.Client> repository, ExecutorService executorService) {
        this.repository = repository;
        this.executorService = executorService;
    }

    @Override
    public CompletableFuture<Optional<Client>> addClient(Client client) throws ValidatorException {
        /*return CompletableFuture.supplyAsync(()-> {
                repository.save(client);
        },executorService).handle((res,ex)->{return Optional.empty();});*/
        return null;
    }

    public Iterable<Client> getAllClients(String ...a) throws SQLException {
        Iterable<Client> clients;
        if (repository instanceof ClientDBRepository){
            clients = ((ClientDBRepository)repository).findAll(new Sort(a).and(new Sort(a)));
            return StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toList());
        }
        else throw new ValidatorException("Too many parameters");

    }

    @Override
    public CompletableFuture<Optional<Client>> removeClient(Long ID) throws SQLException {
        return CompletableFuture.supplyAsync(()-> {
            try {
                return repository.delete(ID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Optional.empty();
        },executorService);
    }

    @Override
    public CompletableFuture<Optional<Client>> updateClient(domain.Client client) throws ValidatorException, SQLException {
        return CompletableFuture.supplyAsync(()-> {
            try {
                return repository.update(client);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Optional.empty();
        },executorService);
    }

    @Override
    public CompletableFuture<Set<Client>> getAllClients() throws SQLException {
        return CompletableFuture.supplyAsync(()->{
            Iterable<Client> clients = null;
            try {
                clients = repository.findAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toSet());
        },executorService);
    }

    /*POST:Returns all students whose name contain the given string.
     PRE: @param s
     */
    @Override
    public CompletableFuture<Set<Client>> filterClientsByName(String s) throws SQLException {
        return CompletableFuture.supplyAsync(()->{
            Iterable<Client> clients = null;
            try {
                clients = repository.findAll();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            Set<domain.Client> filteredClients= new HashSet<>();
            clients.forEach(filteredClients::add);
            filteredClients.removeIf(student -> !student.getName().contains(s));
            return filteredClients;
        },executorService);
    }
    @Override
    public CompletableFuture<Optional<Client>> findOneClient(Long clientID) throws SQLException {
        return CompletableFuture.supplyAsync(()-> {
            try {
                return repository.findOne(clientID);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return Optional.empty();
        },executorService);
    }

}
