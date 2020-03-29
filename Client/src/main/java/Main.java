import Service.ClientService;
import TCP.TCPClient;
import ui.Console;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        TCPClient tcpClient= new TCPClient();
        ClientService clientService = new ClientService(executorService,tcpClient);
        Console console = new Console(clientService);
        console.runConsole();

        executorService.shutdown();

    }
}
