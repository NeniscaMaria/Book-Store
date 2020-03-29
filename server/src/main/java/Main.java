import SDI.server.TCPServer;
import SDI.server.repository.DataBase.ClientDBRepository;
import SDI.server.repository.Repository;
import SDI.server.service.ClientService;
import SDI.server.validators.ClientValidator;
import SDI.server.validators.Validator;
import domain.Client;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) {
        try {
            System.out.println("Starting server...");
            ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            Validator<Client> clientValidator = new ClientValidator();
            Repository<Long, Client> clientRepository = new ClientDBRepository(clientValidator);
            ClientService clientService = new ClientService(clientRepository, executorService);

            //adding method handlers
            TCPServer server = new TCPServer(executorService,clientService);
            System.out.println("Server started.");
            server.startServer();
            executorService.shutdown();
        }catch(RuntimeException e){
            System.out.println(e);
        }
    }
}
