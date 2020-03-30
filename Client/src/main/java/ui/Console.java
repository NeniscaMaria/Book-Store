package ui;

import Service.ClientService;
import Service.PurchaseService;
import domain.Client;
import domain.Message;
import domain.Purchase;
import domain.ValidatorException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
public class Console {
    private ClientService clientService;
    private PurchaseService purchaseService;

    public Console(ClientService clientService,PurchaseService purchaseService) {
        this.clientService = clientService;
        this.purchaseService = purchaseService;
    }

    public void runConsole() {
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
                        //addBook();
                        break;
                    case 3:
                        printAllClients();
                        break;
                    case 4:
                        //printAllBooks();
                        break;
                    case 5:
                        filterClients();
                        break;
                    case 6:
                        //filterBooks();
                        break;
                    case 7:
                        deleteClient();
                        break;
                    case 8:
                        //deleteBook();
                        break;
                    case 9:
                        updateClient();
                        break;
                    case 10:
                        //updateBook();
                        break;
                    case 11:
                        //addPurchase();
                        break;
                    case 12:
                        displayPurchases();
                        break;
                    case 13:
                        //updatePurchase();
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
                        //sortBooks();
                        break;
                    case 19:
                        //sortPurchases();
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
        System.out.println("17.Sort clients.");
        System.out.println("18.Sort books.");
        System.out.println("19.Sort purchases.");
    }

