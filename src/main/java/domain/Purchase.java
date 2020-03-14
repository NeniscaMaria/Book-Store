package domain;

import java.util.Objects;

public class Purchase extends BaseEntity<Long>{
    // Purchase receives a Client, Book and a number of books
    // the client wishes to purchase

    Client client;
    Book book;
    int nrBooks;

    public Purchase() {
    }

    public Purchase(Client client, Book book, int nrBooks) {
        this.client = client;
        this.book = book;
        this.nrBooks = nrBooks;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getNrBooks() {
        return nrBooks;
    }

    public void setNrBooks(int nrBooks) {
        this.nrBooks = nrBooks;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Purchase purchase = (Purchase) o;
        return nrBooks == purchase.nrBooks &&
                Objects.equals(client, purchase.client) &&
                Objects.equals(book, purchase.book);
    }

    @Override
    public int hashCode() {
        return Objects.hash(client, book, nrBooks);
    }

    @Override
    public String toString() {
        return "Purchase{" +
                "client=" + client +
                ", book=" + book +
                ", nrBooks=" + nrBooks +
                '}' + super.toString();
    }
}
