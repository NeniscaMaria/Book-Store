package ui;

import domain.validators.BookValidator;
import domain.validators.ValidatorException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Set;

public class Console {
    private service.ClientService clientService;
    private service.BookService bookService;

    public Console(service.ClientService studentService, service.BookService bookService) {
        this.clientService = studentService;
        this.bookService = bookService;
    }

    public void runConsole() {
        boolean finished=false;
        while(!finished){
            printChoices();
            try {
                Scanner keyboard = new Scanner(System.in);
                System.out.println("Input your choice: ");
                int choice = keyboard.nextInt();
                if (isChoiceOK(choice))
                    switch (choice) {
                        case 0:
                            finished = true;
                            break;
                        case 1:
                            addClient();
                            break;
                        case 2:
                            addBook();
                            break;
                        case 3:
                            printAllClients();
                            break;
                        case 4:
                            printAllBooks();
                            break;
                        case 5:
                            filterClients();
                            break;
                        case 6:
                            filterBooks();
                            break;
                        default:
                            break;
                    }
            }catch(InputMismatchException e ){
                System.out.println("Please input a number.");
            }
        }

    }
    boolean isChoiceOK(int choiceToCheck){
        return choiceToCheck>=0 && choiceToCheck<11;
    }
    private void printChoices(){
        System.out.println("\nChoose one from below:");
        System.out.println("0.Exit");
        System.out.println("1.Add new client.");
        System.out.println("2.Add new book.");
        System.out.println("3.Show all clients.");
        System.out.println("4.Show all books.");
        System.out.println("5.Filter clients.");
        System.out.println("6.Filter books.");
        System.out.println("7.Delete client.");
        System.out.println("8.Delete book.");
        System.out.println("9.Update client.");
        System.out.println("10.Update book.");
    }

    //Client functions:
    private void filterClients() {
        System.out.println("filtered clients (name containing 's2'):");
        Set<domain.Client> students = clientService.filterClientsByName("s2");
        students.stream().forEach(System.out::println);
    }

    private void printAllClients() {
        Set<domain.Client> students = clientService.getAllClients();
        students.stream().forEach(System.out::println);
    }

    private void addClient() {
        domain.Client client = readClient();
        if (client != null) {
            try {
                if (clientService.addClient(client).isPresent())
                    System.out.println("A client with this ID already exists.");
            } catch (ValidatorException e) {
                System.out.println(e.getMessage());
            }
        }else
            System.out.println("Please try again.");
    }

    private domain.Client readClient() {
        System.out.println("Please enter a new client: ");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("ID: ");
            Long id = Long.parseLong(bufferRead.readLine());
            System.out.println("Serial Number: ");
            String serialNumber = bufferRead.readLine();
            System.out.println("Name: ");
            String name = bufferRead.readLine();

            domain.Client student = new domain.Client(serialNumber, name);
            student.setId(id);

            return student;
        } catch (IOException ex) {
            ex.printStackTrace();
        }catch (NumberFormatException ex){
            System.out.println("Please input a valid format.");
        }
        return null;
    }

    // ----------------
    // Books

    private void filterBooks() {
        System.out.println("filtered books (name containing 's2'):");
        Set<domain.Book> books = bookService.filterBooksByName("s2");
        books.stream().forEach(System.out::println);
    }

    private void printAllBooks() {
        Set<domain.Book> books = bookService.getAllBooks();
        books.stream().forEach(System.out::println);
    }

    private void addBook() {
        domain.Book book = readBook();
        BookValidator validator = new BookValidator();
        try {
            validator.validate(book);
            bookService.addBook(book);
        } catch (ValidatorException e) {
            System.out.println(e.getMessage());
        }
    }

    private domain.Book readBook() {
        System.out.println("Please enter a new book: ");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("ID: ");
            Long id = Long.parseLong(bufferRead.readLine());
            System.out.println("Serial Number: ");
            String serialNumber = bufferRead.readLine();
            System.out.println("Name: ");
            String name = bufferRead.readLine();
            System.out.println("Author: ");
            String author = bufferRead.readLine();
            System.out.println("Year: ");
            int year = Integer.parseInt(bufferRead.readLine());

            domain.Book book = new domain.Book(serialNumber, name, author, year);
            book.setId(id);

            return book;
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return null;
    }
}
