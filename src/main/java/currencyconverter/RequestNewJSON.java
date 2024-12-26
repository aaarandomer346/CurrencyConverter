package currencyconverter;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class RequestNewJSON {
    RequestNewJSON() {
        try {
            String url_str = "https://v6.exchangerate-api.com/v6/USE_OWN_KEY/latest/USD";

            // Making Request
            URL url = new URL(url_str);
            HttpURLConnection request = (HttpURLConnection) url.openConnection();
            request.connect();

            // Convert to JSON
            JsonParser jp = new JsonParser();
            JsonElement root = jp.parse(new InputStreamReader((InputStream) request.getContent()));
            JsonObject jsonobj = root.getAsJsonObject();

            // Create and write the JSON to a file
            try (FileWriter file = new FileWriter("response.json")) {
                file.write(jsonobj.toString());  // Write the entire JSON content to the file
                System.out.println("JSON file created successfully.");
            } catch (IOException e) {
                e.printStackTrace();
            }

            // Optionally, print the result from JSON to verify
            String req_result = jsonobj.get("result").getAsString();
            System.out.println("Result: " + req_result);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
