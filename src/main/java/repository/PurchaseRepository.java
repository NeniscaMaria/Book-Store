package repository;

import domain.Purchase;

public interface PurchaseRepository extends Repository<Long, Purchase> {
    long deleteByclientID(Long clientID);
}
