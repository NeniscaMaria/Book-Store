package SDI.server;

import SDI.server.service.BookService;
import SDI.server.service.ClientService;
import SDI.server.service.PurchaseService;
import Service.ClientServiceInterface;
import Service.PurchaseServiceInterface;
import domain.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class TCPServer {
    private ExecutorService executorService;
    private Map<String, UnaryOperator<Message>> methodHandlers; //for methods that return string
    private Map<String, Function<Client,Message>> clientHandlers; //for methods that need a Client as parameter
    private Map<String, Function<Purchase,Message>> purchaseHandlers; //for methods that need a Purchase as parameter

    private ClientService clientService;
    private BookService bookService ;
    private PurchaseService purchaseService;

    public TCPServer(ExecutorService executorService, ClientService clientService, BookService bookService, PurchaseService purchaseService) {
        this.executorService = executorService;
        methodHandlers = new HashMap<>();
        clientHandlers = new HashMap<>();
        purchaseHandlers = new HashMap<>();
        this.clientService = clientService;
        this.purchaseService = purchaseService;
        this.bookService = bookService;
        initializeHandlers();
    }

    private void initializeHandlersClient(){
        //getAllClients function
        addHandler(ClientServiceInterface.GET_ALL_CLIENTS,
                (request) -> {
                    try {
                        CompletableFuture<Set<Client>> clients = clientService.getAllClients();
                        return new Message("success", clients.get().stream().map(Client::toString).reduce("",(a, b)->a+b));
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("error", e.getMessage());
                    }

                });
        addHandler(ClientServiceInterface.REMOVE_CLIENT,
                (request) -> {
                    try {
                        Long id = Long.parseLong(request.getBody());
                        CompletableFuture<Optional<Client>> client = clientService.removeClient(id);
                        if(client.get().isEmpty())
                            return new Message("No client matched this ID.","");
                        return new Message("Client removed successfully", "");
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while deleting client.", e.getMessage());
                    }

                });
        //uncomment when fixed message
        addClientHandler(ClientServiceInterface.ADD_CLIENT,
                (entity)->{
                    try {
                        CompletableFuture<Optional<Client>> client = clientService.addClient(entity);
                        if(client.get().isEmpty())
                            return new Message("Client saved successfully.","");
                        return new Message("A client with this ID already exists.", "");
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while deleting client.", e.getMessage());
                    }
                });
        addClientHandler(ClientServiceInterface.UPDATE_CLIENT,
                (entity)->{
                    try {
                        CompletableFuture<Optional<Client>> client = clientService.updateClient(entity);
                        if(client.get().isEmpty())
                            return new Message("Client updated successfully.","");
                        return new Message("Fail at update client.", "");
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while updating client.", e.getMessage());
                    }
                });
        addHandler(ClientServiceInterface.FIND_ONE,
                (ID)->{
                    try {
                        CompletableFuture<Optional<Client>> client = clientService.findOneClient(Long.parseLong(ID.getBody()));
                        if(client.get().isEmpty())
                            return new Message("No client with this ID.","");
                        return new Message("succes.", client.get().get().toString());
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while finding client.", e.getMessage());
                    }
                });
        addHandler(ClientServiceInterface.FILTER_NAME,
                (request)->{
                    try {
                        CompletableFuture<Set<Client>> clients = clientService.filterClientsByName(request.getBody());
                        return new Message("success", clients.get().stream().map(Client::toString).reduce("",(a, b)->a+b));
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while filtering clients.", e.getMessage());
                    }
                });
    }

    //TODO
    private void initializeHandlersBooks(){}
    //TODO
    private void initializeHandlersPurchases(){
        addHandler(PurchaseServiceInterface.GET_ALL_PURCHASES,
                (request) -> {
                    try {
                        CompletableFuture<Set<Purchase>> purchases = purchaseService.getAllPurchases();
                        return new Message("success", purchases.get().stream().map(Purchase::toString).reduce("",(a, b)->a+b));
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("error", e.getMessage());
                    }

                });
        addHandler(PurchaseServiceInterface.REMOVE_PURCHASE,
                (request) -> {
                    try {
                        Long id = Long.parseLong(request.getBody());
                        CompletableFuture<Optional<Purchase>> client = purchaseService.removePurchase(id);
                        if(client.get().isEmpty())
                            return new Message("No purchase matched this ID.","");
                        return new Message("Purchase removed successfully", "");
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while deleting purchase.", e.getMessage());
                    }

                });
        //uncomment when fixed message
        addPurchaseHandler(PurchaseServiceInterface.ADD_PURCHASE,
                (entity)->{
                    try {
                        CompletableFuture<Optional<Purchase>> purchase = purchaseService.addPurchase(entity);
                        if(purchase.get().isEmpty())
                            return new Message("Purchase saved successfully.","");
                        return new Message("A purchase with this ID already exists.", "");
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while adding purchase client.", e.getMessage());
                    }
                });
        addPurchaseHandler(PurchaseServiceInterface.UPDATE_PURCHASE,
                (entity)->{
                    try {
                        CompletableFuture<Optional<Purchase>> purchase = purchaseService.updatePurchase(entity);
                        if(purchase.get().isEmpty())
                            return new Message("Purchase updated successfully.","");
                        return new Message("Fail at update purchase.", "");
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while updating client.", e.getMessage());
                    }
                });
        addHandler(PurchaseServiceInterface.FIND_ONE,
                (request)->{
                    try {
                        CompletableFuture<Optional<Purchase>> purchase = purchaseService.findOnePurchase(Long.parseLong(request.getBody()));
                        if(purchase.get().isEmpty())
                            return new Message("No client with this ID.","");
                        return new Message("success", purchase.get().get().toString());
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while finding client.", e.getMessage());
                    }
                });
        addHandler(PurchaseServiceInterface.FILTER,
                (request)->{
                    try {
                        CompletableFuture<Set<Purchase>> purchases = purchaseService.filterPurchasesByClientID(Long.parseLong(request.getBody()));
                        return new Message("success", purchases.get().stream().map(Purchase::toString).reduce("",(a, b)->a+b));
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while filtering purchases.", e.getMessage());
                    }
                });
    }

    private void initializeHandlers(){
        initializeHandlersClient();
        initializeHandlersBooks();
        initializeHandlersPurchases();
    }

    public void addHandler(String methodName, UnaryOperator<Message> handler){
        methodHandlers.put(methodName,handler);
    }

    public void addClientHandler(String methodName, Function<Client,Message> handler){
        clientHandlers.put(methodName,handler);
    }

    public void addPurchaseHandler(String methodName, Function<Purchase,Message> handler){
        purchaseHandlers.put(methodName,handler);
    }

    public void startServer(){
        try(var serverSocket = new ServerSocket(Message.PORT)){
            while(true){
                Socket client = serverSocket.accept();
                executorService.submit(new ClientHandler(client));
            }
        }catch(IOException e){
            throw new ServerException("Error connecting clients",e);
        }
    }

    private class ClientHandler implements Runnable{
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run(){
            try(var is = socket.getInputStream();  var os = socket.getOutputStream()){
                Message request = new Message();
                request.readFrom(is);
                System.out.println("Received request: "+request);
                //request header = method name
                //request body = method arguments
                Message response = (Message) methodHandlers.get(request.getHeader()).apply(request);
                response.writeTo(os);
            }catch(IOException e){
                throw new ServerException("Error processing client",e);
            }
        }
    }

}

