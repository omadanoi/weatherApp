import weather.Period;
import weather.WeatherAPI;
import java.util.ArrayList;

public class CustomLocationSearcher {

    // Fetches forecast data based on a custom location input.
    public static ArrayList<Period> fetchData(String userInput) {
        // Check if the input is null or empty; if so, return null.
        if (userInput == null || userInput.isEmpty()) return null;

        // Simple parser: "LOT 77 70"
        // Split the input by one or more whitespace.
        String[] parts = userInput.split("\\s+");

        // Ensures splits into 3 parts
        if (parts.length == 3) {
            // The first part is region
            String region = parts[0];
            try {
                // Parse the second and third parts as integers
                int gridx = Integer.parseInt(parts[1]);
                int gridy = Integer.parseInt(parts[2]);
                // Directly fetch from Weather API
                return WeatherAPI.getForecast(region, gridx, gridy);
            } catch (NumberFormatException ex) {
                // When parsing fails
                return null;
            }  
        }
        // If input does not contain 3 parts
        return null;
    }
}
