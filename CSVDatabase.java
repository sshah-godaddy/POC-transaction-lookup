import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVDatabase {
    // Stores a list of all the transaction records in a TransactionRecord object
    private final List<TransactionRecord> records = new ArrayList<>();
    private boolean connected = false;

    /**
     * Loads the CSV file into memory at startup.
     * Acts as the mock database for the POC.
     */
    public void connect(String filePath) throws IOException {
        try (FileReader reader = new FileReader(filePath);
             CSVParser parser = CSVFormat.DEFAULT
                .withFirstRecordAsHeader()
                .withIgnoreHeaderCase()
                .withTrim()
                .withQuoteMode(org.apache.commons.csv.QuoteMode.ALL_NON_NULL)
                .withIgnoreEmptyLines(true)
                .withAllowMissingColumnNames(true)
                .withEscape('\\')
                .parse(reader))
            {

            for (CSVRecord row : parser) {
                // Create a new TransactionRecord object, setting the specific fields
                // using setter() methods

                TransactionRecord record = new TransactionRecord();
                record.setOrderId(getSafe(row, "order_id"));
                record.setShopperId(getSafe(row, "shopper_id"));
                record.setTransactionCurrencyTotal(getSafe(row, "transactioncurrencytotal"));
                record.setTransactionCurrencyType(getSafe(row, "transactioncurrencytype"));
                record.setBillingCountryCode(getSafe(row, "billing_country_code"));
                record.setPaymentType(getSafe(row, "payment_type"));

                records.add(record); // add to final records list
            }

            connected = true;
            System.err.println("CSV database loaded: " + records.size() + " records.");

        } catch (IOException e) { // Throws an error if "loading" doesn't work properly
            connected = false;
            throw e;
        }
    }

    // Returns whether the CSV cpl_sample is connected or not
    public boolean isConnected() {
        return connected;
    }

    // Returns all the records in the CSV database
    public List<TransactionRecord> getAllRecords() {
        return records;
    }

    /**
     * Safely gets a value from a CSV row by column name.
     * Returns empty string if column doesn't exist.
     */
    private String getSafe(CSVRecord row, String columnName) {
        try {
            return row.get(columnName);
        } catch (IllegalArgumentException e) {
            return "";
        }
    }
}