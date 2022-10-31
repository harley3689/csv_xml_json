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

    public Basket(String[] productsName, int[] prices) {
        this.prices = prices;
        this.productsName = productsName;
        this.productsCount = new int[productsName.length];
    }
    private Basket() {
    }
    public void addToCart(int productNum, int amount) {
        productsCount[productNum] += amount;
    }
    public void setCount() {
        this.productsCount = productsCount;
    }

    protected void printCart() {
        int sum=0;
        System.out.println("Basket:");
        for (int i = 0; i < productsCount.length; i++) {
            int allCount= productsCount[i];
            int priceSum = prices[i] * allCount;
            if (allCount > 0) {
                sum += priceSum;
                System.out.println(productsName[i] + " " + allCount + " " + priceSum);
            }
        }
        System.out.println("Total: " + sum + " rub.");
    }

    public void saveTxt(File textFile) throws FileNotFoundException {
        try(PrintWriter writer = new PrintWriter(textFile);) {
            for (int i = 0; i < productsName.length; i++) {
                writer.println(productsName[i] + " " + prices[i] + " " + productsCount[i]);
            }
        }
    }


    public void save (File jsonFile) throws Exception {
        try(PrintWriter writer = new PrintWriter("basket.json")){
            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithoutExposeAnnotation();
            Gson gson = builder.create();
            String json = gson.toJson(this);
            writer.println(json);
        }
    }

    public static Basket load (File jsonFile) throws IOException {
        try(Scanner scan = new Scanner(jsonFile)){
            GsonBuilder builder = new GsonBuilder();
            builder.excludeFieldsWithoutExposeAnnotation();
            Gson gson = builder.create();
            String json = scan.nextLine();
            Basket basket = gson.fromJson (json,Basket.class);
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
            basket.setCount();
        }

        System.out.print("Basket return!:");
        return basket;
    }

    @Override
    public String toString() {
        return "Name:" + (Arrays.deepToString((productsName)) +
                "Count:" + (Arrays.toString(productsCount)) +
                "Prices:" + (Arrays.toString(prices)));
    }

    protected void printForBuy() {
        System.out.println("List of available products: ");
        for (int i = 0; i < productsName.length; i++) {
            System.out.println((i + 1) + ": " + productsName[i] + ": " + prices[i] + " - " + productsCount[i]);
        }
    }
}