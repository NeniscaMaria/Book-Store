package ui;

import domain.Book;
import domain.Client;
import domain.Purchase;
import domain.validators.BookValidator;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;
import service.PurchaseService;

import javax.swing.text.html.Option;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@Component
public class Console {
    @Autowired
    private service.ClientService clientService;
    @Autowired
    private service.BookService bookService;
    @Autowired
    private service.PurchaseService purchaseService;
    //gitk and git gui in console

    public void runConsole() {
        boolean finished=false;
        while(!finished){
            printChoices();
            try {
                Scanner keyboard = new Scanner(System.in);
                System.out.println("Input your choice: ");
                int choice = keyboard.nextInt();
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
                    case 7:
                        deleteClient();
                        break;
                    case 8:
                        deleteBook();
                        break;
                    case 9:
                        updateClient();
                        break;
                    case 10:
                        updateBook();
                        break;
                    case 11:
                        addPurchase();
                        break;
                    case 12:
                        displayPurchases();
                        break;
                    case 13:
                        updatePurchase();
                        break;
                    case 14:
                        deletePurchase();
                        break;
                    case 15:
                        filterPurchases();
                        break;
                    case 16:
                        getReport();
                        break;
                    case 17:
                        sortClients();
                        break;
                    case 18:
                        sortBooks();
                        break;
                    case 19:
                        sortPurchases();
                        break;
                    default:
                        throw new ValidatorException("Please input a valid choice.");
                    }
            }catch(InputMismatchException e ){
                System.out.println("Please input a number.");
            }catch(ValidatorException ve){
                System.out.println(ve.getMessage());
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

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
        System.out.println("11.Buy a book.");
        System.out.println("12.Show all purchases.");
        System.out.println("13.Update purchase.");
        System.out.println("14.Delete purchase.");
        System.out.println("15.Filter purchases.");
        System.out.println("16.Report.");
    }

    //******************************************************************************************************************
    //Client functions:
    private void filterClients() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Filter after: ");
            String name = bufferRead.readLine();
            System.out.println("filtered clients (name containing "+name+" ):");
            Set<domain.Client> students = clientService.filterClientsByName(name);
            students.stream().forEach(System.out::println);
        } catch (IOException ex) {
            ex.printStackTrace();
        }catch (NumberFormatException ex){
            System.out.println("Please input a valid format.");
        }

    }

    private void deleteClient(){
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("ID: ");
            Long id = Long.parseLong(bufferRead.readLine());
            Optional<Client> client = clientService.removeClient(id);
            client.ifPresent(c -> {System.out.println("Client removed successfully."); });
            purchaseService.removePurchaseByClientID(id);
        } catch (IOException  ex) {
            ex.printStackTrace();
        }catch (NumberFormatException ex){
            System.out.println("Please input a valid format.");
        }
    }

    private void printAllClients() {
        //DESCR: function that prints all the clients saved until now
        clientService.getAllClients().stream().forEach(System.out::println);

    }

    private void addClient() {
        //DESCR: function that saves a new client

        Optional<Client> client = readClient();
        client.ifPresent(c->{
            try {
                Optional<Client> result = clientService.addClient(c);
                result.ifPresent(r-> System.out.println("Client added."));
            } catch (ValidatorException e) {
                System.out.println(e.getMessage());
            }});
    }

    private Optional<Client> readClient() {
        //DESCR: function that reads a client from the keyboard
        //POST: returns the client read

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

            return Optional.of(student);
        } catch (IOException ex) {
            ex.printStackTrace();
        }catch (NumberFormatException ex){
            System.out.println("Please input a valid format.");
        }
        return Optional.empty();
    }

    private void updateClient(){
        Optional<Client> client = readClient();
        client.ifPresent(c->{
            try {
                clientService.updateClient(c);
                //Optional<Client> result = clientService.updateClient(c);
                //result.ifPresent(r -> {
                //    throw new ValidatorException("Client updated successfully!");});
               // throw new ValidatorException("A client with this ID was not found!");

            } catch (ValidatorException  e) {
                System.out.println(e.getMessage());
            }});
    }
    private void sortClients() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter how to order the elements: ");
        Sort.Direction dir = null;
        try {
            if (bufferRead.readLine().equals("DESC"))
                dir = Sort.Direction.DESC;
            if (bufferRead.readLine().equals("ASC"))
                dir = Sort.Direction.ASC;
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Please enter your filters: ");

        try {
            Set<Client> clients = clientService.sort(dir,bufferRead.readLine().split(" "));
            clients.forEach(System.out::println);
        }catch( IOException e){
            System.out.println(e);
        }
    }

    // ----------------
    // Books
    // ----------------

    private void filterBooks() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Filter: ");
        try {
            String filter = bufferRead.readLine();
            Set<Book> filteredBooks = bookService.filterBooksByTitle(filter);
            filteredBooks.forEach(System.out::println);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void printAllBooks() {
        // Print all books from repository
        Set<Book> books = null;
        try {
            books = bookService.getAllBooks();
            books.stream().forEach(System.out::println);
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    private void addBook() {
        // Save book to repository
        Optional<Book> book = readBook();

        book.ifPresent(b->{
            try{
                bookService.addBook(b);
                //Optional<Book> book2 = bookService.addBook(b);
                //book2.ifPresent(b2->System.out.println("A book with this ID already exists."));
            }
            catch (ValidatorException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    private void updateBook(){
        // Update one or more attributes of the given book
        // IDs (read) must match
        Optional<Book> book = readBook();
        book.ifPresent(b->{
            try{
                bookService.updateBook(b);
                //Optional<Book> book2 = bookService.updateBook(b);
                //book2.ifPresent(b2->{throw new ValidatorException("Book updated successfully");});
                //throw new ValidatorException("The book with this ID does not exist.");
            }
            catch (ValidatorException | SQLException e) {
                System.out.println(e.getMessage());
            }
        });
    }

    private void deleteBook(){
        // Delete the book with the same ID (read)
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("ID: ");

        try {
            Long id = Long.parseLong(bufferRead.readLine());
            Optional<Book> book = bookService.deleteBook(id);
            book.ifPresent(b2->{throw new ValidatorException("Book removed successfully");});
            throw new ValidatorException("The book with this ID does not exist.");
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    private Optional<Book> findOneBook(){
        // Return the book with the same ID (read) if it exists
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("ID: ");
        try {
            Long id = Long.parseLong(bufferRead.readLine());
            return bookService.findOne(id);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    private Optional<domain.Book> readBook() {
        // Input book from keyboard
        System.out.println("Please enter a new book: ");

        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("ID: ");
            Long id = Long.parseLong(bufferRead.readLine());
            System.out.println("Serial Number: ");
            String serialNumber = bufferRead.readLine();
            System.out.println("Title: ");
            String name = bufferRead.readLine();
            System.out.println("Author: ");
            String author = bufferRead.readLine();
            System.out.println("Year: ");
            int year = Integer.parseInt(bufferRead.readLine());
            System.out.println("Price: ");
            double price = Double.parseDouble(bufferRead.readLine());
            System.out.println("In stock: ");
            int stock = Integer.parseInt(bufferRead.readLine());

            domain.Book book = new domain.Book(serialNumber, name, author, year, price, stock);
            book.setId(id);

            return Optional.of(book);
        } catch (IOException ex) {
            ex.printStackTrace();
        }catch (NumberFormatException ex){
            System.out.println("Please input a valid format.");
        }
        return Optional.empty();
    }
    private void sortBooks() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter how to order the elements: ");
        Sort.Direction dir = null;
        try {
            if (bufferRead.readLine().equals("DESC"))
                dir = Sort.Direction.DESC;
            if (bufferRead.readLine().equals("ASC"))
                dir = Sort.Direction.ASC;
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Please enter your filters: ");

        try {
            Set<Book> books = bookService.sort(dir,bufferRead.readLine().split(" "));
            books.forEach(System.out::println);
        }catch( IOException e){
            System.out.println(e);
        }
    }

    //******************************************************************************************************************
    //PURCHASES
    //******************************************************************************************************************

    private void addPurchase(){

        Optional<Purchase> purchase = readPurchase();
        purchase.ifPresent(p->{ purchaseService.addPurchase(p); });
    }

    private void displayPurchases(){
        Set<Purchase> purchases = null;
        try {
            purchases = purchaseService.getAllPurchases();
            purchases.stream().forEach(System.out::println);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updatePurchase(){

        Optional<Purchase> purchase = readPurchase();
        purchase.ifPresent(p->{
            Optional<Purchase> pp = null;
            purchaseService.updatePurchase(p);
            pp.ifPresent(ppp -> {throw new ValidatorException("Purchase updated successfully");});
            throw new ValidatorException("This purchase was not found successfully");
        });
    }

    private void deletePurchase(){
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("ID: ");
            Long id = Long.parseLong(bufferRead.readLine());
            purchaseService.removePurchase(id);
            Optional<Purchase> purchase = purchaseService.removePurchase(id);
            purchase.ifPresent(p->{
                System.out.println("Purchase removed successfully");
                int nr = p.getNrBooks();
                Long idBook = p.getBookID();

                Book b = null;
                try {
                    b = bookService.findOne(idBook).get();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                Book newBook = new Book(b.getSerialNumber(), b.getTitle(), b.getTitle(), b.getYear(), b.getPrice(), b.getInStock() + nr);
                newBook.setId(b.getId());

                try {
                    bookService.updateBook(newBook);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            });



        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }

    private Optional<Purchase> readPurchase() {
        System.out.println("Please enter a new purchase: ");
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));


        try {
            System.out.println("ID: ");
            Long id = Long.parseLong(bufferRead.readLine());
            System.out.println("ID client: ");
            Long idClient = Long.parseLong(bufferRead.readLine());

            System.out.println("ID book: ");
            Long idBook = Long.parseLong(bufferRead.readLine());

            System.out.println("Number of books: ");
            int nrBooks = Integer.parseInt(bufferRead.readLine());

            Purchase purchase = new Purchase(idClient, idBook, nrBooks);
            purchase.setId(id);

            Optional<Book> book = bookService.findOne(idBook);

            book.ifPresent(b->{
                int nr = b.getInStock();
                b.setInStock(nr - nrBooks);

                Book newBook = new Book(b.getSerialNumber(), b.getTitle(), b.getTitle(), b.getYear(), b.getPrice(), b.getInStock());
                newBook.setId(b.getId());
                try {
                    bookService.updateBook(newBook);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            });


            return Optional.of(purchase);
        } catch (IOException | SQLException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    private void filterPurchases() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Filter: ");
        try {
            Long filter = Long.parseLong(bufferRead.readLine());
            Set<Purchase> filteredPurchase = purchaseService.filterPurchasesByClientID(filter);
            filteredPurchase.forEach(System.out::println);
        } catch (IOException  e) {
            e.printStackTrace();
        }

    }
    private void sortPurchases() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter how to order the elements: ");
        Sort.Direction dir = null;
        try {
            if (bufferRead.readLine().equals("DESC"))
                dir = Sort.Direction.DESC;
            if (bufferRead.readLine().equals("ASC"))
                dir = Sort.Direction.ASC;
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Please enter your filters: ");

        try {
            Set<Purchase> purchases = purchaseService.sort(dir,bufferRead.readLine().split(" "));
            purchases.forEach(System.out::println);
        }catch( IOException e){
            System.out.println(e);
        }
    }
    //******************************************************************************************************************

    private void getReport() throws SQLException {//move in service
        //getting how many books are in stock
        long nrBooksInStock = bookService.getAllBooks().stream()
                .map(Book::getInStock).count();
        System.out.println("Total books in storage : "+nrBooksInStock);

        //getting how many books were sold
        long soldBooks = purchaseService.getAllPurchases().stream().
                map(Purchase::getNrBooks).count();
        System.out.println("Number of books sold : "+soldBooks);

        //the client that bought from us more often
        //mapping clientID to how many books he/she bought
        Map<Long, Integer> clientIDtoBooksBought = purchaseService.getAllPurchases().stream()
                .collect(Collectors.groupingBy(Purchase::getClientID,Collectors.summingInt(Purchase::getNrBooks)));
        //getting the maximum bought books
        clientIDtoBooksBought.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .ifPresent(e-> {
                        System.out.println("The "+ clientService.findOneClient(e.getKey()).get() + " bought the most books: "+e.getValue());
                });
    }


}
