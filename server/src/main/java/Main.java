import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import java.rmi.RemoteException;

public class Main {

    public static void main(String[] args) throws RemoteException {
        System.out.println("Starting server...");
        AnnotationConfigApplicationContext context =
                new AnnotationConfigApplicationContext("SDI.server.ServerConfig");

    }
}
