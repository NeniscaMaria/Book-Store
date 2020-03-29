package Service;

import TCP.TCPClient;
import domain.Message;
import domain.Purchase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class PurchaseService {
    private ExecutorService executorService;
    private TCPClient tcpClient;

    public PurchaseService(ExecutorService executorService, TCPClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    public Future<Message> getAllPurchases(){
        return executorService.submit(()->{
            //create request
            Message request  = new Message(PurchaseServiceInterface.GET_ALL_PURCHASES,"");
            //send request to server and receive answer
            Message response = tcpClient.sendAndReceive(request);
            return response;
        });
    }

    public Future<Message> removePurchase(Long id){
        return executorService.submit(()->{
            String ID = String.valueOf(id);
            Message request = new Message(PurchaseServiceInterface.REMOVE_PURCHASE,ID);
            return tcpClient.sendAndReceive(request);
        });
    }

    public Future<Message> addPurchases(Purchase purchase){
        return null;
    }
}
