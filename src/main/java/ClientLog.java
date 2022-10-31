import au.com.bytecode.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ClientLog {

    List<String[]> list = new ArrayList<>();

    String[] inform = "productNumber,amount".split(",");

    public void log(int productNumber, int amount) {
        list.add(new String[]{String.valueOf(productNumber), String.valueOf(amount)});
    }

    public void exportCSV(File csvFile) throws IOException {
        try (CSVWriter writ = new CSVWriter(new FileWriter(csvFile,false))) {
            writ.writeNext(inform);
            writ.writeAll(list);
        }
    }
}

