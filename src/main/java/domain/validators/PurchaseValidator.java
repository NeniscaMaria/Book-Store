package domain.validators;

import domain.Book;
import domain.Client;
import domain.Purchase;
import service.BookService;
import service.ClientService;
import java.util.Optional;

public class PurchaseValidator implements Validator<Purchase> {
    private ClientService clients;
    private BookService books;

    private boolean clientExists(Long ID){ //checks if a client with this ID exists
        Optional<Client> client = clients.findOneClient(ID);
        return client.isPresent();
    }

    private boolean bookExists(Long ID){//checks if a book with this ID exists
        Optional<Book> book = books.findOneBook(ID);
        return book.isPresent();
    }

    private boolean isBookInSock(Long ID, int nrBooks){ //checks if there are enough books in stock for this operation to take place
        Optional<Book> book = books.findOneBook(ID);
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
            if (!clientExists(entity.getClientID()))
                throw new ValidatorException("This client does not exist.");
            if (!bookExists(entity.getBookID()))
                throw new ValidatorException("This book does not exist.");
            //validate the stock
            if (!isBookInSock(entity.getBookID(),entity.getNrBooks()))
                throw new ValidatorException("We don't have that many books of this type in stock.");

        });
    }

    public PurchaseValidator(ClientService clients, BookService books) {
        this.clients = clients;
        this.books = books;
    }
}
