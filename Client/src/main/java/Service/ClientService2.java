package Service;

import TCP.TCPClient;
import TCP.TCPClient2;
import domain.Client;
import domain.Message;
import domain.Message2;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ClientService2 {
    private ExecutorService executorService;
    private TCPClient2 tcpClient;

    public ClientService2(ExecutorService executorService, TCPClient2 tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }
    public Future<Message2<Client>> addClient(Client client){

        return executorService.submit(()->{
            Message2<Client> request = new Message2<Client>(ClientServiceInterface.ADD_CLIENT, client);
            return tcpClient.sendAndReceive(request);
        });
    }

}
