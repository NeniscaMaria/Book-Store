package domain;


import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Book extends BaseEntity<Long>{
    /*
    A book has a serialNumber (String), name (string),
    author (String), year of publication (int)
     */
    @Column(nullable=false)
    private String serialNumber;
    @Column(nullable=false)
    private String title;
    @Column(nullable=false)
    private String author;
    @Column(nullable=false)
    private int year;
    @Column(nullable=false)
    private double price;
    @Column(nullable=false)
    private int inStock;
    public Book() {
    }

    public Book(String serialNumber, String name, String author, int year, double price, int inStock) {
        this.serialNumber = serialNumber;
        this.title = name;
        this.author = author;
        this.year = year;
        this.price = price;
        this.inStock = inStock;

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

    public int getInStock() {
        return inStock;
    }

    public void setInStock(int inStock) {
        this.inStock = inStock;
    }

    @Override
    public String toString() {
        return "Book{" +
                "serialNumber='" + serialNumber + '\'' +
                ", name='" + title + '\'' +
                ", author='" + author + '\'' +
                ", year=" + year +
                ", price=" + price +
                ", inStock=" + inStock +
                "} " + super.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Book book = (Book) o;
        return year == book.year &&
                Double.compare(book.price, price) == 0 &&
                inStock == book.inStock &&
                serialNumber.equals(book.serialNumber) &&
                title.equals(book.title) &&
                author.equals(book.author);
    }

    @Override
    public int hashCode() {
        int result = serialNumber.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + author.hashCode();
        result = 31 * result + year;
        result = 31 * result + (int)price; //cast the double to an int so that we get a valid hash code
        result = 31 * result + inStock;
        return result;
    }

}
