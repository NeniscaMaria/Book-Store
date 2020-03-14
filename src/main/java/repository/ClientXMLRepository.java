package repository;

import domain.Client;
import domain.validators.Validator;
import domain.validators.ValidatorException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

public class ClientXMLRepository extends InMemoryRepository<Long,Client> {
    private String fileName;

    public ClientXMLRepository(Validator<Client> validator, String fileName) {
        super(validator);
        this.fileName = fileName;
        try {
            loadData();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    private Client createClientFromElement(Element clientElement){
        Client client = new Client();
        Node IDNode = clientElement.getElementsByTagName("ID").item(0);
        Long ID = Long.parseLong(IDNode.getTextContent());
        client.setId(ID);

        Node serialNoNode = clientElement.getElementsByTagName("SerialNo").item(0);
        String serialNo = serialNoNode.getTextContent();
        client.setSerialNumber(serialNo);

        Node nameNode = clientElement.getElementsByTagName("Name").item(0);
        String name = nameNode.getTextContent();
        client.setName(name);

        System.out.println(client.toString());
        return client;
    }
    private void loadData() throws ParserConfigurationException, IOException, SAXException {
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(fileName);

        Element root = document.getDocumentElement();

        NodeList children = root.getChildNodes();

        IntStream.range(0, children.getLength())
                .mapToObj(index -> children.item(index))
                .filter(node -> node instanceof Element)
                .map(node -> {
                    try {
                        return super.save(createClientFromElement((Element) node));
                    } catch (ParserConfigurationException | IOException | SAXException | TransformerException e) {
                        e.printStackTrace();
                    }
                    return Optional.<Client>empty();
                });
    }

    private Node clientToNode(Client client, Document document){
        Element clientElement = document.createElement("client");
        Element IDElement = document.createElement("ID");
        IDElement.setTextContent(client.getId().toString());
        clientElement.appendChild(IDElement);

        Element serialNoElement = document.createElement("SerialNo");
        serialNoElement.setTextContent(client.getSerialNumber());
        clientElement.appendChild(serialNoElement);

        Element nameElement = document.createElement("Name");
        nameElement.setTextContent(client.getName());
        clientElement.appendChild(nameElement);

        return clientElement;
    }

    @Override
    public Optional<Client> save(domain.Client entity) throws ParserConfigurationException, TransformerException, SAXException, IOException {
        Optional<domain.Client> optional = super.save(entity);
        if (optional.isPresent()) {
            return optional;
        }
        saveToFile(entity);
        return Optional.empty();
    }

    private void saveToFile(domain.Client entity) throws ParserConfigurationException, TransformerException, IOException, SAXException {
        Document document = DocumentBuilderFactory
                .newInstance()
                .newDocumentBuilder()
                .parse(fileName);

        Element root = document.getDocumentElement();
        Node clientNode = clientToNode(entity, document);
        root.appendChild(clientNode);

        Transformer transformer= TransformerFactory
                .newInstance()
                .newTransformer();
        transformer.transform(new DOMSource(document),
                new StreamResult(new File(fileName)));
    }

    public Optional<Client> update(Client client){
        Optional<Client> res = super.update(client);
        res.ifPresent(r-> {
            try {
                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileName);
                Element root = document.getDocumentElement();
                NodeList children = root.getChildNodes();

                IntStream.range(0, children.getLength())
                        .mapToObj(index -> children.item(index))
                        .filter(node -> node instanceof Element)
                        .filter(node-> createClientFromElement((Element)node).getId()==client.getId())
                        .map(node->{
                            Node parent = node.getParentNode();
                            parent.removeChild(node);
                            Node newNode = clientToNode(client,document);
                            parent.appendChild(newNode);

                            Transformer transformer= null;
                            try {
                                transformer = TransformerFactory
                                        .newInstance()
                                        .newTransformer();
                                transformer.transform(new DOMSource(document),
                                        new StreamResult(new File(fileName)));
                            } catch (TransformerException e) {
                                e.printStackTrace();
                            }
                            return null;
                        });
            } catch (SAXException | IOException | ParserConfigurationException e) {
                e.printStackTrace();
            }});
        return res;
    }

    public Optional<Client> delete(Long ID){
        Optional<Client> res = super.delete(ID);
        res.ifPresent(r->{
            try {
                Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(fileName);
                Element root = document.getDocumentElement();
                NodeList children = root.getChildNodes();

                IntStream.range(0, children.getLength())
                        .mapToObj(index -> children.item(index))
                        .filter(node -> node instanceof Element)
                        .filter(node-> createClientFromElement((Element)node).getId()==ID)
                        .map(node->{
                            Node parent = node.getParentNode();
                            parent.removeChild(node);
                            Transformer transformer= null;
                            try {
                                transformer = TransformerFactory
                                        .newInstance()
                                        .newTransformer();
                                transformer.transform(new DOMSource(document),
                                        new StreamResult(new File(fileName)));
                            } catch (TransformerException e) {
                                e.printStackTrace();
                            }
                            return null;});
            } catch (SAXException | IOException | ParserConfigurationException e) {
                e.printStackTrace();
            }});
        return res;
    }
}
