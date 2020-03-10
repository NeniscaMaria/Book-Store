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
        return repository.save(book);
    }

    public Set<Book> getAllBooks() {
        // Return all books from the repository
        Iterable<domain.Book> books = repository.findAll();
        return StreamSupport.stream(books.spliterator(), false).collect(Collectors.toSet());
    }

    public Set<domain.Book> filterBooksByTitle(String s) {
        Iterable<Book> books = repository.findAll();

        Set<Book> bookSet = new HashSet<>();
        books.forEach(bookSet::add);
        bookSet.removeIf(book -> !book.getName().contains(s));
        return bookSet;
    }
}
