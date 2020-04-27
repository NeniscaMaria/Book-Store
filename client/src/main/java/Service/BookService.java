package Service;

import domain.Book;
import domain.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public class BookService implements BookServiceInterface{
    @Autowired
    private BookServiceInterface bookService;

    @Override
    public synchronized List<Book> getAllBooks() throws SQLException, RemoteException {
        return bookService.getAllBooks();
    }

    @Override
    public synchronized List<Book> getAllBooks(String... args) throws SQLException, RemoteException {
        return bookService.getAllBooks(args);
    }

    @Override
    public synchronized int removeBook(Long id) throws SQLException, RemoteException {
        return bookService.removeBook(id);
    }

    @Override
    public synchronized int addBook(Book entity) throws SQLException, ValidatorException, ParserConfigurationException, TransformerException, SAXException, IOException {
        return bookService.addBook(entity);
    }

    @Override
    public synchronized int updateBook(Book entity) throws SQLException, ValidatorException, RemoteException {
        return bookService.updateBook(entity);
    }

    @Override
    public synchronized Book findOneBook(Long bookID) throws SQLException, RemoteException {
        return bookService.findOneBook(bookID);
    }

    @Override
    public synchronized List<Book> filterBooksByTitle(String s) throws SQLException, RemoteException {
        return bookService.filterBooksByTitle(s);
    }
}
