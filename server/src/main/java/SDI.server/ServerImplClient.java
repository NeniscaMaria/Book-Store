package SDI.server;

import SDI.server.service.ClientService;
import Service.ClientServiceInterface;
import domain.Client;
import domain.ValidatorException;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ServerImplClient implements ClientServiceInterface{

    private ExecutorService executorService;
    private ClientService bs;

    public ServerImplClient(ExecutorService executorService, ClientService bs) {
        this.executorService = executorService;
        this.bs = bs;
    }


    @Override
    public Future<Set<Client>> getAllClients() throws SQLException {
        return executorService.submit(()-> bs.getAllClients());
    }

    @Override
    public Future<Optional<Client>> removeClient(Long id) throws SQLException {
        return null;
    }

    @Override
    public Future<Optional<Client>> addClient(Client entity) throws SQLException, ValidatorException {
        return executorService.submit(() -> Optional.of(entity));

    }
}