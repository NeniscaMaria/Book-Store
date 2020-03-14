package repository;

import domain.Client;
import domain.Purchase;
import domain.validators.Validator;

import java.util.Map;

public class PurchaseXMLRepository extends InMemoryRepository<Long, Purchase> {
    Map<Client,Purchase> purchases;

    public PurchaseXMLRepository(Validator<Purchase> validator, Map<Client, Purchase> purchases) {
        super(validator);
        this.purchases = purchases;
    }
}
