import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import weather.Period;
import java.util.ArrayList;

public class MainWeatherScene {
    private Scene scene;
    private ArrayList<Period> forecastData;

    // Constructor initializes the forecast data and creates the scene.
    public MainWeatherScene(ArrayList<Period> forecastData) {
        this.forecastData = forecastData;
        createScene();
    }

    public Scene getScene() {
        return scene;
    }

    // Build UI scene.
    private void createScene() {
        Period today = forecastData.size() > 0 ? forecastData.get(0) : null;

        // Weather App Logo
        ImageView appLogo = new ImageView(new Image(getClass().getResource("/icons/weather-app.png").toExternalForm()));
        appLogo.setFitWidth(100);
        appLogo.setPreserveRatio(true);

        // Title Label.
        Label titleLabel = new Label("Current Weather");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: black;");

        // Search Bar
        // Text field for user input.
        TextField locationField = new TextField();
        locationField.setPromptText("Enter region, X and Y coordinates. Ex: LOT 77 70");
        locationField.setPrefWidth(250);
        locationField.setStyle(
            "-fx-background-color: white;"
        + "-fx-text-fill: black;"
        + "-fx-prompt-text-fill: #aaaaaa;"
        );

        // Search button styled with black background and white text.
        Button locationSearchBtn = new Button("Search");
        locationSearchBtn.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        locationSearchBtn.setOnAction(e -> {
            // Fetch data based on the user input.
            String userInput = locationField.getText();
            ArrayList<Period> newData = CustomLocationSearcher.fetchData(userInput);
            if (newData != null) {
                // Update the forecast data and switch back to the main scene.
                SceneManager.getInstance().updateForecastData(newData);
                SceneManager.getInstance().showMainWeatherScene();
            }
        });
        
        // Container Hbox for the seach bar and button.
        HBox searchBox = new HBox(10, locationField, locationSearchBtn);
        searchBox.setAlignment(Pos.CENTER);

        // Weather Info Labels.
        Label tempLabel = new Label();
        Label shortForecastLabel = new Label();
        Label windLabel = new Label();
        Label precipitationValueLabel = new Label();

        // Precipitation icon.
        ImageView precipitationIcon = new ImageView(new Image(getClass().getResource("/icons/humidity.png").toExternalForm()));
        precipitationIcon.setFitWidth(25);
        precipitationIcon.setPreserveRatio(true);

        // ImageView to hold the main weather icon.
        ImageView iconView = new ImageView();
        if (today != null) {
            // If weather data is available, use the labels.
            tempLabel.setText(today.temperature + "°" + today.temperatureUnit);
            shortForecastLabel.setText(today.shortForecast);
            windLabel.setText("Wind: " + today.windSpeed + " " + today.windDirection);
            precipitationValueLabel.setText(today.probabilityOfPrecipitation.value + "%");
            // Get the associated animated weather icon based on the short forecast.
            iconView = AnimatedWeatherIcons.getIcon(today.shortForecast);
        } else {
            // Text if data is not available.
            tempLabel.setText("No data");
            precipitationValueLabel.setText("N/A");
        }

        // Apply Styling to Labels.
        tempLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");
        shortForecastLabel.setStyle("-fx-font-size: 18px; -fx-text-fill: black;");
        windLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");
        precipitationValueLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

        // Container Hbox for precipitation icon.
        HBox precipitationBox = new HBox(5, precipitationIcon, precipitationValueLabel);
        precipitationBox.setAlignment(Pos.CENTER);

        // View Forecast Button
        Button showForecastBtn = new Button("View 3-Day Forecast");
        showForecastBtn.setStyle("-fx-background-color: black; -fx-text-fill: white; -fx-font-size: 14px;");
        showForecastBtn.setOnAction(e -> SceneManager.getInstance().showForecastScene());

        // Assemble All Elements in a VBox.
        VBox vbox = new VBox(
            8, 
            appLogo,
            searchBox,
            titleLabel,
            iconView,
            shortForecastLabel,
            tempLabel,
            windLabel,
            precipitationBox,
            showForecastBtn
        );
        vbox.setAlignment(Pos.CENTER);

        // Specific margins.
        VBox.setMargin(searchBox, new Insets(10, 0, 40, 0));
        VBox.setMargin(showForecastBtn, new Insets(10, 0, 0, 0));

        // Create the Root Layout.
        StackPane root = new StackPane(vbox);
        root.setStyle("-fx-background-color: #29ABE2;");

        // Create scene.
        scene = new Scene(root, 800, 600);
    }
}
