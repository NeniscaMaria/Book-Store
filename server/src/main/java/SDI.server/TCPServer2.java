package SDI.server;

import SDI.server.service.BookService;
import SDI.server.service.ClientService;
import SDI.server.service.PurchaseService;
import Service.ClientServiceInterface;
import domain.*;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.UnaryOperator;

public class TCPServer2 {

    private ExecutorService executorService;
    private Map<String, UnaryOperator<Message2<Client>>> methodHandlers;

    private ClientService clientService;
    private BookService bookService ;
    private PurchaseService purchaseService;

    public TCPServer2(ExecutorService executorService, ClientService clientService, BookService bookService, PurchaseService purchaseService) {
        this.executorService = executorService;
        methodHandlers = new HashMap<>();
        this.clientService = clientService;
        this.purchaseService = purchaseService;
        this.bookService = bookService;
        initializeHandlers();
    }

    private void initializeHandlersClient(){
        //getAllClients function

        addHandler(ClientServiceInterface.ADD_CLIENT,
                (request) -> {
                    Client c = request.getBody();
                    try {

//                        List<String> args = Arrays.asList(request.getBody().split(" "));
//                        Client client = new Client(args.get(1), args.get(2));
//                        client.setId(Long.parseLong(args.get(0)));
                        Future<Optional<Client>> clients = clientService.addClient(c);
                        return new Message2<Client>("success", c);
                    } catch (SQLException e) {
                        return new Message2<Client>("error", new Client());
                    }

                });

        addHandler(ClientServiceInterface.GET_ALL_CLIENTS,
                (request) -> {
                    try {
                        Future<Optional<Client>> clients = Optional.of(clientService.getAllClients());
                        return new Message2<Client>("success", new Client());
                    } catch (SQLException e) {
                        return new Message2<Client>("error", new Client());
                    }

                });
//        addHandler(ClientServiceInterface.REMOVE_CLIENT,
//                (request) -> {
//                    try {
//                        Long id = Long.parseLong(request.getBody());
//                        Future<Optional<Client>> client = clientService.removeClient(id);
//                        if(client.get().isEmpty())
//                            return new Message("No client matched this ID.","");
//                        return new Message("Client removed successfully", "");
//                    } catch (SQLException | InterruptedException | ExecutionException e) {
//                        return new Message("Server-side error while deleting client.", e.getMessage());
//                    }
//
//                });
        //uncomment when fixed message
//        addHandler(ClientServiceInterface.ADD_CLIENT,
//                (request)->{
//                    try {
//                        String client = request.getBody();
//                        Future<Optional<Client>> client2 = clientService.addClient(client);
//                        if(client.get().isEmpty())
//                            return new Message("Client saved successfully.","");
//                        return new Message("A client with this ID already exists.", "");
//                    } catch (SQLException | InterruptedException | ExecutionException e) {
//                        return new Message("Server-side error while deleting client.", e.getMessage());
//                    }
//                });
    }

    //TODO
    private void initializeHandlersBooks(){}
    //TODO
    private void initializeHandlersPurchases(){

//        addHandler(PurchaseService.GET_ALL_PURCHASES,
//                (request) -> {
//                    try {
//                        Future<Set<Purchase>> purchases = purchaseService.getAllPurchases();
//                        return new Message("success", purchases.get().stream().map(a->a.toString()).reduce("",(a,b)->a+b));
//                    } catch (SQLException | InterruptedException | ExecutionException e) {
//                        return new Message("error", e.getMessage());
//                    }
//
//                });
//        addHandler(PurchaseService.REMOVE_PURCHASE,
//                (request) -> {
//                    try {
//                        Long id = Long.parseLong(request.getBody());
//                        Future<Optional<Purchase>> client = purchaseService.removePurchase(id);
//                        if(client.get().isEmpty())
//                            return new Message("No purchase matched this ID.","");
//                        return new Message("Purchase removed successfully", "");
//                    } catch (SQLException | InterruptedException | ExecutionException e) {
//                        return new Message("Server-side error while deleting purchase.", e.getMessage());
//                    }
//
//                });
    }

    private void initializeHandlers(){
        initializeHandlersClient();
        initializeHandlersBooks();
        initializeHandlersPurchases();
    }

    public void addHandler(String methodName, UnaryOperator<Message2<Client>> handler){
        methodHandlers.put(methodName,handler);
    }

    public void startServer(){
        try(var serverSocket = new ServerSocket(Message.PORT)){
            while(true){
                Socket client = serverSocket.accept();
                executorService.submit(new TCPServer2.ClientHandler(client));
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
                Message2<Client> request = new Message2<Client>();
                request.readFrom(is);
                System.out.println("Received request: "+request);
                //request header = method name
                //request body = method arguments
                Message2<Client> response = (Message2<Client>) methodHandlers.get(request.getHeader()).apply(request);
                response.writeTo(os);
            }catch(IOException e){
                throw new ServerException("Error processing client",e);
            }
        }
    }
}
