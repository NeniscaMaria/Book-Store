package service;

import domain.Book;
import domain.validators.ValidatorException;
import repository.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BookService {
    private Repository<Long, Book> repository;

    public BookService(Repository<Long, domain.Book> repository) {
        this.repository = repository;
    }

    public Optional<Book> addBook(domain.Book book) throws ValidatorException {
        // Add given book to the repository
        // Return Optional null if the entity was added, otherwise return the entity with the same id
        return repository.save(book);
    }

    public Set<Book> getAllBooks() {
        // Return all books from the repository
        Iterable<domain.Book> books = repository.findAll();
        return StreamSupport.stream(books.spliterator(), false).collect(Collectors.toSet());
    }

    public Set<domain.Book> filterBooksByTitle(String s) {
        // Return the books that contain the given string
        Iterable<Book> books = repository.findAll();

        Set<Book> bookSet = new HashSet<>();
        books.forEach(bookSet::add);
        bookSet.removeIf(book -> !book.getTitle().contains(s));
        return bookSet;
    }

    public Optional<Book> updateBook(Book book){
        // Update the book with the same id as the one given
        // Return Optional null if the entity was updated, otherwise the given entity (if the id does not exist)
        return repository.update(book);
    }
}
