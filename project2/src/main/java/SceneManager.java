import javafx.scene.Scene;
import javafx.stage.Stage;
import weather.Period;
import java.util.ArrayList;

public class SceneManager {

    // Ensures that only one instance of SceneManager exists.
    private static SceneManager instance = new SceneManager();
    
    // Access singleton
    public static SceneManager getInstance() { 
        return instance; 
    }

    private SceneManager() {}

    private Stage primaryStage;

    // Scene objects for the main weather display and forecast view.
    private Scene mainWeatherScene;
    private Scene forecastScene;

    // Holds the current forecast data used by the scenes.
    private ArrayList<Period> currentForecast;

    // Initializes the SceneManager with the primary stage and initial forecast data.
    public void init(Stage stage, ArrayList<Period> initialForecast) {
        // Stores reference
        this.primaryStage = stage;
        // Set the current forecast
        this.currentForecast = initialForecast;

        // Create the main weather scene and forecast scene using the current forecast data.
        mainWeatherScene = new MainWeatherScene(currentForecast).getScene();
        forecastScene = new ForecastScene(currentForecast).getScene();

        primaryStage.setTitle("Weather App");
        primaryStage.show();
    }

    // Switches the primary stage to display the main weather scene
    public void showMainWeatherScene() {
        primaryStage.setScene(mainWeatherScene);
    }

    // Switches the primary stage to display the forecast scene.
    public void showForecastScene() {
        primaryStage.setScene(forecastScene);
    }
    
    // Updates the forecast data
    public void updateForecastData(ArrayList<Period> newForecast) {
        this.currentForecast = newForecast;
        // Recreate the scenes with the new forecast data.
        mainWeatherScene = new MainWeatherScene(newForecast).getScene();
        forecastScene = new ForecastScene(newForecast).getScene();
    }
}
