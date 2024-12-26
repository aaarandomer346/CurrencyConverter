package currencyconverter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Objects;
import java.util.Scanner;

public class App
{
    // variables used for creating a new response every 30 mins
    static long currTimeMilli = System.currentTimeMillis();
    static long currTimeSec = currTimeMilli/1000;
    static long prevTimeSec = 0;

    public static void main(String[] args) throws IOException, ParseException {

        prevTimeSec = loadTime(prevTimeSec); // loads the last time of response from time.dat

        if (prevTimeSec + 1800 <= currTimeSec) { // if the last time of response was more than or 30 mins ago request new response
            new RequestNewJSON();
            saveTime(currTimeSec); // saves the current time
        }
        Scanner scanner = new Scanner(System.in); // creates scanner instance

        // asks what currency the person wants
        System.out.println("What currency do you have?");
        String A = scanner.nextLine();
        A = A.toUpperCase();
        System.out.println("How much of that currency do you have?");
        String AAmountStr = scanner.nextLine();
        int AAmount = Integer.parseInt(AAmountStr);
        System.out.println("What is the currency you want to convert to?");
        String B = scanner.nextLine();
        B = B.toUpperCase();

        // gets and outputs the amount in the other currency
        double BAmount = Convert(A, B, AAmount);
        System.out.println("The Amount In " + B + " Is: " + BAmount);
    }

    public static double Convert(String AStr, String BStr, int AAmount) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("response.json");
        Object obj = jsonParser.parse(reader);
        JSONObject empjsonobj = (JSONObject) obj;
        JSONObject conversionRates = (JSONObject) empjsonobj.get("conversion_rates");

        if (Objects.equals(AStr, "USD")) { // if the first input is USD
            if (Objects.equals(BStr, "USD")) {
                return AAmount; // if, for some reason, someone is stupid enough to ask USD to USD this outputs how much of that currency they have
            }
            double BToUSD = (double) conversionRates.get(BStr); // gets the second currency exchange rate to usd
            return AAmount*BToUSD; // returns the amount in second currency
        } else if (Objects.equals(BStr, "USD")) { // if the second currency is usd
            double AToUSD = (double) conversionRates.get(AStr); // get the exchange rate of the first currency to usd from the json file
            return AAmount/AToUSD; // returns the amount in usd
        }
        double AToUSD = (double) conversionRates.get(AStr); // gets the conversion rate from currency 1 to usd
        double BToUSD = (double) conversionRates.get(BStr); // gets the conversion rate from currency 2 to usd
        return (AAmount/AToUSD)*BToUSD; // converts currency 1 to usd then usd to currency 2
    }

    public static void saveTime(long currTimeSec) {
        try {
            // Convert the integer to a String before writing
            BufferedWriter write = new BufferedWriter(new FileWriter("time.dat"));
            write.write(Long.toString(currTimeSec));  // Use Long.toString for long values
            write.close();  // Close the writer to ensure data is flushed to the file
        } catch (IOException e) {
            System.out.println("An Issue has occurred!");
            e.printStackTrace();  // Print the actual exception for debugging
        }
    }
    public static long loadTime(long prevTime) {
        try {
            BufferedReader read = new BufferedReader(new FileReader("time.dat"));
            prevTime = Long.parseLong(read.readLine());
            return prevTime;
        } catch (IOException e) {
            System.out.println("An Issue has occurred!");
            throw new RuntimeException(e);
        }
    }
}