package repository;

import domain.Book;
import domain.Client;
import domain.Purchase;
import domain.validators.BookValidator;
import domain.validators.ClientValidator;
import domain.validators.PurchaseValidator;
import domain.validators.Validator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import service.BookService;
import service.ClientService;

import java.util.HashSet;

import static org.junit.Assert.*;

public class PurchaseFileRepositoryTest {
    private Book book;
    private Client client;
    private Book book2;
    private Client client2;
    private Purchase purchase;
    private Purchase purchase2;

    private static final Long ID = 1L;
    private static final Long NEW_ID = 2L;
    private static final Long ID_CLIENT = 1L;
    private static final Long ID_CLIENT2 = 2L;
    private static final Long ID_BOOK1 = 1L;
    private static final Long ID_BOOK2 = 2L;
    private static final int NR_BOOKS = 20;
    private static final int NR_BOOKS2 = 18;
    private  String filename;
    private HashSet allPurchases;

    private Validator<Client> validClient;
    private Validator<Book> validBook;
    private Validator<Purchase> validPurchase;

    private ClientFileRepository repoClient;
    private BookFileRepository repoBook;

    private ClientService cs;
    private BookService bs;

    @Before
    public void setUp() throws Exception {
        filename = "purchaseTest.txt";

        validClient = new ClientValidator();
        validBook = new BookValidator();



        client = new Client();
        client.setId(ID_CLIENT);
        book = new Book();
        book.setId(ID_BOOK1);
        purchase = new Purchase(ID_CLIENT, ID_BOOK1, NR_BOOKS);
        purchase.setId(ID);

        client2 = new Client();
        client.setId(ID_CLIENT);
        book2 = new Book();
        book2.setId(ID_BOOK2);
        purchase = new Purchase(ID_CLIENT2, ID_BOOK2, NR_BOOKS2);
        purchase.setId(ID);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void save() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }
}