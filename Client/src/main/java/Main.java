import Service.ClientService;
import Service.ClientService2;
import Service.PurchaseService;
import TCP.TCPClient;
import TCP.TCPClient2;
import ui.Console;

import java.sql.Connection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static void main(String[] args) {
        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

        TCPClient2 tcpClient= new TCPClient2();
        ClientService2 clientService = new ClientService2(executorService,tcpClient);
//        PurchaseService purchaseService = new PurchaseService(executorService,tcpClient);
        Console console = new Console(clientService);
        console.runConsole();

        executorService.shutdown();

    }
}
