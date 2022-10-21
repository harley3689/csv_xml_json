

import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static String enabled;
    private static String fileName;
    private static String format;
    protected static int[] prices = {100, 200, 300};
    protected static String[] products = {"Apples", "Bread", "Potatoes"};

    public static void main(String[] args) throws IOException, ParserConfigurationException, SAXException, ParseException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("shop.xml"));

        Scanner scanner = new Scanner(System.in);

        File csvFile = new File("log.csv");
        File textFile = new File("basket.txt");
        File jsonFile = new File("basket.json");
        ClientLog log = new ClientLog();
        Basket basket = new Basket(prices, products);


        node(doc, "load");
        if (enabled.equals("true")) {
            basket.printForBuy();
            if (jsonFile.exists()) {
                if (format.equals("json")) {
                    basket.load();
                } else {
                    Basket.loadTxtFile(textFile);
                }
            }
        }

        while (true) {
            System.out.println("Enter the product number and quantity, or 'end' to exit.");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("end")) {
                break;
            }
            String[] parts = input.split(" ");
            if (parts.length != 2) {
                continue;
            }

            int productNumber;
            try {
                productNumber = Integer.parseInt(parts[0]) - 1;
            } catch (NumberFormatException e) {
                System.out.println("Error!");
                continue;
            }

            int productCount;
            try {
                productCount = Integer.parseInt(parts[1]);
            } catch (NumberFormatException e) {
                System.out.println("Error!");
                continue;
            }
            if (productCount > 50 || productCount <= 0) {
                System.out.println("Error!");
                continue;
            }
            basket.addToCart(productNumber, productCount);
            log.log(productNumber + 1, productCount);
        }
        basket.printCart();

        Main.node(doc, "save");
        if (enabled.equals("true")) {
            jsonFile = new File(fileName + "." + format);
            if (format.equals("json")) {
                basket.save2();
            } else {
                basket.saveTxt(textFile);
            }
            log.exportCSV(csvFile);
        }
    }
    private static void node(Document doc, String name) {
        NodeList nodeList = doc.getElementsByTagName(name);
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node_ = nodeList.item(i);
            if (Node.ELEMENT_NODE == node_.getNodeType()) {
                Element element = (Element) node_;
                enabled = element.getElementsByTagName("enabled").item(0).getTextContent();
                fileName = element.getElementsByTagName("fileName").item(0).getTextContent();
                if (!name.equals("log")) {
                    format = element.getElementsByTagName("format").item(0).getTextContent();
                }
            }
        }
    }
}

