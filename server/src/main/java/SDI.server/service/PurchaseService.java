package SDI.server.service;

import SDI.server.repository.DataBase.PurchaseDataBaseRepository;
import SDI.server.repository.DataBase.implementation.Sort;
import SDI.server.repository.Repository;
import Service.PurchaseServiceInterface;
import domain.ValidatorException;
import domain.Purchase;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PurchaseService  implements PurchaseServiceInterface {

    private Repository<Long, Purchase> repository;
    private ExecutorService executorService;

    public PurchaseService(Repository<Long, domain.Purchase> repository, ExecutorService executorService) {
        this.repository = repository;
        this.executorService = executorService;
    }

    public Future<Optional<Purchase>> addPurchase(domain.Purchase purchase) throws ValidatorException{
        return executorService.submit(()->repository.save(purchase));
    }

    public void removeClients(Long ID) throws IOException, SAXException, ParserConfigurationException {
        repository.removeEntitiesWithClientID(ID);
    }

    public Future<Optional<Purchase>> removePurchase(Long ID) throws SQLException {
        return executorService.submit(()->repository.delete(ID));
    }

    public Future<Optional<Purchase>> updatePurchase(domain.Purchase purchase) throws ValidatorException, SQLException {
        return executorService.submit(()->repository.update(purchase));
    }

    public Future<Set<Purchase>> getAllPurchases() throws SQLException {
        Iterable<domain.Purchase> purchases= repository.findAll();
        Set<Purchase> result = StreamSupport.stream(purchases.spliterator(), false).collect(Collectors.toSet());
        return executorService.submit(()->result);
    }

    public Iterable<Purchase> getAllPurchases(String ...a) throws SQLException {

        Iterable<domain.Purchase> pur;
        if (repository instanceof PurchaseDataBaseRepository){
            pur = ((PurchaseDataBaseRepository)repository).findAll(new Sort(a).and(new Sort(a)));
            return StreamSupport.stream(pur.spliterator(), false).collect(Collectors.toList());
        }
        else throw new ValidatorException("Too many parameters");

    }

    public Future<Set<domain.Purchase>> filterPurchasesByClientID(Long clientID) throws SQLException {
        return executorService.submit(()->{
            Iterable<domain.Purchase> purchases = repository.findAll();
            Set<domain.Purchase> filteredPurchases= new HashSet<>();
            purchases.forEach(filteredPurchases::add);
            filteredPurchases.removeIf(purchase -> !(purchase.getClientID()==clientID));
            return filteredPurchases;
        });
    }

    public Future<Optional<Purchase>> findOnePurchase(Long purchaseID) throws SQLException {
        return executorService.submit(()->repository.findOne(purchaseID));
    }
}
