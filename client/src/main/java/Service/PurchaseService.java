package Service;

import domain.Purchase;
import domain.ValidatorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PurchaseService implements PurchaseServiceInterface {

    @Autowired
    private PurchaseServiceInterface purchaseService;

    @Override
    public synchronized List<Purchase> getAllPurchases() throws SQLException, RemoteException {
        return purchaseService.getAllPurchases();
    }

    @Override
    public synchronized List<Purchase> getAllPurchases(String... args) throws SQLException, RemoteException {
        return (List<Purchase>) purchaseService.getAllPurchases(args);
    }

    @Override
    public synchronized int removePurchase(Long id) throws SQLException, RemoteException {
        return purchaseService.removePurchase(id);
    }

    @Override
    public synchronized int addPurchase(Purchase entity) throws SQLException, ValidatorException, ParserConfigurationException, TransformerException, SAXException, IOException {
        return purchaseService.addPurchase(entity);
    }

    @Override
    public synchronized List<Purchase> filter(Long clientID) throws SQLException, RemoteException {
        return purchaseService.filter(clientID);
    }

    @Override
    public synchronized int updatePurchase(Purchase entity) throws SQLException, ValidatorException, RemoteException {
        return purchaseService.updatePurchase(entity);
    }

    @Override
    public synchronized Optional<Purchase> findOnePurchase(Long id) throws SQLException, RemoteException {
        return purchaseService.findOnePurchase(id);
    }

}


