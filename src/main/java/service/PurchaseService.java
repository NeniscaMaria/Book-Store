package service;

import domain.Purchase;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import net.bytebuddy.dynamic.DynamicType;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import repository.PurchaseRepository;
import repository.Repository;

import javax.transaction.Transactional;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PurchaseService {
    private static final Logger log = LoggerFactory.getLogger(PurchaseService.class);
    @Autowired
    private PurchaseRepository repository;
    @Autowired
    private Validator<Purchase> validator;

    public void addPurchase(domain.Purchase purchase) throws ValidatorException{
        log.trace("addPurchase - method entered: purchase = {}",purchase);
        validator.validate(purchase);
        repository.save(purchase);
        log.trace("addPurchase - saved: purchase={}",purchase);
        log.trace("addPurchase - method finished");
    }

    public Set<Purchase> sort(Sort.Direction dir, String ...a ){
        log.trace("Purchase: sort - method entered: dir = {}, filters ={}",dir,a);
        Iterable<Purchase> purchases = repository.findAll(Sort.by(dir,a));
        log.trace("Purchase - sort: purchases sorted sorted={}",purchases);
        Set<Purchase> result = StreamSupport.stream(purchases.spliterator(), false).collect(Collectors.toSet());
        log.trace("Purchase - sort: method finished. Returned s={}",result);
        return result;
    }

    public Optional<Purchase> removePurchase(Long ID) throws SQLException {
        log.trace("removePurchase - method entered: id={}",ID);
        try {
            repository.deleteById(ID);
        }catch(EmptyResultDataAccessException er){
            log.trace("removePurchase - method finished. No Purchase with this ID");
            return Optional.empty();
        }
        log.trace("removePurchase - removed id={}",ID);
        log.trace("removePurchase - method finished");
        return Optional.of(new Purchase());
    }

    @Transactional
    public void updatePurchase(domain.Purchase purchase) throws ValidatorException {
        validator.validate(purchase);
        log.trace("updatePurchase - method entered: purchase = {}",purchase);
        repository.findById(purchase.getId())
                .ifPresent(s->{
                    s.setBookID(purchase.getBookID());
                    s.setClientID(purchase.getClientID());
                    s.setNrBooks(purchase.getNrBooks());
                    log.trace("updatePurchase - updated: s={}",s);
                });
        log.trace("updateStudent - method finished");
    }

    @Transactional
    public Set<Purchase> getAllPurchases() throws SQLException {
        log.trace("getAllPurchases - method entered");
        Iterable<domain.Purchase> purchases= repository.findAll();
        log.trace("getAllPurchase - purchases got p={}",purchases);
        Set<Purchase> result = StreamSupport.stream(purchases.spliterator(), false).collect(Collectors.toSet());
        log.trace("getAllPurchases - method finished. Returned: {} ",result);
        return result;
    }

    @Transactional
    public Set<domain.Purchase> filterPurchasesByClientID(Long clientID)  {
        log.trace("filterPurchasesByClientID - method entered: id={}",clientID);
        Iterable<domain.Purchase> purchases = repository.findAll();
        Set<domain.Purchase> filteredPurchases= new HashSet<>();
        purchases.forEach(filteredPurchases::add);
        filteredPurchases.removeIf(purchase -> !(purchase.getClientID()==clientID));
        log.trace("filterPurchasesByClientID - method finished");
        return filteredPurchases;
    }

    @Transactional
    public void removePurchaseByClientID(Long clientID){
        log.trace("removePurchaseByClientID - method entered id={}",clientID);
        long no = repository.deleteByclientID(clientID);
        log.trace("removePurchaseByClientID - method finished with {} rows deleted",no);
    }

}
