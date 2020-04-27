package SDI.server.ServerConfig;
import SDI.server.repository.DataBase.BookDataBaseRepository;
import SDI.server.repository.DataBase.ClientDBRepository;
import SDI.server.repository.DataBase.PurchaseDataBaseRepository;
import SDI.server.service.BookService;
import SDI.server.service.ClientService;
import SDI.server.service.PurchaseService;
import SDI.server.validators.BookValidator;
import SDI.server.validators.ClientValidator;
import SDI.server.validators.PurchaseValidator;
import Service.BookServiceInterface;
import Service.ClientServiceInterface;
import Service.PurchaseServiceInterface;

import domain.Sort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiServiceExporter;

import java.rmi.RemoteException;

@Configuration
public class ServerConfig {
    @Bean
    RmiServiceExporter rmiServiceExporter() throws RemoteException {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceName("ClientService");
        rmiServiceExporter.setServiceInterface(ClientServiceInterface.class);
        rmiServiceExporter.setService(clientService());
        return rmiServiceExporter;
    }
    @Bean
    ClientDBRepository clientDBRepository() throws RemoteException{
        return new ClientDBRepository();
    }
    @Bean
    ClientValidator clientValidator() throws RemoteException{
        return new ClientValidator();
    }

    @Bean
    ClientServiceInterface clientService() throws RemoteException {
        return new ClientService(){};
    }
    @Bean
    RmiServiceExporter rmiServiceExporterBook() throws RemoteException {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceName("BookService");
        rmiServiceExporter.setServiceInterface(BookServiceInterface.class);
        rmiServiceExporter.setService(bookService());
        return rmiServiceExporter;
    }

    @Bean
    BookDataBaseRepository bookDataBaseRepository() throws RemoteException{
        return new BookDataBaseRepository();
    }

    @Bean
    BookServiceInterface bookService() throws RemoteException {
        return new BookService() {
        };
    }

    @Bean
    BookValidator bookValidator() throws RemoteException{
        return new BookValidator();
    }


    @Bean
    RmiServiceExporter rmiServiceExporterPurchase() throws RemoteException {
        RmiServiceExporter rmiServiceExporter = new RmiServiceExporter();
        rmiServiceExporter.setServiceName("PurchaseService");
        rmiServiceExporter.setServiceInterface(PurchaseServiceInterface.class);
        rmiServiceExporter.setService(purchaseService());
        return rmiServiceExporter;
    }

    @Bean
    PurchaseServiceInterface purchaseService() throws RemoteException {
        return new PurchaseService() {
        };
    }

    @Bean
    Sort sort() throws RemoteException {
        return new Sort();
    }

    @Bean
    PurchaseDataBaseRepository purchaseDataBaseRepository() throws RemoteException{
        return new PurchaseDataBaseRepository();
    }
    @Bean
    PurchaseValidator purchaseValidator() throws RemoteException{
        return new PurchaseValidator(clientService(),bookService());
    }

}
