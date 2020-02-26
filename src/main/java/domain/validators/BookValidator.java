package domain.validators;

import domain.Book;

public class BookValidator implements Validator<domain.Book>  {
    @Override
    public void validate(Book entity) throws ValidatorException {
        // Checks if the Book is null
        if(entity == null)
            throw new ValidatorException("Book cannot be null");
        // Checks if the fields for the serial number, name, author are empty and if the year is less than 1200
        if(entity.getSerialNumber().equals("0"))
            throw new ValidatorException("Serial number cannot be zero.");
        if(entity.getName().equals("") || entity.getName().equals(" "))
            throw new ValidatorException("Name cannot be null.");
        if(entity.getAuthor().equals("") || entity.getAuthor().equals(" "))
            throw new ValidatorException("Author cannot be null.");
        if(entity.getYear() < 1200)
            throw new ValidatorException("Year cannot be negative.");

    }
}
