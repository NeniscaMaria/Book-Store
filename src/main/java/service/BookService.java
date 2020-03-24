package service;

import domain.Book;
import domain.validators.ValidatorException;
import org.xml.sax.SAXException;
import repository.DataBase.BookDataBaseRepository;
import repository.DataBase.implementation.Sort;
import repository.Repository;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BookService {
    private Repository<Long, Book> repository;

    public BookService(Repository<Long, domain.Book> repository) {
        this.repository = repository;
    }

    public Optional<Book> addBook(domain.Book book) throws ValidatorException, ParserConfigurationException, TransformerException, SAXException, IOException, SQLException {
        // Add given book to the repository
        // Return Optional null if the entity was added, otherwise return the entity with the same id
        return repository.save(book);
    }

    public Set<Book> getAllBooks() throws SQLException {
        // Return all books from the repository
        Iterable<domain.Book> books = repository.findAll();
        return StreamSupport.stream(books.spliterator(), false).collect(Collectors.toSet());
    }

    public Iterable<Book> getAllBooks(String ...a) throws SQLException {
        // Return all books from the repository
        Iterable<domain.Book> books;
        if (repository instanceof BookDataBaseRepository){
            books = ((BookDataBaseRepository)repository).findAll(new Sort(a).and(new Sort(a)));
            return StreamSupport.stream(books.spliterator(), false).collect(Collectors.toList());
        }
        else throw new ValidatorException("Too many parameters");

    }

    public Set<domain.Book> filterBooksByTitle(String s) throws SQLException {
        // Return the books that contain the given string
        Iterable<Book> books = repository.findAll();

        Set<Book> bookSet = new HashSet<>();
        books.forEach(bookSet::add);
        bookSet.removeIf(book -> !book.getTitle().contains(s));
        return bookSet;
    }

    public Optional<Book> updateBook(Book book) throws SQLException {
        // Update the book with the same id as the one given
        // Return Optional null if the entity was updated, otherwise the given entity (if the id does not exist)
        return repository.update(book);
    }

    public Optional<Book> deleteBook(Long bookID) throws ValidatorException, SQLException {
        // Delete the book with the given id from repository
        // Return Optional null if the entity was deleted, otherwise return the entity with the same id
        return repository.delete(bookID);
    }

    public Optional<Book> findOneBook(Long bookID) throws SQLException {
        return repository.findOne(bookID);
    }

}
