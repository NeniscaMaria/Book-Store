import SDI.server.TCPServer;
import SDI.server.TCPServer2;
import SDI.server.repository.DataBase.BookDataBaseRepository;
import SDI.server.repository.DataBase.ClientDBRepository;
import SDI.server.repository.DataBase.PurchaseDataBaseRepository;
import SDI.server.repository.Repository;
import SDI.server.service.BookService;
import SDI.server.service.ClientService;
import SDI.server.service.PurchaseService;
import SDI.server.validators.BookValidator;
import SDI.server.validators.ClientValidator;
import SDI.server.validators.PurchaseValidator;
import SDI.server.validators.Validator;
import Service.ClientServiceInterface;
import domain.*;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("Starting server...");
            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            Validator<Client> clientValidator = new ClientValidator();
            Repository<Long, Client> clientRepository = new ClientDBRepository(clientValidator);
            ClientService clientService = new ClientService(clientRepository, executorService);

            Validator<Book> bookValidator = new BookValidator();
            Repository<Long, Book> bookRepository = new BookDataBaseRepository(bookValidator);
            BookService bookService = new BookService(bookRepository,executorService);

            Validator<Purchase> purchaseValidator = new PurchaseValidator(clientService,bookService);
            Repository<Long, Purchase> purchaseRepository = new PurchaseDataBaseRepository(purchaseValidator, bookRepository);
            PurchaseService purchaseService = new PurchaseService(purchaseRepository,executorService);

            //adding method handlers
            TCPServer2 server = new TCPServer2(executorService,clientService,bookService,purchaseService);
            System.out.println("Server started.");

            server.addHandler(ClientServiceInterface.ADD_CLIENT, (request) -> {
                Client client = request.getBody();
                Future<Optional<Client>> future = null;
                try {
                    future = clientService.addClient(client);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                try {
                    Optional<Client> result = future.get();
                    return new Message2<Client>("ok", result.get()); //fixme: hardcoded str
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                    return new Message2<Client>("error", client);//fixme: hardcoded str
                }


            });

            server.startServer();
            executorService.shutdown();
        }catch(RuntimeException e){
            System.out.println(e);
        }
    }
}
