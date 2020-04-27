package SDI.server.service;

import SDI.server.repository.DataBase.BookDataBaseRepository;
import Service.BookServiceInterface;
import domain.Sort;
import domain.ValidatorException;
import domain.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class BookService implements BookServiceInterface {

    @Autowired
    private BookDataBaseRepository bookrepo;
    @Autowired
    private Sort sort;

    public BookService() throws RemoteException {

    }

    @Override
    public List<Book> getAllBooks() throws SQLException, RemoteException {
        return bookrepo.findAll();
    }

    @Override
    public List<Book> getAllBooks(String... args) throws SQLException, RemoteException {
        sort.setInfo(args);
        return bookrepo.findAll(sort);
    }

    @Override
    public int removeBook(Long id) throws SQLException, RemoteException {
        return bookrepo.delete(id);
    }

    @Override
    public int addBook(Book entity) throws SQLException, ValidatorException, RemoteException, ParserConfigurationException, TransformerException, SAXException, IOException {
        return bookrepo.save(entity);
    }

    @Override
    public int updateBook(Book entity) throws SQLException, RemoteException, ValidatorException {
        return bookrepo.update(entity);
    }

    @Override
    public Book findOneBook(Long bookID) throws SQLException, RemoteException {
        return bookrepo.findOne(bookID).get();
    }

    @Override
    public List<Book> filterBooksByTitle(String s) throws SQLException, RemoteException {
        return getAllBooks().stream().filter(book -> book.getTitle().contains(s)).collect(Collectors.toList());

    }




}
