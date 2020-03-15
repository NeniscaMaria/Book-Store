package repository;

import domain.Client;
import domain.Purchase;
import domain.validators.Validator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.IntStream;

public class PurchaseXMLRepository extends InMemoryRepository<Long, Purchase> {
    private String filename;

    public PurchaseXMLRepository(Validator<Purchase> validator, String filename) throws IOException, SAXException, ParserConfigurationException {
        super(validator);
        this.filename = filename;
        loadData();
    }

    private void saveAllToFile(Document document){
        try {
            Transformer transformer = TransformerFactory
                    .newInstance()
                    .newTransformer();
            transformer.transform(new DOMSource(document),
                    new StreamResult(new File(filename)));
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
    private Purchase createPurchaseFromElement(Element purchaseElement){
        Node IDNode = purchaseElement.getElementsByTagName("ID").item(0);
        Long ID = Long.parseLong(IDNode.getTextContent());

        Node serialNoNode = purchaseElement.getElementsByTagName("SerialNo").item(0);
        String serialNo = serialNoNode.getTextContent();

        Node nameNode = purchaseElement.getElementsByTagName("Name").item(0);
        String name = nameNode.getTextContent();

        Purchase purchase = new Purchase(serialNo,name);
        purchase.setId(ID);
        return purchase;
    }
    private void loadData() throws ParserConfigurationException, IOException, SAXException {
        File file = new File(filename);
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(file);

        document.getDocumentElement().normalize();
        Element root = document.getDocumentElement();

        NodeList children = document.getElementsByTagName("client");
        IntStream.range(0, children.getLength())
                .mapToObj(children::item)
                .filter(node -> node.getNodeType()==Node.ELEMENT_NODE)
                .forEach(node ->{ super.save(createPurchaseFromElement((Element) node));});
    }

    private Node purchaseToNode(Purchase purchase, Document document){
        Element purchaseElement = document.createElement("client");
        Element IDElement = document.createElement("ID");
        IDElement.setTextContent(purchase.getId().toString());
        purchaseElement.appendChild(IDElement);

        Element serialNoElement = document.createElement("SerialNo");
        serialNoElement.setTextContent(purchase.getSerialNumber());
        purchaseElement.appendChild(serialNoElement);

        Element nameElement = document.createElement("Name");
        nameElement.setTextContent(purchase.getName());
        purchaseElement.appendChild(nameElement);

        return purchaseElement;
    }

    @Override
    public Optional<Purchase> save(domain.Purchase entity){

        Optional<Purchase> optional;
        try {
            optional = super.save(entity);
            if (optional.isPresent()) {
                return optional;
            }
            saveToFile(entity);
        } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    private void saveToFile(domain.Purchase entity) throws ParserConfigurationException, IOException, SAXException {
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(filename);

        Element root = document.getDocumentElement();
        Node clientNode = purchaseToNode(entity, document);
        root.appendChild(clientNode);
        saveAllToFile(document);
    }

    public Optional<Purchase> update(Purchase purchase){
        Optional<Purchase> res = super.update(purchase);
        res.ifPresent(r-> {
            try {
                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filename);
                Element root = document.getDocumentElement();
                NodeList children = root.getChildNodes();

                IntStream.range(0, children.getLength())
                        .mapToObj(children::item)
                        .filter(node -> node instanceof Element)
                        .filter(node-> createPurchaseFromElement((Element) node).getId().equals(purchase.getId()))
                        .forEach(node->{
                            Node parent = node.getParentNode();
                            Node newNode = purchaseToNode(purchase,document);
                            parent.replaceChild(newNode,node);
                            Transformer transformer= null;
                            saveAllToFile(document);
                        });
            } catch (SAXException | IOException | ParserConfigurationException e) {
                e.printStackTrace();
            }});
        return res;
    }


    public Optional<Purchase> delete(Long ID){
        Optional<Purchase> res = super.delete(ID);
        res.ifPresent(r->{
            try {
                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(filename);
                Element root = document.getDocumentElement();
                NodeList children = root.getChildNodes();

                IntStream.range(0, children.getLength())
                        .mapToObj(children::item)
                        .filter(node -> node instanceof Element)
                        .filter(node-> createPurchaseFromElement((Element)node).getId()==ID)
                        .forEach(node->{
                            Node parent = node.getParentNode();
                            parent.removeChild(node);
                            Transformer transformer= null;
                            saveAllToFile(document);});
            } catch (SAXException | IOException | ParserConfigurationException e) {
                e.printStackTrace();
            }});
        return res;
    }
}
