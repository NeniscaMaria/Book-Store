package domain.validators;

import domain.Book;

import java.util.Calendar;

public class BookValidator implements Validator<domain.Book>  {
    boolean checkNull(String stringToBeChecked){
        return stringToBeChecked.equals("") || stringToBeChecked.equals(" ");
    }
    boolean isValid(String name){
        // Name can't contain numbers, special characters
        // If multiple authors, they are separate by ';'
        return name.contains("1234567890=+_[]{}()~`!@#$%^&*<>,./\\?");
    }
    @Override
    public void validate(Book entity) throws ValidatorException {
        // Checks if the Book is null
        if(entity == null)
            throw new ValidatorException("Book cannot be null");
        if(entity.getId() < 0)
            throw new ValidatorException("Please enter a non-negative ID.");
        if(entity.getSerialNumber().equals("0"))
            throw new ValidatorException("Please enter a non-zero serial number.");
        if(checkNull(entity.getName()))
            throw new ValidatorException("Please enter a non-null name.");
        if(checkNull(entity.getAuthor()) || isValid(entity.getAuthor()))
            throw new ValidatorException("Please enter a non-null author name.");
        if(entity.getYear() < 0 || entity.getYear() > Calendar.getInstance().get(Calendar.YEAR))
            throw new ValidatorException("Please enter a valid year");

    }
}
