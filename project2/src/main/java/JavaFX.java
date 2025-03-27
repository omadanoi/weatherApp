import javafx.application.Application;
import javafx.stage.Stage;
import weather.Period;
import weather.WeatherAPI;
import java.util.ArrayList;

public class JavaFX extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        ArrayList<Period> initialForecast = WeatherAPI.getForecast("LOT", 77, 70);
        SceneManager.getInstance().init(primaryStage, initialForecast);
        SceneManager.getInstance().showMainWeatherScene();
    }
}
