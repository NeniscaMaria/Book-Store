package ui;

import Service.BookServiceInterface;
import Service.ClientServiceInterface;
import Service.PurchaseService;
import Service.PurchaseServiceInterface;
import domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.*;

public class Console {
    @Autowired
    private ClientServiceInterface clientService;
    @Autowired
    private PurchaseServiceInterface purchaseService;
    @Autowired
    private BookServiceInterface bookService;

    public Console(){}
    public synchronized void runConsole() {
        boolean finished = false;
        while (!finished) {
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
                        //getReport();
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
                        throw new InvalidInput("Please input a valid choice.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Please input a number.");
            } catch (InvalidInput ve) {
                System.out.println(ve.getMessage());
            }
        }
    }
    private synchronized void printChoices(){
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
        System.out.println("17.Sort clients.");
        System.out.println("18.Sort books.");
        System.out.println("19.Sort purchases.");
    }
    //--------------------------------------------
    //CLIENT
    //--------------------------------------------
    private synchronized void filterClients() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Filter after: ");
            String name = bufferRead.readLine();
            System.out.println("filtered clients (name containing "+name+" ):");
            Set<Client> result = clientService.filterClientsByName(name);
            result.stream().forEach(System.out::println);
        } catch (IOException | SQLException ex) {
            ex.printStackTrace();
        }catch (NumberFormatException ex){
            System.out.println("Please input a valid format.");
        }

    }
    private synchronized void deleteClient() {
        Scanner key = new Scanner(System.in);
        System.out.println("ID of client to be removed:");
        Long id = key.nextLong();
        try {
            int result = clientService.delete(id);
            if(result != 0)
                System.out.println("A client with this ID was not found.");
            else
                System.out.println("Client removed successfully.");
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    private synchronized void printAllClients() {
        List<Client> clients = null;
        try {
            clients = clientService.findAll();
            clients.forEach(System.out::println);
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    private synchronized void sortClients() {
       BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Please enter your filters: ");
        try {
            Iterable<Client> clients = clientService.findAll(bufferRead.readLine().split(" "));
            clients.forEach(System.out::println);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    private synchronized void addClient() {
       ///DESCR: function that saves a new client

        Optional<Client> client = readClient();
        client.ifPresent(c-> {
            try {
                int result = clientService.save(c);
                if(result == 0)
                    System.out.println("A client with this ID already exists");
                else
                    System.out.println("Client saved successfully.");
            } catch (ValidatorException e) {
                System.out.println(e.getMessage());
            } catch (SQLException | ParserConfigurationException | RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private synchronized Optional<Client> readClient() {
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

    private synchronized void updateClient(){

        Optional<Client> client = readClient();
        client.ifPresent(c->{
            try {
                int result = clientService.update(c);
                if(result == 0)
                    System.out.println("A client with this ID was not found!");
                else
                    System.out.println("Client updated successfully.");
            } catch (ValidatorException | SQLException | RemoteException e) {
                System.out.println(e.getMessage());
            }});
    }


    // ----------------
    // Books
    // ----------------

    private synchronized void filterBooks() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Filter: ");
        try {
            String filter = bufferRead.readLine();
            List<Book> books = bookService.filterBooksByTitle(filter);
            books.forEach(System.out::println);
        } catch (IOException |SQLException e) {
            e.printStackTrace();
        }
    }

    private synchronized void printAllBooks() {
        try {
            List<Book> books = bookService.getAllBooks();
            books.forEach(System.out::println);
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        }
    }

    private synchronized void addBook() {
        // Save book to repository
        Optional<Book> book = readBook();

        book.ifPresent(b-> {
            try {
                int result = bookService.addBook(b);
                if (result != 0)
                    System.out.println("Book saved successfully");
                else System.out.println("A book with this ID already exists");

            } catch (ValidatorException e) {
                System.out.println(e.getMessage());
            } catch (IOException | SQLException | ParserConfigurationException | SAXException | TransformerException e) {
                e.printStackTrace();
            }
        });
    }

    private synchronized void updateBook(){
        // Update one or more attributes of the given book
        // IDs (read) must match
        Optional<Book> book = readBook();
        book.ifPresent(b->{
            try {
                int result = bookService.updateBook(b);
                if (result != 0)
                    System.out.println("Book updated successfully");
                else System.out.println("A book with this ID does not exist");

            } catch (ValidatorException | SQLException | RemoteException e) {
                System.out.println(e.getMessage());
            }});
    }

    private synchronized void deleteBook(){
        // Delete the book with the same ID (read)
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("ID: ");

        try {
            Long id = Long.parseLong(bufferRead.readLine());
            int result = bookService.removeBook(id);
            if (result != 0)
                System.out.println("Book deleted successfully");
            else System.out.println("A book with this ID does not exist");

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }

    private synchronized Optional<Book> findOneBook(){
        // Return the book with the same ID (read) if it exists
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("ID: ");
        try {
            Long id = Long.parseLong(bufferRead.readLine());
            Book book = bookService.findOneBook(id);
            return Optional.ofNullable(book);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }


    private synchronized Optional<domain.Book> readBook() {
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

    private synchronized void sortBooks() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter your filters: ");
        try {
            Iterable<Book> books = bookService.getAllBooks(bufferRead.readLine().split(" "));
            books.forEach(System.out::println);
        }catch(Exception e){
            System.out.println(e);
        }
    }

    //******************************************************************************************************************
    //PURCHASES
    //******************************************************************************************************************

    private synchronized void addPurchase(){
        Optional<Purchase> purchase = readPurchase();
        purchase.ifPresent(p->{
            try {
                int result = purchaseService.addPurchase(p);
                if (result != 0)
                    System.out.println("Purchase saved successfully");
                else System.out.println("A purchase with this ID already exists");

            }catch(ValidatorException ex){
                System.out.println(ex.getMessage());
            } catch (IOException | SQLException | ParserConfigurationException | SAXException | TransformerException e) {
                e.printStackTrace();
            }
        });
    }
    private synchronized Optional<Purchase> readPurchase() {
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
            return Optional.of(purchase);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }
    private synchronized void updatePurchase(){
        Optional<Purchase> purchase = readPurchase();
        purchase.ifPresent(p->{
            try {
                int result = purchaseService.updatePurchase(p);
                if (result != 0)
                    System.out.println("Purchase updated successfully");
                else System.out.println("A purchase with this ID does not exist");

            }catch(ValidatorException ve){
                System.out.println(ve.getMessage());
            } catch (SQLException | RemoteException e) {
                e.printStackTrace();
            }
        });
    }

    private synchronized void displayPurchases(){
        try {
            List<Purchase> purchases = purchaseService.getAllPurchases();
            purchases.forEach(System.out::println);
        } catch (SQLException | RemoteException e) {
            e.printStackTrace();
        }
    }
    private synchronized void filterPurchases() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Filter: ");
        try {
            Long id = Long.parseLong(bufferRead.readLine());
            List<Purchase> result = purchaseService.filter(id);
            result.forEach(System.out::println);
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }
    private synchronized void deletePurchase(){
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("ID: ");
        try {
            Long id = Long.parseLong(bufferRead.readLine());
            int result = purchaseService.removePurchase(id);
            if (result != 0)
                System.out.println("Purchase deleted successfully");
            else System.out.println("A purchase with this ID does not exist");

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }

    }

    private synchronized void sortPurchases() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter your filters: ");
        try {
            Iterable<Purchase> purchases = purchaseService.getAllPurchases(bufferRead.readLine().split(" "));
            purchases.forEach(System.out::println);
        }catch(Exception e){
            System.out.println(e);
        }
    }








    //******************************************************************************************************************

//    private void getReport(){
//        //getting how many books are in stock
//        bookService.getAllBooks().thenAccept(books->{
//            Set<Book> bookset = (Set<Book>) books.getBody();
//            long nrBooksInStock = bookset.stream()
//                    .map(Book::getInStock).count();
//            System.out.println("Total books in storage : " + nrBooksInStock);});
//
//        //getting how many books were sold
//        purchaseService.getAllPurchases().thenAccept(purchases->{
//            Set<Purchase> p = purchases.getBody();
//            long soldBooks = p.stream().
//                    map(Purchase::getNrBooks).count();
//            System.out.println("Number of books sold : " + soldBooks);
//
//            //the client that bought from us more often
//            //mapping clientID to how many books he/she bought
//            Map<Long, Integer> clientIDtoBooksBought = p.stream()
//                    .collect(Collectors.groupingBy(Purchase::getClientID, Collectors.summingInt(Purchase::getNrBooks)));
//            //getting the maximum bought books
//            clientIDtoBooksBought.entrySet().stream()
//                    .max(Map.Entry.comparingByValue())
//                    .ifPresent(e -> { try{
//                        clientService.findOneClient(e.getKey()).thenAccept(client->{
//                                System.out.println("The " + client.getBody().get() + " bought the most books: " + e.getValue());
//                        });
//                    }catch(SQLException se){
//                        System.out.println(se);
//                    }
//                    });
//        });
//
//    }
}
