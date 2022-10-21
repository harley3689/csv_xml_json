
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.bind.JsonTreeReader;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class Basket implements Serializable {
    protected int[] prices;
    protected String[] productsName;
    protected int[] productsCount;
    protected int sum;


    public Basket(int[] prices, String[] productsName) {
        this.prices = prices;
        this.productsName = productsName;
        this.productsCount = new int[prices.length];
        this.sum = sum;
    }
    public Basket(String[] productsName, int[] productsCount, int[] prices, int sum){
        this.productsName = productsName;
        this.productsCount = productsCount;
        this.prices = prices;
        this.sum = sum;
    }
    public Basket() {
    }

    public void setProductsName(String[] productsName) {
        this.productsName = productsName;
    }

    public void setPrices(int[] prices) {
        this.prices = prices;
    }

    public Integer setSum(int sum) {
        this.sum = sum;
        return this.sum;
    }

    public void setCount(int[] productsCount) {
        this.productsCount = new int[prices.length];
    }

    public void addToCart(int productNum, int amount) {
        productsCount[productNum] += amount;
    }


    protected void printCart() {
        System.out.println("Basket:");
        for (int i = 0; i < productsCount.length; i++) {
            int allCount = productsCount[i];
            int priceSum = prices[i] * allCount;
            if (allCount > 0) {
                sum += priceSum;
                setSum(sum);
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
     public void save()throws IOException{
        ObjectMapper mapper = new ObjectMapper();
        Map<String,Object> map= new HashMap<>();

        map.put("name",productsName);
        map.put("count",productsCount);
        map.put("price",prices);
        map.put("total",sum);
        String s = mapper.writeValueAsString(map);
        System.out.println(s);

        try(FileWriter file = new FileWriter("basket.json")){
            file.write(s);
        }
    }
    public void save2()throws IOException{
        Basket basket = new Basket();
        ObjectMapper mapper = new ObjectMapper();

        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        String s = mapper.writeValueAsString(basket);

        FileWriter file = new FileWriter("basket.json");
        file.write(s);
        file.close();
    }

    static Basket load()throws IOException,ParseException{
        ObjectMapper mapper = new ObjectMapper();
        Basket basket = (mapper.readValue(new File("basket.json"),Basket.class));

        return basket;
    }



    public void saveJson() {
        Gson gson = new GsonBuilder().setPrettyPrinting().enableComplexMapKeySerialization().create();
        try (FileWriter writ = new FileWriter("basket.json")) {
            Map<String, Object> map = new HashMap<>();
            map.put("Product", productsName);
            map.put("Price", prices);
            map.put("ProductCount", productsCount);
            map.put("Total", sum);
            gson.toJson(map, writ);
        } catch (IOException e) {
            throw new RuntimeException("Error!");
        }
    }

    public void loadJson()throws IOException {
        JSONParser parser = new JSONParser();
        try {
            Object obj = parser.parse(new FileReader("basket.json"));
            JSONObject js = (JSONObject) obj;
            System.out.println(js);
        } catch (IOException | ParseException e) {
            throw new RuntimeException("Error!");
        }
    }


    public static Basket loadTxtFile(File textFile) throws IOException {
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
        }

        Basket basket = new Basket(prices, productsNames);
        basket.setCount(productsCount);
        System.out.print("Basket return!:");
        return basket;
    }

    @Override
    public String toString() {
        return "Basket{" +
                "productsName " + Arrays.toString(productsName) +
                ",productsCount " + Arrays.toString(productsCount) +
                ",prices " + Arrays.toString(prices) +
                ",Total sum " + sum +
                '}';
    }
    protected void printForBuy() {
        System.out.println("List of available products: ");
        for (int i = 0; i < productsName.length; i++) {
            System.out.println((i + 1) + " " + productsName[i] + " " + prices[i] + " " + sum + " rub.");
        }
    }
}