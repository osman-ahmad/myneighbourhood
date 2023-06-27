package ibf2022.miniproject.myneighbourhood.services;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GoogleMapService {
    @Value("${GOOGLE_MAPS_API_KEY}")
    private String apiKey;

    public double[] getLatLng(String address) {
        String encodedAddress = encodeAddress(address);
        System.out.println("encodedAddress:>>>>>>>>> " + encodedAddress);
        String geocodingUrl = "https://maps.googleapis.com/maps/api/geocode/json?address=" + encodedAddress + "%2C+SG&key=" + apiKey;
        System.out.println("geocodingUrl:>>>>>>>>> " + geocodingUrl);
        String jsonResponse = getJsonResponse(geocodingUrl);
        
        double[] coordinates = JsonParser.parseJson(jsonResponse);
        
        return coordinates;
    }

    private String encodeAddress(String address) {
        try {
            return java.net.URLEncoder.encode(address, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getJsonResponse(String urlString) {
        StringBuilder response = new StringBuilder();

        try {
            URL url = new URL(urlString);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            connection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return response.toString();
    }

    private static class JsonParser {
        public static double[] parseJson(String json) {
            double[] coordinates = new double[2];
            
            JsonObject jsonObject = Json.createReader(new java.io.StringReader(json)).readObject();
            JsonArray results = jsonObject.getJsonArray("results");
            JsonObject firstResult = results.getJsonObject(0);
            JsonObject geometry = firstResult.getJsonObject("geometry");
            JsonObject location = geometry.getJsonObject("location");
            
            double lat = location.getJsonNumber("lat").doubleValue();
            double lng = location.getJsonNumber("lng").doubleValue();
            
            coordinates[0] = lat;
            coordinates[1] = lng;

            return coordinates;
        }
    }
}
