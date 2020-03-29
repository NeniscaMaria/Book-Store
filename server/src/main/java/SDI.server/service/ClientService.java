package SDI.server.service;

import SDI.server.repository.DataBase.ClientDBRepository;
import SDI.server.repository.DataBase.implementation.Sort;
import SDI.server.repository.Repository;
import SDI.server.validators.ValidatorException;
import domain.Client;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientService {
    private Repository<Long, Client> repository;

    public ClientService(Repository<Long, domain.Client> repository) {
        this.repository = repository;
    }

    public Optional<Client> addClient(domain.Client client) throws ValidatorException, ParserConfigurationException, TransformerException, SAXException, IOException, SQLException {
        return repository.save(client);
    }

    public Iterable<Client> getAllClients(String ...a) throws SQLException {
        Iterable<Client> clients;
        if (repository instanceof ClientDBRepository){
            clients = ((ClientDBRepository)repository).findAll(new Sort(a).and(new Sort(a)));
            return StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toList());
        }
        else throw new ValidatorException("Too many parameters");

    }

    public Optional<Client> removeClient(Long ID) throws SQLException {
        return repository.delete(ID);
    }

    public Optional<Client> updateClient(domain.Client client) throws ValidatorException, SQLException {
        return repository.update(client);
    }

    public Set<domain.Client> getAllClients() throws SQLException {
        Iterable<domain.Client> clients = repository.findAll();
        return StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toSet());
    }

    /*POST:Returns all students whose name contain the given string.
     PRE: @param s
     */
    public Set<domain.Client> filterClientsByName(String s) throws SQLException {
        Iterable<domain.Client> clients = repository.findAll();
        Set<domain.Client> filteredClients= new HashSet<>();
        clients.forEach(filteredClients::add);
        filteredClients.removeIf(student -> !student.getName().contains(s));

        return filteredClients;
    }
    public Optional<Client> findOneClient(Long clientID) throws SQLException {
        return repository.findOne(clientID);
    }
}
