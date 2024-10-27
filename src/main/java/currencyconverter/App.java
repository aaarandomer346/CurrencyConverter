package currencyconverter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileReader;
import java.io.IOException;
import java.util.Objects;
import java.util.Scanner;

public class App
{
    public static void main( String[] args ) throws IOException, ParseException {

        Scanner scanner = new Scanner(System.in);

        System.out.println("What currency do you have?");
        String A = scanner.nextLine();
        A = A.toUpperCase();
        System.out.println("How much of that currency do you have?");
        String AAmountStr = scanner.nextLine();
        int AAmount = Integer.parseInt(AAmountStr);
        System.out.println("What is the currency you want to convert to?");
        String B = scanner.nextLine();
        B = B.toUpperCase();
        double BAmount = Convert(A, B, AAmount);
        System.out.println("The Amount In " + B + " Is: " + BAmount);
    }

    public static double Convert(String AStr, String BStr, int AAmount) throws IOException, ParseException {
        JSONParser jsonParser = new JSONParser();
        FileReader reader = new FileReader("jsonfiles/employe.json");
        Object obj = jsonParser.parse(reader);
        JSONObject empjsonobj = (JSONObject) obj;
        JSONObject conversionRates = (JSONObject) empjsonobj.get("conversion_rates");

        if (Objects.equals(AStr, "USD")) {
            if (Objects.equals(BStr, "USD")) {
                return AAmount;
            }
            double BToUSD = (double) conversionRates.get(BStr);
            return AAmount*BToUSD;
        } else if (Objects.equals(BStr, "USD")) {
            double AToUSD = (double) conversionRates.get(AStr);
            return AAmount/AToUSD;
        }
        double AToUSD = (double) conversionRates.get(AStr);
        double BToUSD = (double) conversionRates.get(BStr);
        return (AAmount/AToUSD)*BToUSD;
    }
}