package config;

import Service.BookServiceInterface;
import Service.ClientServiceInterface;
import Service.PurchaseServiceInterface;
import domain.Sort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.remoting.rmi.RmiProxyFactoryBean;
import ui.Console;

import java.rmi.RemoteException;

@Configuration
public class ClientConfig {
    @Bean
    RmiProxyFactoryBean rmiProxyFactoryBean() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();
        rmiProxyFactoryBean.setServiceInterface(ClientServiceInterface.class);
        rmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/ClientService");

        return rmiProxyFactoryBean;
    }
    @Bean
    Console console() throws RemoteException{
        return new Console();
    }
    @Bean
    RmiProxyFactoryBean rmiProxyFactoryBeanBook() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();

        rmiProxyFactoryBean.setServiceInterface(BookServiceInterface.class);
        rmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/BookService");
        return rmiProxyFactoryBean;
    }
    @Bean
    RmiProxyFactoryBean rmiProxyFactoryBeanPurchase() {
        RmiProxyFactoryBean rmiProxyFactoryBean = new RmiProxyFactoryBean();

        rmiProxyFactoryBean.setServiceInterface(PurchaseServiceInterface.class);
        rmiProxyFactoryBean.setServiceUrl("rmi://localhost:1099/PurchaseService");
        return rmiProxyFactoryBean;
    }

}
