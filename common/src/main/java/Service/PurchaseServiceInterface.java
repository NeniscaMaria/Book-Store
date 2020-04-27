package Service;


import domain.Purchase;
import domain.ValidatorException;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public interface PurchaseServiceInterface extends Remote {
    List<Purchase> getAllPurchases() throws SQLException, RemoteException;
    //    List<Book> getAllBooks(Sort s) throws SQLException, RemoteException;
    Iterable<Purchase> getAllPurchases(String... args) throws SQLException, RemoteException;
    int removePurchase(Long id) throws SQLException, RemoteException;
    int addPurchase(Purchase entity) throws SQLException, ValidatorException, ParserConfigurationException, TransformerException, SAXException, IOException;
    List<Purchase> filter(Long clientID) throws SQLException, RemoteException;

    int updatePurchase(Purchase entity) throws SQLException, ValidatorException, RemoteException;
    Optional<Purchase> findOnePurchase(Long id) throws SQLException, RemoteException;
}