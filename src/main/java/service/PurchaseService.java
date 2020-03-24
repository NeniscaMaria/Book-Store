package service;

import domain.Book;
import domain.Purchase;
import domain.validators.ValidatorException;
import org.xml.sax.SAXException;
import repository.DataBase.BookDataBaseRepository;
import repository.DataBase.PurchaseDataBaseRepository;
import repository.DataBase.implementation.Sort;
import repository.Repository;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PurchaseService {
    private Repository<Long, Purchase> repository;

    public PurchaseService(Repository<Long, domain.Purchase> repository) {
        this.repository = repository;
    }

    public Optional<Purchase> addPurchase(domain.Purchase purchase) throws ValidatorException, ParserConfigurationException, TransformerException, SAXException, IOException, SQLException {
        return repository.save(purchase);
    }

    public void removeClients(Long ID) throws IOException, SAXException, ParserConfigurationException {
        repository.removeEntitiesWithClientID(ID);
    }

    public Optional<Purchase> removePurchase(Long ID) throws SQLException {
        return repository.delete(ID);
    }

    public Optional<Purchase> updatePurchase(domain.Purchase purchase) throws ValidatorException, SQLException {
        return repository.update(purchase);
    }

    public Set<Purchase> getAllPurchases() throws SQLException {
        Iterable<domain.Purchase> purchases= repository.findAll();
        return StreamSupport.stream(purchases.spliterator(), false).collect(Collectors.toSet());
    }

    public Iterable<Purchase> getAllPurchases(String ...a) throws SQLException {
        Iterable<domain.Purchase> pur;
        if (repository instanceof PurchaseDataBaseRepository){
            pur = ((PurchaseDataBaseRepository)repository).findAll(new Sort(a).and(new Sort(a)));
            return StreamSupport.stream(pur.spliterator(), false).collect(Collectors.toList());
        }
        else throw new ValidatorException("Too many parameters");

    }

    public Set<domain.Purchase> filterPurchasesByClientID(Long clientID) throws SQLException {
        Iterable<domain.Purchase> purchases = repository.findAll();
        Set<domain.Purchase> filteredPurchases= new HashSet<>();
        purchases.forEach(filteredPurchases::add);
        filteredPurchases.removeIf(purchase -> !(purchase.getClientID()==clientID));

        return filteredPurchases;
    }

    public Optional<Purchase> findOnePurchase(Long purchaseID) throws SQLException {
        return repository.findOne(purchaseID);
    }
}
