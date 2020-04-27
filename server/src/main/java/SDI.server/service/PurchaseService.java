package SDI.server.service;

import SDI.server.repository.DataBase.PurchaseDataBaseRepository;
import SDI.server.validators.PurchaseValidator;
import Service.BookServiceInterface;
import Service.ClientServiceInterface;
import Service.PurchaseServiceInterface;
import domain.Sort;
import domain.ValidatorException;
import domain.Purchase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.IOException;
import java.rmi.RemoteException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class PurchaseService  implements PurchaseServiceInterface {
    @Autowired
    private PurchaseDataBaseRepository repo;
    @Autowired
    private JdbcOperations operations;
    @Autowired
    private Sort sort;

    public PurchaseService() throws RemoteException{ }

    @Override
    public List<Purchase> getAllPurchases() throws SQLException, RemoteException {
        return repo.findAll();
    }

    @Override
    public Iterable<Purchase> getAllPurchases(String... args) throws SQLException, RemoteException {
        sort.setInfo(args);
        return repo.findAll(sort);
    }

    @Override
    public int removePurchase(Long id) throws SQLException, RemoteException {
        return repo.delete(id);
    }

    @Override
    public int addPurchase(Purchase entity) throws SQLException, ValidatorException, ParserConfigurationException, TransformerException, SAXException, IOException {
        return repo.save(entity);
    }

    @Override
    public List<Purchase> filter(Long clientID) throws SQLException, RemoteException {
        String cmd = "select * from Purchases where id = ?";
        return operations.query(cmd, new Object[]{clientID}, (results, row) -> {
            Long id = results.getLong("id");
            Long idClient = results.getLong("clientid");
            Long idBook = results.getLong("bookid");
            int nrBooks = results.getInt("nrbooks");
            Purchase purchase = new Purchase(idClient, idBook, nrBooks);
            purchase.setId(id);
            return purchase;
        });
    }

    @Override
    public int updatePurchase(Purchase entity) throws SQLException, ValidatorException, RemoteException {
        return repo.update(entity);
    }

    @Override
    public Optional<Purchase> findOnePurchase(Long id) throws SQLException, RemoteException {
        return repo.findOne(id);
    }


}
