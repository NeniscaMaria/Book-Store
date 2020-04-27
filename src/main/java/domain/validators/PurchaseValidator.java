package domain.validators;

import domain.Book;
import domain.Client;
import domain.Purchase;
import org.springframework.stereotype.Component;
import service.BookService;
import service.ClientService;

import java.sql.SQLException;
import java.util.Optional;

@Component
public class PurchaseValidator implements Validator<Purchase> {
    private ClientService clients;
    private BookService books;

    private boolean clientExists(Long ID) throws SQLException { //checks if a client with this ID exists
        Optional<Client> client = clients.findOneClient(ID);
        return client.isPresent();
    }

    private boolean bookExists(Long ID) throws SQLException {//checks if a book with this ID exists
        Optional<Book> book = books.findOne(ID);
        return book.isPresent();
    }

    private boolean isBookInSock(Long ID, int nrBooks) throws SQLException { //checks if there are enough books in stock for this operation to take place
        if (nrBooks==0)
            return false;
        Optional<Book> book = books.findOne(ID);
        //we know here for sure that the book exists because we check if the book exists before we call this function
        return book.get().getInStock()>=nrBooks;
    }

    @Override
    public void validate(domain.Purchase entity) throws ValidatorException {

        Optional<Purchase> purchase = Optional.ofNullable(Optional.ofNullable(entity).orElseThrow(()-> new ValidatorException("Entity is null.")));
        purchase.ifPresent(p->{
            if(entity.getId()<0)
                throw new ValidatorException("Please choose a non-negative ID.");
            //validate if the client and the book exist
            try {
                if (!clientExists(entity.getClientID()))
                    throw new ValidatorException("This client does not exist.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            try {
                if (!bookExists(entity.getBookID()))
                    throw new ValidatorException("This book does not exist.");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            //validate the stock
            try {
                if (!isBookInSock(entity.getBookID(),entity.getNrBooks()))
                    throw new ValidatorException("We don't have that many books of this type in stock or you selected 0 books.");
            } catch (SQLException e) {
                e.printStackTrace();
            }

        });
    }

    public PurchaseValidator(ClientService clients, BookService books) {
        this.clients = clients;
        this.books = books;
    }
}
