

import SDI.server .TCPServer;
import SDI.server.repository.DataBase.ClientDBRepository;
import SDI.server.repository.Repository;
import SDI.server.service.ClientService;
import SDI.server.validators.ClientValidator;
import SDI.server.validators.Validator;
import domain.Client;
import domain.Message;

import java.sql.SQLException;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        System.out.println("Starting server...");
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        Validator<Client> clientValidator = new ClientValidator();
        Repository<Long, Client> clientRepository = new ClientDBRepository(clientValidator);
        ClientService clientService = new ClientService(clientRepository,executorService);

        TCPServer server = new TCPServer(executorService);
        server.addHandler("getAllClients",
                (request)->{
                    String name = request.getBody();
                    try {
                        Set<Client> clients = clientService.getAllClients();
                        clients.stream().forEach(System.out::println);
                        return new Message("success","ok");
                    } catch (SQLException e) {
                        e.printStackTrace();
                        return new Message("error",e.getMessage());
                    }

                });
        server.startServer();
        executorService.shutdown();
    }
}
