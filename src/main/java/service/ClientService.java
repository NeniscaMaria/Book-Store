package service;

import domain.Client;
import domain.Purchase;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import net.bytebuddy.asm.Advice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import repository.Repository;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ClientService {
    private static final Logger log = LoggerFactory.getLogger(ClientService.class);
    @Autowired
    private Repository<Long, domain.Client> repository;
    @Autowired
    private Validator<Client> validator;

    public Optional<Client> addClient(domain.Client client){
        log.trace("addClient - method entered client={}",client);
        validator.validate(client);
        try {
            repository.save(client);
        }catch(DataIntegrityViolationException de){
            log.trace("addClient - method finished");
            return Optional.empty();
        }
        log.trace("addClient - client saved client = {}",client);
        log.trace("addClient - method finished");
        return Optional.of(new Client());
    }

    public Optional<Client> removeClient(Long ID) {
        log.trace("removeClient - method entered id={}",ID);
        try {
            repository.deleteById(ID);
        }catch(EmptyResultDataAccessException er){
            log.trace("removeClient - method finished. No Purchase with this ID");
            return Optional.empty();
        }
        log.trace("removeClient - client deleted id={}",ID);
        log.trace("removeClient - method finished");
        return Optional.of(new Client());
    }

    public Set<Client> sort(Sort.Direction dir, String ...a ){
        log.trace("Client: sort method entered dir={} filters={}",dir,a);
        Iterable<Client> clients = repository.findAll(Sort.by(dir,a));
        Set<Client> result = StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toSet());
        log.trace("Client: sort method finished. Returned: c={}",result);
        return result;
    }

    @Transactional
    public void updateClient(domain.Client client) {
        log.trace("updateClient - method entered: client={}", client);
        validator.validate(client);
        repository.findById(client.getId())
                .ifPresent(s -> {
                    s.setName(client.getName());
                    s.setSerialNumber(client.getSerialNumber());
                    log.debug("updateClient - updated: s={}", s);
                });
        log.trace("updateClient - method finished");
    }

    public Set<domain.Client> getAllClients() {
        log.trace("getAlClients - method entered");
        Iterable<domain.Client> clients = repository.findAll();
        Set<Client> result = StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toSet());
        log.trace("getAllClients - method finished and returned clients={}",clients);
        return result;
    }

    public Set<domain.Client> filterClientsByName(String s)  {
        log.trace("filterClientsByName - method entered name={}",s);
        Iterable<domain.Client> clients = repository.findAll();
        Set<domain.Client> filteredClients= new HashSet<>();
        clients.forEach(filteredClients::add);
        filteredClients.removeIf(student -> !student.getName().contains(s));
        log.trace("filterClientsByname - method finished and returned c={}",filteredClients);
        return filteredClients;
    }
    public Optional<Client> findOneClient(Long clientID) {
        return repository.findById(clientID);
    }
}
