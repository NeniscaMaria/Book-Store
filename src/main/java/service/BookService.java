package service;

import domain.Book;
import domain.Client;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;
import repository.Repository;

import javax.transaction.Transactional;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class BookService {
    private static final Logger log = LoggerFactory.getLogger(BookService.class);
    @Autowired
    private Repository<Long, Book> repository;
    @Autowired
    private Validator<Book> validator;

    public void addBook(Book book){
        log.trace("addBook - method entered book={}",book);
        validator.validate(book);
        repository.save(book);
        log.trace("addBook - book added book={}",book);
        log.trace("addBook - method finished");
    }

    public Set<Book> getAllBooks() throws SQLException {
        log.trace("getAllBooks - method entered");
        Iterable<domain.Book> books = repository.findAll();
        Set<Book> result = StreamSupport.stream(books.spliterator(), false).collect(Collectors.toSet());
        log.trace("getAllBooks - method finished. Returned: {}",result);
        return result;
    }
    public Set<Book> sort(Sort.Direction dir, String ...a ){
        log.trace("Books: sort - method entered dir={} filters={}",dir,a);
        Iterable<Book> books = repository.findAll(Sort.by(dir,a));
        Set<Book> result = StreamSupport.stream(books.spliterator(), false).collect(Collectors.toSet());
        log.trace("Books: sort - method finished. Returned sorted={}",result);
        return result;
    }

    public Set<domain.Book> filterBooksByTitle(String s) throws SQLException {
        log.trace("filterBooksByTitle - method entered title={}",s);
        Iterable<Book> books = repository.findAll();

        Set<Book> bookSet = new HashSet<>();
        books.forEach(bookSet::add);
        bookSet.removeIf(book -> !book.getTitle().contains(s));
        log.trace("filterBooksByTitle - method finished. Returned : books={}",bookSet);
        return bookSet;
    }

    @Transactional
    public void updateBook(Book book) throws SQLException {
        log.trace("updateBook - method entered: book={}", book);
        validator.validate(book);
        repository.findById(book.getId())
                .ifPresent(s -> {
                    s.setSerialNumber(book.getSerialNumber());
                    s.setTitle(book.getTitle());
                    s.setAuthor(book.getAuthor());
                    s.setYear(book.getYear());
                    s.setPrice(book.getPrice());
                    s.setInStock(book.getInStock());
                    log.debug("updateBook - updated: s={}", s);
                });
        log.trace("updateBook - method finished");
    }

    public Optional<Book> deleteBook(Long bookID) throws ValidatorException, SQLException {
        log.trace("deleteBook - method entered : id={}",bookID);
        try {
            repository.deleteById(bookID);
        }catch(EmptyResultDataAccessException er){
            log.trace("deleteBook - method finished. No Purchase with this ID");
            return Optional.empty();
        }
        log.trace("deleteBook - book deleted id={}",bookID);
        log.trace("deleteBook - method finished");
        return Optional.of(new Book());
    }

    public Optional<Book> findOne(Long bookID) throws SQLException {
        log.trace("findOne book - method entered id={}",bookID);
        log.trace("findOne book - method finished");
        return repository.findById(bookID);
    }

}
