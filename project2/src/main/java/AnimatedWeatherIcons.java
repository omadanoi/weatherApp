import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AnimatedWeatherIcons {

    // Returns an ImageView containing the appropriate weather icon based on the short description.
    public static ImageView getIcon(String shortForecast) {
        // Default placeholder if no match is found
        String iconPath = "/icons/Sunny.png"; // Default to sunny if no match

        // Determine the correct icon based on the forecast
        String lowerCaseForecast = shortForecast.toLowerCase();

        // Determine the correct icon based on the content of the forecast.
        if (lowerCaseForecast.contains("rain") && lowerCaseForecast.contains("sun")) {
            iconPath = "/icons/Rain&Sun.png";
        } else if (lowerCaseForecast.contains("rain") && lowerCaseForecast.contains("night")) {
            iconPath = "/icons/Rain-night.png";
        } else if (lowerCaseForecast.contains("rain")) {
            iconPath = "/icons/Rain.png";
        } else if (lowerCaseForecast.contains("thunderstorm")) {
            iconPath = "/icons/Rain&Thunderstorm.png";
        } else if (lowerCaseForecast.contains("scattered showers")) {
            iconPath = "/icons/Scattered-showers.png";
        } else if (lowerCaseForecast.contains("severe thunderstorm")) {
            iconPath = "/icons/Severe-thunderstorm.png";
        } else if (lowerCaseForecast.contains("cloud") && lowerCaseForecast.contains("night")) {
            iconPath = "/icons/Cloudy-clear at times-night.png";
        } else if (lowerCaseForecast.contains("cloud")) {
            iconPath = "/icons/Cloudy.png";
        } else if (lowerCaseForecast.contains("partly cloudy") && lowerCaseForecast.contains("night")) {
            iconPath = "/icons/Partly-cloudy-night.png";
        } else if (lowerCaseForecast.contains("partly cloudy")) {
            iconPath = "/icons/Partly-cloudy.png";
        } else if (lowerCaseForecast.contains("drizzle") && lowerCaseForecast.contains("night")) {
            iconPath = "/icons/Drizzle-night.png";
        } else if (lowerCaseForecast.contains("drizzle") && lowerCaseForecast.contains("sun")) {
            iconPath = "/icons/Drizzle&Sun.png";
        } else if (lowerCaseForecast.contains("drizzle")) {
            iconPath = "/icons/Drizzle.png";
        } else if (lowerCaseForecast.contains("snow")) {
            iconPath = "/icons/Snow.png";
        } else if (lowerCaseForecast.contains("sleet")) {
            iconPath = "/icons/Sleet.png";
        } else if (lowerCaseForecast.contains("fog")) {
            iconPath = "/icons/Fog.png";
        } else if (lowerCaseForecast.contains("hail")) {
            iconPath = "/icons/Hail.png";
        } else if (lowerCaseForecast.contains("blizzard")) {
            iconPath = "/icons/Blizzard.png";
        } else if (lowerCaseForecast.contains("heavy rain")) {
            iconPath = "/icons/Heavy-rain.png";
        } else if (lowerCaseForecast.contains("clear") ) {
            iconPath = "/icons/Clear-night.png";
        } else if (lowerCaseForecast.contains("sunny")) {
            iconPath = "/icons/Sunny.png";
        } else if (lowerCaseForecast.contains("wind")) {
            iconPath = "/icons/Wind.png";
        }

        // Load the icon using getResource()
        Image image = new Image(AnimatedWeatherIcons.class.getResource(iconPath).toExternalForm());
        // Create an ImageView to display the loaded image.
        ImageView iconView = new ImageView(image);
        iconView.setFitWidth(50);
        iconView.setPreserveRatio(true);

        return iconView;
    }

}
