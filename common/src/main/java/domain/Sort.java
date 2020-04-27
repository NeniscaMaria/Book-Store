package domain;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

@Component
public class Sort implements Serializable, Remote {
    @Autowired
    private JdbcOperations jdbcOperations;

    public enum Direction{
        ASC, DESC
    }


    public static Direction dir = Direction.ASC;
    private List<String> info;

    public Sort() throws RemoteException {
        info = new ArrayList<>();
    }

    public void setInfo(String... args){
        info = Arrays.asList(args.clone());
    }

    public Sort(String ...a) throws RemoteException {
        info = Arrays.asList(a.clone());
    }
//
//    public Sort(Direction dir, String ...a) throws RemoteException {
//        Sort.dir = dir;
//        info = Arrays.asList(a.clone());
//
//    }

    public List<Book> sortBook() throws RemoteException{
        String cmd = "select * from Books order by ";
        try {
            String cmd2 = info.stream().reduce((s1, s2) -> s1 + " " + Sort.dir + ", " + s2 + " " + Sort.dir).get();
            cmd = cmd + cmd2;
            System.out.println(cmd);
            return jdbcOperations.query(cmd, (results, row) -> {
                Long id = results.getLong("id");
                String sn = results.getString("serialNumber");
                String title = results.getString("title");
                String author = results.getString("author");
                int year = results.getInt("year");
                double price = results.getDouble("price");
                int stock = results.getInt("instock");
                Book b = new Book(sn, title, author, year, price, stock);
                b.setId(id);
                return b;
            });
        }
        catch (Exception e){
            throw new ValidatorException("Please enter at least one valid criteria");
        }
    }

    public Iterable<Client> sortClient() throws RemoteException{
        String cmd = "select * from Clients order by ";

        try {
            String cmd2 = info.stream().reduce((s1, s2) -> s1 + " " + Sort.dir + ", " + s2 + " " + Sort.dir).get();
            cmd = cmd + cmd2;
            System.out.println(cmd);
            return jdbcOperations.query(cmd, (resultSet, rowNum) -> {
                Long id = resultSet.getLong("id");
                String serialNumber = resultSet.getString("serialNumber");
                String name = resultSet.getString("name");
                Client client = new Client(serialNumber, name);
                client.setId(id);
                return client;
            });
        }
        catch (Exception e){
            throw new ValidatorException("Please enter at least one valid criteria");
        }
    }

    public Iterable<Purchase> sortPurchase() throws RemoteException{
        String cmd = "select * from Purchases order by ";
        try {
            String cmd2 = info.stream().reduce((s1, s2) -> s1 + " " + Sort.dir + ", " + s2 + " " + Sort.dir).get();
            cmd = cmd + cmd2;
            System.out.println(cmd);
            return jdbcOperations.query(cmd, (results, row) -> {
                Long id = results.getLong("id");
                Long idClient = results.getLong("clientid");
                Long idBook = results.getLong("bookid");
                int nrBooks = results.getInt("nrbooks");
                Purchase purchase = new Purchase(idClient, idBook, nrBooks);
                purchase.setId(id);
                return purchase;
            });
        }
        catch (Exception e){
            throw new ValidatorException("Please enter at least one valid criteria");
        }
    }

//    public Iterable<Book> sortBook() throws SQLException {
//        String s = "select * from Books order by ";
//        Optional<String> ee = info.stream().reduce((s1, s2) -> s1 + " " + Sort.dir + ", " + s2 + " " + Sort.dir);
//        s = s + ee.get() + ";";
//
//        List<Book> result = new ArrayList<>();
//        System.out.println(s);
//        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//        PreparedStatement preparedStatement = connection.prepareStatement(s);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        while (resultSet.next()) {
//            Long id = resultSet.getLong("id");
//            String serialNumber = resultSet.getString("serialNumber");
//            String title = resultSet.getString("title");
//            String author = resultSet.getString("author");
//            int year = resultSet.getInt("year");
//            double price = resultSet.getDouble("price");
//            int stock = resultSet.getInt("inStock");
//
//            Book b = new Book(serialNumber, title, author, year, price, stock);
//            b.setId(id);
//            result.add(b);
//
//            connection.close();
//
//        }
//
//        return result;
//
//
//     }
//     public Iterable<Client> sortClient() throws SQLException {
//         String cmd = "select * from Clients order by ";
//         Optional<String> ee = info.stream().reduce((s1, s2) -> s1 + ", " + s2 + " " + dir);
//         cmd = cmd + ee.get() + ";";
//         List<Client> result = new ArrayList<>();
//
//         Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//         PreparedStatement preparedStatement = connection.prepareStatement(cmd);
//         ResultSet resultSet = preparedStatement.executeQuery();
//         while (resultSet.next()) {
//             Long id = resultSet.getLong("id");
//             String serialNumber = resultSet.getString("serialNumber");
//             String name = resultSet.getString("name");
//
//             Client client = new Client(serialNumber, name);
//             client.setId(id);
//             result.add(client);
//         }
//         connection.close();
//         return result;
//     }
//
//    public Iterable<Purchase> sortPurchase() throws SQLException {
//        String cmd = "select * from Purchases order by ";
//        Optional<String> ee = info.stream().reduce((s1, s2) -> s1 + ", " + s2 + " " + dir);
//        cmd = cmd + ee.get() + ";";
//        List<Purchase> result = new ArrayList<>();
//
//        Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
//        PreparedStatement preparedStatement = connection.prepareStatement(cmd);
//        ResultSet resultSet = preparedStatement.executeQuery();
//        while (resultSet.next()) {
//            Long id = resultSet.getLong("id");
//            Long clientID = resultSet.getLong("clientID");
//            Long bookID = resultSet.getLong("bookID");
//            int nrBooks = resultSet.getInt("nrBooks");
//
//            Purchase p = new Purchase(clientID, bookID, nrBooks);
//            p.setId(id);
//            result.add(p);
//
//        }
//        connection.close();
//
//
//        return result;
//
//    }
//
//    public Sort and(Sort sort){
//        sort.info.stream().reduce((s1, s2) -> s1 + ", " + s2 + " " + dir);
//        return sort;
//    }

}

