import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.*;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

public class Basket implements Serializable{
    @Expose
    @SerializedName("price")
    private int[] prices;
    @Expose
    @SerializedName("name")
    private String[] productsName;
    @Expose
    @SerializedName("count")
    private int[] productsCount;
    @Expose
    @SerializedName("total")
    private int sum;


    public Basket(int[] prices, String[] productsName) {
        this.prices = prices;
        this.productsName = productsName;
        this.productsCount = new int[prices.length];
    }

    private Basket() {
    }

    public void addToCart(int productNum, int amount) {
        productsCount[productNum] += amount;
    }

    public void setProductsName(String[] productsName) {
        this.productsName = productsName;
    }

    public void setPrices(int[] prices) {
       this.prices = prices;
    }

    public void setSum(int sum) {
        this.sum = sum;
    }
    public void setCount(int[] productsCount) {
        for (int i = 0; i < this.productsCount.length; i++) {
            this.productsCount[i]=productsCount[i];
       }
    }

    public String[] getProductsName() {
        return productsName;
    }

    public int[] getProductsCount() {
        return productsCount;
    }

    public int[] getPrices() {
        return prices;
    }

    public int getSum() {
        return this.sum;
    }

    protected void printCart() {
        System.out.println("Basket:");
        for (int i = 0; i < productsCount.length; i++) {
            int allCount= this.productsCount[i];
            int priceSum = prices[i] * allCount;
            if (allCount > 0) {
                sum = priceSum;
                System.out.println(productsName[i] + " " + allCount + " " + priceSum);
            }
        }
        System.out.println("Total: " + sum + " rub.");
    }

    public void saveTxt(File textFile) {
        try (PrintWriter writer = new PrintWriter(textFile)) {
            for (int i = 0; i < productsName.length; i++) {
                writer.println(productsName[i] + " " + prices[i] + " " + productsCount[i]);
            }
        } catch (IOException e) {
            throw new RuntimeException("Error!");
        }
    }

    public void saveJson() throws RuntimeException, IOException, CloneNotSupportedException {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        try (FileWriter writer = new FileWriter("basket.json")) {
            gson.toJson(this, writer);
        }
    }

    public void save() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new FileWriter("basket.json"), this);
    }

    public static Basket load() throws NullPointerException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        //mapper.readValue(new File("basket.json"), Basket.class);
        return mapper.readValue(new FileReader("basket.json"), Basket.class);
    }

    public static Basket loadJson() throws IOException,ParseException {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();


            FileReader reader = new FileReader("basket.json");
        Basket basket;
        basket = gson.fromJson(reader,Basket.class);
        basket.setCount(basket.productsCount);
            basket.setSum(basket.sum);
            //setProductsName(basket.productsName);
            //setPrices(basket1.prices);
            System.out.println(basket);

            return (Basket) basket;
    }

    public static Basket loadTxtFile(File textFile) throws IOException {
        Basket basket = new Basket();
        Path path = textFile.toPath();
        List<String> basketList = Files.readAllLines(path);


        String[] productsNames = new String[basketList.size()];
        int[] prices = new int[basketList.size()];
        int[] productsCount = new int[basketList.size()];

        for (int i = 0; i <= basketList.size() - 1; i++) {
            String[] data = basketList.get(i).split(" ");
            productsNames[i] = data[0];
            prices[i] = Integer.parseInt(data[1]);
            productsCount[i] = Integer.parseInt(data[2]);
            basket.setCount(basket.productsCount);
        }

        System.out.print("Basket return!:");
        return basket;
    }

    @Override
    public String toString() {
        return "Name:" + (Arrays.deepToString((productsName)) +
                "Count:" + (Arrays.toString(productsCount)) +
                "Prices:" + (Arrays.toString(prices)) +
                "Total:" + sum);
    }

    protected void printForBuy() {
        System.out.println("List of available products: ");
        for (int i = 0; i < productsName.length; i++) {
            System.out.println((i + 1) + ": " + productsName[i] + ": " + prices[i] + " - " + productsCount[i]);
        }
    }
}