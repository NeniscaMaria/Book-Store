import domain.Book;
import domain.Client;
import domain.Purchase;
import domain.validators.*;
import repository.*;
import repository.DataBase.BookDataBaseRepository;
import repository.DataBase.ClientDBRepository;
import repository.DataBase.PurchaseDataBaseRepository;
import service.BookService;
import service.ClientService;
import service.PurchaseService;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    private static void runJDBC(){
        Validator<Client> clientValidator = new ClientValidator();
        Repository<Long, Client> clientRepository = new ClientDBRepository(clientValidator);
        ClientService clientService = new ClientService(clientRepository);

        Validator<Book> bookValidator = new BookValidator();
        Repository<Long, Book> bookRepository = new BookDataBaseRepository(bookValidator);
        BookService bookService = new BookService(bookRepository);

        Validator<Purchase> purchaseValidator = new PurchaseValidator(clientService,bookService);
        Repository<Long, Purchase> purchaseRepository = new PurchaseDataBaseRepository(purchaseValidator, bookRepository);
        PurchaseService purchaseService = new PurchaseService(purchaseRepository);

        ui.Console console = new ui.Console(clientService, bookService, purchaseService);
        console.runConsole();
    }

    public static void main(String args[]) {
        System.out.println("Choose storage option:");
        System.out.println("1.Start");
        System.out.println("2.Exit");
        boolean finished = false;
        while(!finished) {
            try {
                Scanner keyboard = new Scanner(System.in);
                System.out.println("Input choice: ");
                int choice = keyboard.nextInt();
                switch (choice) {
                    case 1:
                        runJDBC();
                        finished = true;
                        break;
                    case 2:
                        finished = true;
                        break;
                    default:
                        throw new ValidatorException("Please input a valid choice.");
                }
            } catch (ValidatorException ve) {
                System.out.println(ve.getMessage());
            }catch(InputMismatchException e ){
                System.out.println("Please input a number.");
            }
        }
    }
}
