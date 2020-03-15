package service;

import domain.Book;
import domain.Client;
import domain.validators.ValidatorException;
import org.xml.sax.SAXException;
import repository.Repository;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientService {
    private Repository<Long, domain.Client> repository;

    public ClientService(Repository<Long, domain.Client> repository) {
        this.repository = repository;
    }

    public Optional<Client> addClient(domain.Client client) throws ValidatorException, ParserConfigurationException, TransformerException, SAXException, IOException {
        return repository.save(client);
    }

    public Optional<Client> removeClient(Long ID){
        return repository.delete(ID);
    }

    public Optional<Client> updateClient(domain.Client client) throws ValidatorException {
        return repository.update(client);
    }

    public Set<domain.Client> getAllClients() {
        Iterable<domain.Client> clients = repository.findAll();
        return StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toSet());
    }

    /*POST:Returns all students whose name contain the given string.
     PRE: @param s
     */
    public Set<domain.Client> filterClientsByName(String s) {
        Iterable<domain.Client> clients = repository.findAll();
        Set<domain.Client> filteredClients= new HashSet<>();
        clients.forEach(filteredClients::add);
        filteredClients.removeIf(student -> !student.getName().contains(s));

        return filteredClients;
    }
    public Optional<Client> findOneClient(Long clientID){
        return repository.findOne(clientID);
    }
}
