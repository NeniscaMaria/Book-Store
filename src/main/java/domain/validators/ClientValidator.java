package domain.validators;

public class ClientValidator implements Validator<domain.Client> {
    boolean checkNull(String stringToBeChecked){
        return stringToBeChecked=="" || stringToBeChecked==" ";
    }
    @Override
    public void validate(domain.Client entity) throws ValidatorException {
        if(entity==null)
            throw new ValidatorException("Client is null!");
        if(entity.getId()<0)
            throw new ValidatorException("Please choose a non-negative ID.");
        if(checkNull(entity.getName()))
            throw new ValidatorException("Please insert a valid name.");
        if(checkNull(entity.getSerialNumber()))
            throw new ValidatorException("Please insert a valid serial number.");
    }
}
