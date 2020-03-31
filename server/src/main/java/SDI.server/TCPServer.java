package SDI.server;

import SDI.server.service.BookService;
import SDI.server.service.ClientService;
import SDI.server.service.PurchaseService;
import Service.BookServiceInterface;
import Service.ClientServiceInterface;
import Service.PurchaseServiceInterface;
import domain.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
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
    //problem with maps that have long
    private ExecutorService executorService;
    private Map<String, UnaryOperator<Message<String>>> methodHandlers; //for methods that return string

    private Map<String, Function<Message<Client>,Message<Optional<Client>>>> clientHandlers; //for methods that need a Client as parameter
    private Map<String, Function<Message<String>,Message<Set<Client>>>> clientHandlersSet; //for methods that need a Client as parameter
    private Map<String,Function<Message<Long>, Message<Optional<Client>>>> clientHandlerLong;

    private Map<String, Function<Message<Book>, Message<Optional<Book>>>> bookHandlers; // Book
    private Map<String, Function<Message<String>, Message<Set<Book>>>> bookHandlersSet; // HashSet<Book>

    private Map<String, Function<Message<Purchase>,Message<Optional<Purchase>>>> purchaseHandlers; //for methods that need a Purchase as parameter
    private Map<String,Function<Message<Long>, Message<Optional<Purchase>>>> purchaseHandlerLong;

    private ClientService clientService;
    private BookService bookService;
    private PurchaseService purchaseService;

    public TCPServer(ExecutorService executorService, ClientService clientService, BookService bookService, PurchaseService purchaseService) {
        this.executorService = executorService;
        methodHandlers = new HashMap<>();
        clientHandlers = new HashMap<>();
        purchaseHandlers = new HashMap<>();
        clientHandlersSet = new HashMap<>();
        clientHandlerLong = new HashMap<>();
        purchaseHandlerLong = new HashMap<>();
        bookHandlers = new HashMap<>();
        bookHandlersSet = new HashMap<>();

        this.clientService = clientService;
        this.purchaseService = purchaseService;
        this.bookService = bookService;
        initializeHandlers();
    }

    private void initializeHandlersClient(){

        addHandler(ClientServiceInterface.GET_ALL_SORT,
                (request)->{
                    try {
                        CompletableFuture<Iterable<Client>> clients = clientService.getAllClients(request.getBody());
                        return new Message("success",clients.get());
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("error", e.getMessage());
                    }
                });

        //getAllClients function
        //return Set<Client> DONE
        addClientHandlerSet(ClientServiceInterface.GET_ALL_CLIENTS,
                (request) -> {
                    try {
                        CompletableFuture<Set<Client>> clients = clientService.getAllClients();
                        return new Message("success", clients.get());
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("error", e.getMessage());
                    }

                });
        addClientHandlerSet(ClientServiceInterface.FILTER_NAME,
                (request)->{
                    try {
                        CompletableFuture<Set<Client>> clients = clientService.filterClientsByName((String) request.getBody());
                        return new Message("success", clients.get());
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while filtering clients.", e.getMessage());
                    }
                });


        //input: Long output:Optional<Client>
        addClientHandlerLong(ClientServiceInterface.REMOVE_CLIENT,
                (request) -> {
                    try {
                        Long id = request.getBody();
                        System.out.println("REMOVE");
                        CompletableFuture<Optional<Client>> client = clientService.removeClient(id);
                        return new Message("success",client.get());
                    } catch (SQLException |InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while deleting client.", e.getMessage());
                    }
                });
        addClientHandlerLong(ClientServiceInterface.FIND_ONE,
                (ID)->{
                    try {
                        CompletableFuture<Optional<Client>> client = clientService.findOneClient(ID.getBody());
                        System.out.println("FIND ONE: "+client.get());
                        return new Message("success.",client.get());
                    } catch (SQLException |InterruptedException | ExecutionException e) {
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

    private void initializeHandlersBooks(){
        addBookHandlersHashSet(BookServiceInterface.GET_ALL_BOOKS,
                (request) -> {
                    try {
                        CompletableFuture<Set<Book>> books = bookService.getAllBooks();
                        return new Message("success", books.get());
                    } catch (SQLException | InterruptedException | ExecutionException e ){
                        return new Message("error", e.getMessage());
                    }


                });
        addBookHandlersHashSet(BookServiceInterface.FILTER_BOOKS,
                (request)->{
                    try {
                        CompletableFuture<Set<Book>> books = bookService.filterBooksByTitle((String) request.getBody());
                        return new Message("success", books.get());
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while filtering books.", e.getMessage());
                    }
                });


        addHandler(BookServiceInterface.REMOVE_BOOK,
                (request) -> {
                    try {
                        Long id = Long.parseLong((String) request.getBody());
                        CompletableFuture<Optional<Book>> book = bookService.removeBook(id);
                        if(book.get().isEmpty())
                            return new Message("No book matched this ID.","");
                        return new Message("Client book successfully", "");
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while deleting book.", e.getMessage());
                    }

                });
        addHandler(BookServiceInterface.FIND_ONE,
                (ID)->{
                    try {
                        CompletableFuture<Optional<Book>> book = bookService.findOneBook(Long.parseLong((String) ID.getBody()));
                        if(book.get().isEmpty())
                            return new Message("No book with this ID.","");
                        return new Message("succes.", book.get().get().toString());
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while finding book.", e.getMessage());
                    }
                });

        addBookHandler(BookServiceInterface.ADD_BOOK,
                (entity)->{
                    try {
                        System.out.println("Adding book");
                        CompletableFuture<Optional<Book>> book = bookService.addBook(entity.getBody());
                        return new Message<Optional<Book>>("success", book.get());
                    } catch (InterruptedException | ExecutionException | ParserConfigurationException | TransformerException | SAXException | IOException | SQLException e ){
                        return new Message("Server-side error while deleting book.", e.getMessage());
                    }

                });
        addBookHandler(BookServiceInterface.UPDATE_BOOK,
                (entity)->{
                    try {
                        CompletableFuture<Optional<Book>> book = bookService.updateBook(entity.getBody());
                        return new Message<Optional<Book>>("success",book.get());
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while updating book.", e.getMessage());
                    }
                });
    }

    private void initializeHandlersPurchases(){
        //output: Set<Purchase> DONE
        addHandler(PurchaseServiceInterface.GET_ALL_PURCHASES,
                (request) -> {
                    try {
                        CompletableFuture<Set<Purchase>> purchases = purchaseService.getAllPurchases();
                        return new Message("success", purchases.get());
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("error", e.getMessage());
                    }

                });
        addHandler(PurchaseServiceInterface.FILTER,
                (request)->{
                    try {
                        CompletableFuture<Set<Purchase>> purchases = purchaseService.filterPurchasesByClientID(Long.parseLong((String) request.getBody()));
                        return new Message("success", purchases.get());
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while filtering purchases.", e.getMessage());
                    }
                });

        //input: Long output: Optional<Purchase>
        addPurchaseHandlerLong(PurchaseServiceInterface.REMOVE_PURCHASE,
                (request) -> {
                    try {
                        Long id = request.getBody();
                        CompletableFuture<Optional<Purchase>> p = purchaseService.removePurchase(id);
                        return new Message("success",p.get());
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while deleting purchase.", e.getMessage());
                    }
                });

        addPurchaseHandlerLong(PurchaseServiceInterface.FIND_ONE,
                (request)->{
                    try {
                        CompletableFuture<Optional<Purchase>> purchase = purchaseService.findOnePurchase(request.getBody());
                        return new Message("success",purchase.get());
                    } catch (SQLException | InterruptedException | ExecutionException e) {
                        return new Message("Server-side error while finding client.", e.getMessage());
                    }
                });

        //input: Purchase output:Optional<Purchase> DONE
        addPurchaseHandler(PurchaseServiceInterface.ADD_PURCHASE,
                (entity)->{
                    try {
                        CompletableFuture<Optional<Purchase>> purchase = purchaseService.addPurchase(entity.getBody());
                        return new Message<>("succes", purchase.get());
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
    public void addClientHandlerLong(String methodName,Function<Message<Long>, Message<Optional<Client>>> handler){
        clientHandlerLong.put(methodName,handler);
    }
    public void addClientHandler(String methodName, Function<Message<Client>,Message<Optional<Client>>> handler){
        clientHandlers.put(methodName,handler);
    }
    public void addClientHandlerSet(String methodName, Function<Message<String>,Message<Set<Client>>> handler){
        clientHandlersSet.put(methodName,handler);
    }
    public void addBookHandler(String methodName, Function<Message<Book>,Message<Optional<Book>>> handler){
        bookHandlers.put(methodName,handler);
    }
    public void addBookHandlersHashSet(String methodName, Function<Message<String>,Message<Set<Book>>> handler){
        bookHandlersSet.put(methodName,handler);
    }

    public void addPurchaseHandler(String methodName, Function<Message<Purchase>,Message<Optional<Purchase>>> handler){
        purchaseHandlers.put(methodName,handler);
    }
    public void addPurchaseHandlerLong(String methodName,Function<Message<Long>, Message<Optional<Purchase>>> handler){
        purchaseHandlerLong.put(methodName,handler);
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

        private Message getResponse(Message request){
            if(methodHandlers.get(request.getHeader())!=null)
                return methodHandlers.get(request.getHeader()).apply(request);
//<<<<<<< HEAD
            else {
                if (clientHandlers.get(request.getHeader()) != null)
                    return clientHandlers.get(request.getHeader()).apply(request);
                else {
                    if (purchaseHandlers.get(request.getHeader()) != null)
                        return purchaseHandlers.get(request.getHeader()).apply(request);
                    else {
                        if (clientHandlersSet.get(request.getHeader()) != null)
                            return clientHandlersSet.get(request.getHeader()).apply(request);
                        else {
                            if (clientHandlerLong.get(request.getHeader()) != null)
                                return clientHandlerLong.get(request.getHeader()).apply(request);
                            else{
                                if(purchaseHandlerLong.get(request.getHeader())!=null)
                                    return clientHandlerLong.get(request.getHeader()).apply(request);
                                else {
                                    if (bookHandlers.get(request.getHeader()) != null)
                                        return bookHandlers.get(request.getHeader()).apply(request);
                                    else
                                    if(bookHandlersSet.get(request.getHeader())!=null)
                                        return bookHandlersSet.get(request.getHeader()).apply(request);
                                }

                            }
                        }
                    }
                }
            }
//=======
//            if (clientHandlers.get(request.getHeader()) != null)
//                return clientHandlers.get(request.getHeader()).apply(request);
//            if (purchaseHandlers.get(request.getHeader()) != null)
//                return purchaseHandlers.get(request.getHeader()).apply(request);
//            if (clientHandlersSet.get(request.getHeader()) != null)
//                return clientHandlersSet.get(request.getHeader()).apply(request);
//            if (clientHandlerLong.get(request.getHeader()) != null)
//                return clientHandlerLong.get(request.getHeader()).apply(request);
//            if(purchaseHandlerLong.get(request.getHeader())!=null)
//                return purchaseHandlerLong.get(request.getHeader()).apply(request);
//>>>>>>> e717568e2609057cca37c260c3cdb19d70922f82
            return null;
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
                Message response = getResponse(request);
                System.out.println("CLIENT_HANDLER_SERVER: "+response);
                ObjectOutputStream oos = new ObjectOutputStream(os);
                oos.writeObject(response);
                //response.writeTo(os);
            }catch(IOException | ClassNotFoundException e){
                throw new ServerException("Error processing client",e);
            }
        }
    }

}

