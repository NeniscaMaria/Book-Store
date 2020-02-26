package service;

import domain.Book;
import domain.validators.ValidatorException;
import repository.Repository;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class BookService {
    private Repository<Long, Book> repository;

    public BookService(Repository<Long, domain.Book> repository) {
        this.repository = repository;
    }

    public void addBook(domain.Book book) throws ValidatorException {
        // add given book to the repository
        repository.save(book);
    }

    public Set<Book> getAllBooks() {
        // return all books from the repository
        Iterable<domain.Book> clients = repository.findAll();
        return StreamSupport.stream(clients.spliterator(), false).collect(Collectors.toSet());
    }

    public Set<domain.Client> filterClientsByName(String s) {
        return null;
    }
}
