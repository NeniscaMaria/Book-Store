package Service;

import TCP.TCPClient;
import domain.Client;
import domain.Message;
import domain.ValidatorException;

import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class ClientService{
    private ExecutorService executorService;
    private TCPClient tcpClient;

    public ClientService(ExecutorService executorService, TCPClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    public Future<Message> getAllClients(){
        return executorService.submit(()->{
            //create request
            Message request  = new Message(ClientServiceInterface.GET_ALL_CLIENTS,"");
            //send request to server and receive answer
            Message response = tcpClient.sendAndReceive(request);
            return response;
        });
    }

    public Future<Message> removeClient(Long id){
        return executorService.submit(()->{
            String ID = String.valueOf(id);
            Message request = new Message(ClientServiceInterface.REMOVE_CLIENT,ID);
            return tcpClient.sendAndReceive(request);
        });
    }

    public Future<Message> addClient(Client client){
        return null;
    }

    Future<Message> updateClient(Client entity) throws SQLException, ValidatorException{
        /*return executorService.submit(()->{
            Message request = new Message(ClientServiceInterface.UPDATE_CLIENT,entity);
            return tcpClient.sendAndReceive(request);
        });*/
        return null;
    }
    public Future<Message> findOneClient(Long id) throws SQLException{
        return executorService.submit(()->{
            String ID = String.valueOf(id);
            Message request = new Message(ClientServiceInterface.FIND_ONE,ID);
            return tcpClient.sendAndReceive(request);
        });
    }
    public Future<Message> filterClientsByName(String s) throws SQLException{
        return executorService.submit(()->{
            Message request = new Message(ClientServiceInterface.FILTER_NAME,s);
            return tcpClient.sendAndReceive(request);
        });
    }
}
