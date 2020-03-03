import domain.Book;
import domain.Client;
import domain.validators.BookValidator;
import domain.validators.ClientValidator;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import repository.BookFileRepository;
import repository.ClientFileRepository;
import repository.InMemoryRepository;
import repository.Repository;
import service.BookService;
import service.ClientService;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * I1:
 * <li>F1: add student</li>
 * <li>F2: print all students</li>
 * <li>in memory repo</li>
 *
 * I2:
 * <li>in file repo</li>
 * <li>F3: print students whose name contain a given string</li>
 */


public class Main {
    private static void runInMemory(){
        //DESCR: runs the program and saves everything in the memory
        Validator<Client> studentValidator = new ClientValidator();
        Repository<Long, Client> studentRepository = new InMemoryRepository<>(studentValidator);
        ClientService clientService = new ClientService(studentRepository);

        Validator<Book> bookValidator = new BookValidator();
        Repository<Long, Book> bookRepository = new InMemoryRepository<>(bookValidator);
        BookService bookService = new BookService(bookRepository);

        ui.Console console = new ui.Console(clientService, bookService);
        console.runConsole();
    }

    private static void runInFiles(){
        //DESCR: runs program and saves the clients and the books in files
        Validator<Client> studentValidator = new ClientValidator();
        Repository<Long, Client> clientRepository = new ClientFileRepository(studentValidator, "clients.txt");
        ClientService clientService = new ClientService(clientRepository);

        Validator<Book> bookValidator = new BookValidator();
        Repository<Long, Book> bookRepository = new BookFileRepository(bookValidator, "books.txt");
        BookService bookService = new BookService(bookRepository);

        ui.Console console = new ui.Console(clientService, bookService);
        console.runConsole();
    }
    

    public static void main(String args[]) {
        System.out.println("Choose storage option:");
        System.out.println("1.In memory");
        System.out.println("2.In files");
        boolean finished = false;
        while(!finished) {
            try {
                Scanner keyboard = new Scanner(System.in);
                System.out.println("Input choice: ");
                int choice = keyboard.nextInt();
                switch (choice) {
                    case 1:
                        runInMemory();
                        finished = true;
                        break;
                    case 2:
                        runInFiles();
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
