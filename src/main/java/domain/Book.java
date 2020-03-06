package domain;

import java.util.Objects;

public class Book extends BaseEntity<Long>{
    /*
    A book has a serialNumber (String), name (string),
    author (String), year of publication (int)
     */
    private String serialNumber;
    private String title;
    private String author;
    private int year;
    private double price;
    public Book() {
    }

    public Book(String serialNumber, String title, String author, int year, double price) {
        this.serialNumber = serialNumber;
        this.title = title;
        this.author = author;
        this.year = year;
        this.price = price;
    }

    public double getPrice(){
        return this.price;
    }

    public void setPrice(double priceFromUser){
        this.price=priceFromUser;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return year == book.year &&
                Objects.equals(serialNumber, book.serialNumber) &&
                Objects.equals(title, book.title) &&
                Objects.equals(price, book.price) &&
                Objects.equals(author, book.author);
    }


    @Override
    public int hashCode() {
        int result = serialNumber.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + year;
        result = 31 * result + (int)price; //cast the double to an int so that we get a valid hash code
        return result;
    }

    @Override
    public String toString() {
        return super.toString() + "Book{" +
                "serialNumber='" + serialNumber + '\'' +
                ", title='" + title + '\'' +
                ", author=" + author + '\'' +
                ", year=" + year + '\'' +
                ", price=" + price + '\'' +
                "} ";
    }
}
