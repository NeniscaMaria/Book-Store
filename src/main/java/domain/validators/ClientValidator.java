package domain.validators;

public class ClientValidator implements Validator<domain.Client> {
    @Override
    public void validate(domain.Client entity) throws ValidatorException {
        if(entity.getName()=="" || entity.getName()==" ")
            throw new ValidatorException("Name cannot be null!");
        if(entity.getSerialNumber()=="0")
            throw new ValidatorException("Serial number cannot be zero.");
    }
}
