package SDI.server.validators;

import SDI.server.service.BookService;
import SDI.server.service.ClientService;
import Service.BookServiceInterface;
import Service.ClientServiceInterface;
import domain.Book;
import domain.Client;
import domain.Purchase;
import domain.ValidatorException;

import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

public class PurchaseValidator implements Validator<Purchase> {
    private ClientServiceInterface clients;
    private BookServiceInterface books;

    private boolean clientExists(Long ID) throws SQLException, ExecutionException, InterruptedException, RemoteException { //checks if a client with this ID exists
        Optional<Client> client = clients.findOne(ID);
        return client.isPresent();
    }

    private boolean bookExists(Long ID) throws SQLException, RemoteException {//checks if a book with this ID exists

        Book book = books.findOneBook(ID);
        return Optional.of(book).isPresent();
    }

    private boolean isBookInSock(Long ID, int nrBooks) throws SQLException, RemoteException { //checks if there are enough books in stock for this operation to take place
        if (nrBooks==0)
            return false;

        Book book = books.findOneBook(ID);
        //we know here for sure that the book exists because we check if the book exists before we call this function
        return book.getInStock()>=nrBooks;
    }

    @Override
    public void validate(domain.Purchase entity) throws ValidatorException {

        Optional<Purchase> purchase = Optional.ofNullable(Optional.ofNullable(entity).orElseThrow(() -> new ValidatorException("Entity is null.")));
        purchase.ifPresent(p->{
            if(entity.getId()<0)
                throw new ValidatorException("Please choose a non-negative ID.");
            //validate if the client and the book exist
            try {
                if (!clientExists(entity.getClientID()))
                    throw new ValidatorException("This client does not exist.");
            } catch (SQLException | InterruptedException | ExecutionException | RemoteException e) {
                e.printStackTrace();
            }
            try {
                if (!bookExists(entity.getBookID()))
                    throw new ValidatorException("This book does not exist.");
            } catch (SQLException | RemoteException e) {
                e.printStackTrace();
            }
            //validate the stock
            try {
                if (!isBookInSock(entity.getBookID(),entity.getNrBooks()))
                    throw new ValidatorException("We don't have that many books of this type in stock or you selected 0 books.");
            } catch (SQLException | RemoteException e) {
                e.printStackTrace();
            }

        });
    }

    public PurchaseValidator(ClientServiceInterface clients, BookServiceInterface books) {
        this.clients = clients;
        this.books = books;
    }
}
