package Service;

import TCP.TCPClient;
import domain.Message;
import domain.Purchase;
import domain.ValidatorException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PurchaseService {
    private ExecutorService executorService;
    private TCPClient tcpClient;

    public PurchaseService(ExecutorService executorService, TCPClient tcpClient) {
        this.executorService = executorService;
        this.tcpClient = tcpClient;
    }

    public CompletableFuture<Message> getAllPurchases(){
        return CompletableFuture.supplyAsync(()->{
            //create request
            Message request  = new Message(PurchaseServiceInterface.GET_ALL_PURCHASES,"");
            //send request to server and receive answer
            return tcpClient.sendAndReceive(request);
        },executorService);
    }

    public CompletableFuture<Message> removePurchase(Long id){
        return CompletableFuture.supplyAsync(()->{
            String ID = String.valueOf(id);
            Message request = new Message(PurchaseServiceInterface.REMOVE_PURCHASE,ID);
            return tcpClient.sendAndReceive(request);
        },executorService);
    }

    public CompletableFuture<Message> addPurchases(Purchase purchase){
        return null;
    }
    public CompletableFuture<Message> updatePurchase(Purchase purchase) {
        /*return executorService.submit(()->{
            Message request = new Message(PurchaseServiceInterface.UPDATE_PURCHASE,purchase);
            Message response = tcpClient.sendAndReceive(request);
            return response;
        });*/
        return null;
    }
    public CompletableFuture<Message> filterPurchasesByClientID(Long id) throws SQLException {
        return CompletableFuture.supplyAsync(()->{
            String ID = String.valueOf(id);
            Message request = new Message(PurchaseServiceInterface.FILTER,ID);
            return tcpClient.sendAndReceive(request);
        });
    }

    public CompletableFuture<Message> findOnePurchase(Long id) throws SQLException {
        return CompletableFuture.supplyAsync(()->{
            String ID = String.valueOf(id);
            Message request = new Message(PurchaseServiceInterface.FIND_ONE,ID);
            return tcpClient.sendAndReceive(request);
        },executorService);
    }
}


