package SDI.server;

import SDI.server.service.BookService;
import SDI.server.service.ClientService;
import SDI.server.service.PurchaseService;
import Service.ClientServiceInterface;
import Service.PurchaseServiceInterface;
import domain.*;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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
    private Map<String, UnaryOperator<Message<String>>> methodHandlers; //for methods that return string

    private Map<String, Function<Message<Client>,Message<Optional<Client>>>> clientHandlers; //for methods that need a Client as parameter
    private Map<String, Function<Message<String>,Message<HashSet<Client>>>> clientHandlersSet; //for methods that need a Client as parameter

    private Map<String, Function<Message<Purchase>,Message<Optional<Purchase>>>> purchaseHandlers; //for methods that need a Purchase as parameter

    private ClientService clientService;
    private BookService bookService ;
    private PurchaseService purchaseService;

    public TCPServer(ExecutorService executorService, ClientService clientService, BookService bookService, PurchaseService purchaseService) {
        this.executorService = executorService;
        methodHandlers = new HashMap<>();
        clientHandlers = new HashMap<>();
        purchaseHandlers = new HashMap<>();
        clientHandlersSet = new HashMap<>();
        this.clientService = clientService;
        this.purchaseService = purchaseService;
        this.bookService = bookService;
        initializeHandlers();
    }

    private void initializeHandlersClient(){
        //getAllClients function
        //return Set<Client>
        addClientHandlerSet(ClientServiceInterface.GET_ALL_CLIENTS,
                (request) -> {
                    try {
                        CompletableFuture<HashSet<Client>> clients = clientService.getAllClients();
                        return new Message("success", clients);
                    } catch (SQLException e) {
                        return new Message("error", e.getMessage());
                    }

                });
        addClientHandlerSet(ClientServiceInterface.FILTER_NAME,
                (request)->{
                    try {
                        CompletableFuture<HashSet<Client>> clients = clientService.filterClientsByName((String) request.getBody());
                        return new Message("success", clients);
                    } catch (SQLException e) {
                        return new Message("Server-side error while filtering clients.", e.getMessage());
                    }
                });


        //input: Long output:Optional<Client>
        addHandler(ClientServiceInterface.REMOVE_CLIENT,
                (request) -> {
                    try {
                        Long id = Long.parseLong((String) request.getBody());
                        CompletableFuture<Optional<Client>> client = clientService.removeClient(id);
                        if(client.get().isEmpty())
                            return new Message("No client matched this ID.","");
                        return new Message("Client removed successfully", "");
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while deleting client.", e.getMessage());
                    }

                });
        addHandler(ClientServiceInterface.FIND_ONE,
                (ID)->{
                    try {
                        CompletableFuture<Optional<Client>> client = clientService.findOneClient(Long.parseLong((String) ID.getBody()));
                        if(client.get().isEmpty())
                            return new Message("No client with this ID.","");
                        return new Message("succes.", client.get().get().toString());
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while finding client.", e.getMessage());
                    }
                });

        //input:Client output:Optional<Client> DONE
        addClientHandler(ClientServiceInterface.ADD_CLIENT,
                (entity)->{
                    try {
                        System.out.println("Adding client..");
                        CompletableFuture<Optional<Client>> client = clientService.addClient(entity.getBody());
                        return new Message<Optional<Client>>("success",client.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while deleting client.", e.getMessage());
                    }
                });
        addClientHandler(ClientServiceInterface.UPDATE_CLIENT,
                (entity)->{
                    try {
                        CompletableFuture<Optional<Client>> client = clientService.updateClient(entity.getBody());
                        return new Message<Optional<Client>>("success",client.get());
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while updating client.", e.getMessage());
                    }
                });
    }

    //TODO
    private void initializeHandlersBooks(){}
    //TODO
    private void initializeHandlersPurchases(){
        //output: Set<Purchase
        addHandler(PurchaseServiceInterface.GET_ALL_PURCHASES,
                (request) -> {
                    try {
                        CompletableFuture<Set<Purchase>> purchases = purchaseService.getAllPurchases();
                        return new Message("success", purchases.get().stream().map(Purchase::toString).reduce("",(a, b)->a+b));
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("error", e.getMessage());
                    }

                });
        addHandler(PurchaseServiceInterface.FILTER,
                (request)->{
                    try {
                        CompletableFuture<Set<Purchase>> purchases = purchaseService.filterPurchasesByClientID(Long.parseLong((String) request.getBody()));
                        return new Message("success", purchases.get().stream().map(Purchase::toString).reduce("",(a, b)->a+b));
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while filtering purchases.", e.getMessage());
                    }
                });

        //input: Long output: Optional<Purchase>
        addHandler(PurchaseServiceInterface.REMOVE_PURCHASE,
                (request) -> {
                    try {
                        Long id = Long.parseLong((String) request.getBody());
                        CompletableFuture<Optional<Purchase>> client = purchaseService.removePurchase(id);
                        if(client.get().isEmpty())
                            return new Message("No purchase matched this ID.","");
                        return new Message("Purchase removed successfully", "");
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while deleting purchase.", e.getMessage());
                    }

                });
        addHandler(PurchaseServiceInterface.FIND_ONE,
                (request)->{
                    try {
                        CompletableFuture<Optional<Purchase>> purchase = purchaseService.findOnePurchase(Long.parseLong((String) request.getBody()));
                        if(purchase.get().isEmpty())
                            return new Message("No client with this ID.","");
                        return new Message("success", purchase.get().get().toString());
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while finding client.", e.getMessage());
                    }
                });

        //input: Purchase output:Optional<Purchase>
        addPurchaseHandler(PurchaseServiceInterface.ADD_PURCHASE,
                (entity)->{
                    try {
                        CompletableFuture<Optional<Purchase>> purchase = purchaseService.addPurchase(entity.getBody());
                         return new Message<Optional<Purchase>>("succes",purchase.get());
                    } catch (InterruptedException | ExecutionException e) {
                        return new Message("fail",e.getMessage());
                    }
                });
        addPurchaseHandler(PurchaseServiceInterface.UPDATE_PURCHASE,
                (entity)->{
                    try {
                        CompletableFuture<Optional<Purchase>> purchase = purchaseService.updatePurchase(entity.getBody());
                        return new Message("success", purchase.get());
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while updating client.", e.getMessage());
                    }
                });


    }

    private void initializeHandlers(){
        initializeHandlersClient();
        initializeHandlersBooks();
        initializeHandlersPurchases();
    }

    public void addHandler(String methodName, UnaryOperator<Message<String>> handler){
        methodHandlers.put(methodName,handler);
    }

    public void addClientHandler(String methodName, Function<Message<Client>,Message<Optional<Client>>> handler){
        clientHandlers.put(methodName,handler);
    }
    public void addClientHandlerSet(String methodName, Function<Message<String>,Message<HashSet<Client>>> handler){
        clientHandlersSet.put(methodName,handler);
    }
    public void addPurchaseHandler(String methodName, Function<Message<Purchase>,Message<Optional<Purchase>>> handler){
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
                ObjectInputStream ois = new ObjectInputStream(is);
                Message request = (Message)ois.readObject();
                //request.readFrom(is);
                System.out.println("Received request: "+request);
                //request header = method name
                //request body = method arguments
                Message response = new Message();
                if(methodHandlers.get(request.getHeader())!=null)
                    response = (Message) methodHandlers.get(request.getHeader()).apply(request);
                else
                    if(clientHandlers.get(request.getHeader())!=null)
                        response = (Message) clientHandlers.get(request.getHeader()).apply(request);
                    else
                    if(purchaseHandlers.get(request.getHeader())!=null)
                        response = (Message) purchaseHandlers.get(request.getHeader()).apply(request);
                    else
                    if(clientHandlersSet.get(request.getHeader())!=null)
                        response = (Message) clientHandlersSet.get(request.getHeader()).apply(request);
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(response);
                //response.writeTo(os);
            }catch(IOException | ClassNotFoundException e){
                throw new ServerException("Error processing client",e);
            }
        }
    }

}

