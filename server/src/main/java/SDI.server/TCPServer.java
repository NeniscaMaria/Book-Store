package SDI.server;

import SDI.server.service.ClientService;
import Service.ClientServiceInterface;
import domain.*;
import domain.Message;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;

public class TCPServer {
    private ExecutorService executorService;
    private Map<String, UnaryOperator<Message>> methodHandlers;

    private ClientService clientService;

    public TCPServer(ExecutorService executorService, ClientService clientService) {
        this.executorService = executorService;
        methodHandlers = new HashMap<>();
        this.clientService = clientService;
        initializeHandlers();
    }

    private void initializeHandlersClient(){
        //getAllClients function
        addHandler(ClientServiceInterface.GET_ALL_CLIENTS,
                (request) -> {
                    try {
                        Future<Set<Client>> clients = clientService.getAllClients();
                        return new Message("success", clients.get().stream().map(a->a.toString()).reduce("",(a,b)->a+b));
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("error", e.getMessage());
                    }

                });
        addHandler(ClientServiceInterface.REMOVE_CLIENT,
                (request) -> {
                    try {
                        Long id = Long.parseLong(request.getBody());
                        Future<Optional<Client>> client = clientService.removeClient(id);
                        if(client.get().isEmpty())
                            return new Message("No client matched this ID.","");
                        return new Message("Client removed successfully", "");
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while deleting client.", e.getMessage());
                    }

                });
    }

    //TODO
    private void initializeHandlersBooks(){}
    //TODO
    private void initializeHandlersPurchases(){}

    private void initializeHandlers(){
        initializeHandlersClient();
        initializeHandlersBooks();
        initializeHandlersPurchases();
    }

    public void addHandler(String methodName, UnaryOperator<Message> handler){
        methodHandlers.put(methodName,handler);
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

