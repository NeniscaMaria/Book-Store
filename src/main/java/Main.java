

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
    public static void main(String args[]) {
        //in-memory repo
//         Validator<Student> studentValidator = new StudentValidator();
//         Repository<Long, Student> studentRepository = new InMemoryRepository<>(studentValidator);
//         StudentService studentService = new StudentService(studentRepository);
//         Console console = new Console(studentService);
//         console.runConsole();

        //file repo
//        try {
//            System.out.println(new File(".").getCanonicalPath());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //in file repo
        domain.validators.Validator<domain.Client> studentValidator = new domain.validators.ClientValidator();
        repository.Repository<Long, domain.Client> clientRepository = new repository.ClientFileRepository(studentValidator, "clients.txt");
        service.ClientService clientService = new service.ClientService(clientRepository);

        // book

        domain.validators.Validator<domain.Book> bookValidator = new domain.validators.BookValidator();
        repository.Repository<Long, domain.Book> bookRepository = new repository.BookFileRepository(bookValidator, "books.txt");
        service.BookService bookService = new service.BookService(bookRepository);

        ui.Console console = new ui.Console(clientService, bookService);
        console.runConsole();
//        ui.Console console = new ui.Console(clientService);
//        console.runConsole();
    }
}
