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
        // add given book to the repository
        return repository.save(book);
    }

    public Set<Book> getAllBooks() {
        // return all books from the repository
        Iterable<domain.Book> books = repository.findAll();
        return StreamSupport.stream(books.spliterator(), false).collect(Collectors.toSet());
    }

    public Set<domain.Book> filterBooksByName(String s) {
        return null;
    }
}
