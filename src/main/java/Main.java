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

    public static void main(String[] args) throws Exception {
        int[] prices = {100, 200, 300};
        String[] productsName = {"Apples", "Bread", "Potatoes"};
        Basket basket = new Basket(productsName, prices);

        File csvFile = new File("log.csv");
        File textFile = new File("basket.txt");
        File jsonFile = new File("basket.json");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File("shop.xml"));

        Scanner scanner = new Scanner(System.in);
        ClientLog log = new ClientLog();


        node(doc, "load");
        if (enabled.equals("true")) {
            if (jsonFile.exists()) {
                if (format.equals("json")) {
                    basket = Basket.load(new File("basket.json"));
                    basket.printForBuy();
                } else {
                    basket = Basket.loadTxtFile(new File("basket.json"));
                    basket.printForBuy();
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
            int number = Integer.parseInt(parts[0]) - 1;
            int count = Integer.parseInt(parts[1]);
            basket.addToCart(number, count);
            log.log(number + 1, count);
        }
        basket.printCart();

        node(doc, "save");
        if (enabled.equals("true")) {
            textFile = new File(fileName + "." + format);
            if (format.equals("json")) {
                basket.save(new File("basket.json"));
            } else {
                basket.saveTxt(new File("basket.txt"));
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


