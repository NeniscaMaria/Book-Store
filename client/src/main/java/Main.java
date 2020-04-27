import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import ui.Console;

import java.net.MalformedURLException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

public class Main {
    public static void main(String[] args) throws RemoteException, NotBoundException, MalformedURLException {
        AnnotationConfigApplicationContext context =
        new AnnotationConfigApplicationContext("config");
        Console console=context.getBean(Console.class);
        console.runConsole();
    }
}
