import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


public class Basket implements Serializable{
    @Expose
    @SerializedName("name")
    private String[] productsName;
    @Expose
    @SerializedName("price")
    private int[] prices;
    @Expose
    @SerializedName("count")
    private int[] productsCount;
    @Expose
    @SerializedName("total")
    private int sum;


    public Basket(int[] prices, String[] productsName) {
        this.prices = prices;
        this.productsName = productsName;
        this.productsCount = new int[productsName.length];
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
        this.productsCount = productsCount;
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
                sum += priceSum;
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



    public void saveJson(File jsonFile) throws RuntimeException, IOException, CloneNotSupportedException {
        GsonBuilder builder = new GsonBuilder();
        builder.excludeFieldsWithoutExposeAnnotation();
        Gson gson = builder.create();
        try (FileWriter writer = new FileWriter(jsonFile)) {
            gson.toJson(this, writer);
        }
    }

    public void save (File jsonFile) throws FileNotFoundException {
        try(PrintWriter writer = new PrintWriter(jsonFile)){
            Gson gson = new Gson();
            String json = gson.toJson(this);
            writer.println(json);
        }
    }

    public static Basket load (File jsonFile) throws FileNotFoundException {
        try(Scanner scan = new Scanner(System.in)){
            Gson gson = new Gson();
            String str = scan.nextLine();
            Basket basket = gson.fromJson(str,Basket.class);
            return basket;
        }
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