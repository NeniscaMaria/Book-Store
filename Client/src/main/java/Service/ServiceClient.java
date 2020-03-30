package Service;

import TCP.TCPClient;
import domain.Client;
import domain.Message;
import domain.ValidatorException;

import java.sql.SQLException;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ServiceClient implements ClientServiceInterface{

    private ExecutorService executorService;
    private TCPClient tcpClient;

    public ServiceClient(ExecutorService executorService, TCPClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    @Override
    public Future<Set<Client>> getAllClients() throws SQLException {
        return null;
    }

    @Override
    public Future<Optional<Client>> removeClient(Long id) throws SQLException {
        return null;
    }

    @Override
    public Future<Optional<Client>> addClient(Client entity) throws SQLException, ValidatorException {
        return executorService.submit(()->{


            Message request =  new Message(ClientServiceInterface.ADD_CLIENT, entity.toString());
            System.out.println("sending request: "+request);
            Message response = tcpClient.sendAndReceive(request);
            System.out.println("received response: "+response);


            return Optional.of(entity);
        });
    }
}
