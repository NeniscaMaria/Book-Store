package service;

import domain.validators.ValidatorException;
import repository.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class ClientService {
    private Repository<Long, domain.Client> repository;

    public ClientService(Repository<Long, domain.Client> repository) {
        this.repository = repository;
    }

    public void addClient(domain.Client client) throws ValidatorException {
        repository.save(client);
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
        //version 1
//        Set<domain.Client> filteredStudents = StreamSupport.stream(clients.spliterator(), false)
//                .filter(client -> client.getName().contains(s)).collect(Collectors.toSet());

        //version 2
        Set<domain.Client> filteredClients= new HashSet<>();
        clients.forEach(filteredClients::add);
        filteredClients.removeIf(student -> !student.getName().contains(s));

        return filteredClients;
    }
}
