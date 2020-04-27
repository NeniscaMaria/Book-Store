package Service;

import domain.Book;
import domain.ValidatorException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;

public interface BookServiceInterface extends Remote {

    List<Book> getAllBooks() throws SQLException, RemoteException;
//    List<Book> getAllBooks(Sort s) throws SQLException, RemoteException;
    List<Book> getAllBooks(String... args) throws SQLException, RemoteException;
    int removeBook(Long id) throws SQLException, RemoteException;
    int addBook(Book entity) throws SQLException, ValidatorException, ParserConfigurationException, TransformerException, SAXException, IOException;

    int updateBook(Book entity) throws SQLException, ValidatorException, RemoteException;
    Book findOneBook(Long bookID) throws SQLException, RemoteException;
    List<Book> filterBooksByTitle(String s) throws SQLException, RemoteException;
}
