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
import java.util.concurrent.Future;
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
    public Future<Optional<Client>> addClient(Client client) throws SQLException, ValidatorException {
        return executorService.submit(()->repository.save(client));
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
    public Future<Optional<Client>> removeClient(Long ID) throws SQLException {
        return executorService.submit(()->repository.delete(ID));
    }

    @Override
    public Future<Optional<Client>> updateClient(domain.Client client) throws ValidatorException, SQLException {
        return executorService.submit(()->repository.update(client));
    }

    @Override
    public Future<Set<Client>> getAllClients() throws SQLException {
        Iterable<domain.Client> clients = repository.findAll();
        Set<Client> result = StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toSet());
        return executorService.submit(()->result);
    }

    /*POST:Returns all students whose name contain the given string.
     PRE: @param s
     */
    @Override
    public Future<Set<domain.Client>> filterClientsByName(String s) throws SQLException {
        return executorService.submit(()->{
            Iterable<domain.Client> clients = repository.findAll();
            Set<domain.Client> filteredClients= new HashSet<>();
            clients.forEach(filteredClients::add);
            filteredClients.removeIf(student -> !student.getName().contains(s));
            return filteredClients;
        });
    }
    @Override
    public Future<Optional<Client>> findOneClient(Long clientID) throws SQLException {
        return executorService.submit(()->repository.findOne(clientID));
    }

}