    //******************************************************************************************************************
    //CLIENT
    //WHAT WORKS:
    //delete, display, filter
    //******************************************************************************************************************
    private void filterClients() {
        BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Filter after: ");
            String name = bufferRead.readLine();
            System.out.println("filtered clients (name containing "+name+" ):");
            Future<Message> result = clientService.filterClientsByName(name);
            System.out.println(result.get().getBody());
        } catch (IOException | SQLException | InterruptedException | ExecutionException ex) {
            ex.printStackTrace();
        }catch (NumberFormatException ex){

            System.out.println("Please input a valid format.");
        }

    }

    private void deleteClient(){
        Scanner key = new Scanner(System.in);
        System.out.println("ID of client to be removed:");
        Long id = key.nextLong();
        Future<Message> result = clientService.removeClient(id);
        try {
            System.out.println(result.get().getHeader());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void printAllClients() {
        Future<Message> clients = clientService.getAllClients();
        try {
            System.out.println(clients.get().getBody());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void addClient() {
       ///DESCR: function that saves a new client

        Optional<Client> client = readClient();
        client.ifPresent(c->{
            try {
                Future<Message> result = clientService.addClient(c);
                System.out.println(result.get().getHeader());
                //result.ifPresent(r-> System.out.println("A client with this ID already exists."));
            } catch (ValidatorException e) {
                System.out.println(e.getMessage());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });
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
        /*Optional<Client> client = readClient();
        client.ifPresent(c->{
            try {
                Optional<Client> result = clientService.updateClient(c);
                result.ifPresent(r -> {
                    throw new ValidatorException("Client updated successfully!");});
                throw new ValidatorException("A client with this ID was not found!");

            } catch (ValidatorException | SQLException e) {
                System.out.println(e.getMessage());
            }});*/
    }

    private void sortClients() {
       /* BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Please enter how to order the elements: ");


        try {
            if (bufferRead.readLine().equals("DESC"))
                Sort.dir = Sort.Direction.DESC;
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Please enter your filters: ");

        try {
            Iterable<Client> clients = clientService.getAllClients(bufferRead.readLine().split(" "));
            clients.forEach(System.out::println);
        }catch(SQLException | IOException e){
            System.out.println(e);
        }*/
    }

    //******************************************************************************************************************
    //PURCHASES
    //WHAT WORKS:
    //delete, display, filter
    //******************************************************************************************************************

    private void addPurchase(){

       /* Optional<Purchase> purchase = readPurchase();
        purchase.ifPresent(p->{
            try {
                Optional<Purchase> pur = purchaseService.addPurchase(p);
                pur.ifPresent(pp -> System.out.println("A purchase with this ID already exists"));

            } catch (ParserConfigurationException | TransformerException | SAXException | IOException |SQLException  e) {
                e.printStackTrace();
            }

        });*/
    }

    private void displayPurchases(){
        Future<Message> purchases = purchaseService.getAllPurchases();
        try {
            System.out.println(purchases.get().getBody());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void updatePurchase(){
/*
        Optional<Purchase> purchase = readPurchase();
        purchase.ifPresent(p->{
            try {
                Optional<Purchase> pp = purchaseService.updatePurchase(p);
                pp.ifPresent(ppp -> {
                    throw new ValidatorException("Purchase updated successfully");
                });
                throw new ValidatorException("This purchase was not found successfully");
            }catch(SQLException e){
                System.out.println(e);
            }
        });*/
    }

    private void deletePurchase(){
        Scanner key = new Scanner(System.in);
        System.out.println("ID of purchase to be removed:");
        Long id = key.nextLong();
        Future<Message> result = purchaseService.removePurchase(id);
        try {
            System.out.println(result.get().getHeader());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

    }

    private void sortPurchases() {
        /*BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Please enter how to order the elements: ");


        try {
            if (bufferRead.readLine().equals("DESC"))
                Sort.dir = Sort.Direction.DESC;
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Please enter your filters: ");

        try {
            Iterable<Purchase> pur = purchaseService.getAllPurchases(bufferRead.readLine().split(" "));
            pur.forEach(System.out::println);
        }catch(SQLException | IOException e){
            System.out.println(e);
        }*/
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
            return Optional.of(purchase);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return Optional.empty();
    }

    private void filterPurchases() {
       BufferedReader bufferRead = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Filter: ");
        try {
            Long filter = Long.parseLong(bufferRead.readLine());
            Future<Message> result = purchaseService.filterPurchasesByClientID(filter);
            System.out.println(result.get().getBody());
        } catch (IOException | InterruptedException | ExecutionException | SQLException e) {
            e.printStackTrace();
        }

    }





/*
    private BookService bookService;
    private PurchaseService purchaseService;

    public Console(ClientService studentService, BookService bookService, PurchaseService purchaseService) {
        this.clientService = studentService;
        this.bookService = bookService;
        this.purchaseService=purchaseService;
    }
    //gitk and git gui in console


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
        } catch (IOException |SQLException e) {
            e.printStackTrace();
        }
    }

    private void printAllBooks() {
        // Print all books from repository
        try {
            Set<domain.Book> books = bookService.getAllBooks();
            books.stream().forEach(System.out::println);
        }catch(SQLException e){
            System.out.println(e);
        }
    }

    private void addBook() {
        // Save book to repository
        Optional<Book> book = readBook();

        book.ifPresent(b->{
            try{
                Optional<Book> book2 = bookService.addBook(b);
                book2.ifPresent(b2->System.out.println("A book with this ID already exists."));
            }
            catch (ValidatorException e) {
                System.out.println(e.getMessage());
            } catch (IOException | ParserConfigurationException | SAXException | TransformerException | SQLException e) {
                e.printStackTrace();
            }
        });
    }

    private void updateBook(){
        // Update one or more attributes of the given book
        // IDs (read) must match
        Optional<Book> book = readBook();
        book.ifPresent(b->{
            try{
                Optional<Book> book2 = bookService.updateBook(b);
                book2.ifPresent(b2->{throw new ValidatorException("Book updated successfully");});
                throw new ValidatorException("The book with this ID does not exist.");
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
            return bookService.findOneBook(id);
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


        try {
            if (bufferRead.readLine().equals("DESC"))
                Sort.dir = Sort.Direction.DESC;
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("Please enter your filters: ");

        try {
            Iterable<Book> books = bookService.getAllBooks(bufferRead.readLine().split(" "));
            books.forEach(System.out::println);
        }catch(SQLException | IOException e){
            System.out.println(e);
        }
    }


    //******************************************************************************************************************

    private void getReport(){
        try {
            //getting how many books are in stock
            long nrBooksInStock = StreamSupport.stream(bookService.getAllBooks().spliterator(), false)
                    .map(Book::getInStock).count();
            System.out.println("Total books in storage : " + nrBooksInStock);

            //getting how many books were sold
            long soldBooks = StreamSupport.stream(purchaseService.getAllPurchases().spliterator(), false).
                    map(Purchase::getNrBooks).count();
            System.out.println("Number of books sold : " + soldBooks);

            //the client that bought from us more often
            //mapping clientID to how many books he/she bought
            Map<Long, Integer> clientIDtoBooksBought = purchaseService.getAllPurchases().stream()
                    .collect(Collectors.groupingBy(Purchase::getClientID, Collectors.summingInt(Purchase::getNrBooks)));
            //getting the maximum bought books
            clientIDtoBooksBought.entrySet().stream()
                    .max(Comparator.comparing(Map.Entry::getValue))
                    .ifPresent(e -> { try{
                        System.out.println("The " + clientService.findOneClient(e.getKey()).get() + " bought the most books: " + e.getValue());
                    }catch(SQLException se){
                        System.out.println(se);
                    }
                    });
        }catch(SQLException e){
            System.out.println(e);
        }
    }

*/


}
