package domain.validators;

import domain.Purchase;
import java.util.Optional;

public class PurchaseValidator implements Validator<Purchase> {
    @Override
    public void validate(domain.Purchase entity) throws ValidatorException {

        Optional<Purchase> purchase = Optional.ofNullable(Optional.ofNullable(entity).orElseThrow(ValidatorException::new));
        purchase.ifPresent(p->{
            if(entity.getId()<0)
                throw new ValidatorException("Please choose a non-negative ID.");
            //check if clientID and bookID exist

        });
    }

}
