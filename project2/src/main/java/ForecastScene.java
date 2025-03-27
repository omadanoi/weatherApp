import javafx.animation.ScaleTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;
import weather.Period;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class ForecastScene {
    // The scene for displaying the 3-day weather forecast.
    private Scene scene;
    // All day/night pairs from the forecast
    private ArrayList<Period[]> dayNightPairs = new ArrayList<>();

    private static final int pageSize = 3; // Number of days per page
    private int currentPage = 0;           // Current page index, base = 0
    private int highlightedIndex = 0;      //  Index of highlighted item on current page

    private HBox forecastContainer;        // Container for day boxes on current page
    private VBox[] dayBoxes = new VBox[pageSize]; // Holds the boxes currently displayed

    // Groups forecast data into day?night pairs
    public ForecastScene(ArrayList<Period> forecastData) {
        groupByName(forecastData);
        createScene();
    }

    public Scene getScene() {
        return scene;
    }

    
    // Groups forecast periods into day/night pairs.
    // For example, "Wednesday" paired with "Wednesday Night".
    // Leftover day or night gets paired with null.
    private void groupByName(ArrayList<Period> allData) {
        Period dayPeriod = null;
        String dayName = null;
        for (Period p : allData) {
            String lowerName = p.name.toLowerCase();
            if (lowerName.contains("night") || lowerName.contains("overnight")) {
                // If a matching day period exists, pair it with this night period.
                if (dayPeriod != null && dayName != null && lowerName.contains(dayName.toLowerCase())) {
                    dayNightPairs.add(new Period[]{dayPeriod, p});
                    dayPeriod = null;
                    dayName = null;
                } else {
                    // Otherwise add the night period with a null day.
                    dayNightPairs.add(new Period[]{null, p});
                }
            } else {
                // Store day period for potential pairing with a night period.
                dayPeriod = p;
                dayName = p.name;
            }
        }
        // If there's an unmatched day period, add it with null for the night.
        if (dayPeriod != null) {
            dayNightPairs.add(new Period[]{dayPeriod, null});
        }
    }

    // Creates and sets up the forecast scene layout.
    private void createScene() {
        VBox root = new VBox(30);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);
        // Set the background color 
        root.setStyle("-fx-background-color: #29ABE2;");

        // Create the header label for the forecast scene.
        Label header = new Label("3-Day Weather Forecast");
        header.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: black;");
        VBox.setMargin(header, new Insets(0, 0, 40, 0)); // 40px space below header

        // Container for the 3 day boxes
        forecastContainer = new HBox(60);
        forecastContainer.setAlignment(Pos.CENTER);
        // Load the first page with default highlight index 0
        updatePage(0);

        // Left arrow
        ImageView leftArrow = new ImageView(new Image(
                getClass().getResource("/icons/Arrow left-circle.png").toExternalForm()));
        leftArrow.setFitWidth(40);
        leftArrow.setPreserveRatio(true);
        // Event handler for left arrow click.
        leftArrow.setOnMouseClicked(this::highlightPreviousDay);

        // Right arrow
        ImageView rightArrow = new ImageView(new Image(
                getClass().getResource("/icons/Arrow right-circle.png").toExternalForm()));
        rightArrow.setFitWidth(40);
        rightArrow.setPreserveRatio(true);
        // Event handler for right arrow click.
        rightArrow.setOnMouseClicked(this::highlightNextDay);

        // Place arrows on the sides of the forecast container
        HBox mainRow = new HBox(40, leftArrow, forecastContainer, rightArrow);
        mainRow.setAlignment(Pos.CENTER);

        // Back button with extra top margin so it's pushed lower
        Button backBtn = new Button("← Back");
        backBtn.setStyle("-fx-background-color: black; -fx-text-fill: white;");
        backBtn.setOnAction(e -> SceneManager.getInstance().showMainWeatherScene());
        VBox.setMargin(backBtn, new Insets(20, 0, 0, 0)); // 40px above the button

        // Add header, mainRow, and backBtn to the root container
        root.getChildren().addAll(header, mainRow, backBtn);
        scene = new Scene(root, 800, 600);
    }

    
    // Updates the forecastContainer to show the current page of day boxes.
    // defaultHighlight the index (0-based) to highlight by default on the new page.
    private void updatePage(int defaultHighlight) {
        // Clear the current forecast boxes from the container.
        forecastContainer.getChildren().clear();
        int startIndex = currentPage * pageSize;
        int count = 0;
        // Loop through and add forecast boxes for the current page.
        for (int i = 0; i < pageSize; i++) {
            int globalIndex = startIndex + i;
            if (globalIndex < dayNightPairs.size()) {
                Period[] pair = dayNightPairs.get(globalIndex);
                // Create a forecast box for the day/night pair.
                VBox box = createDayNightBox(pair[0], pair[1]);
                dayBoxes[i] = box;
                forecastContainer.getChildren().add(box);
                count++;
            } else {
                dayBoxes[i] = null;
            }
        }
        highlightedIndex = defaultHighlight;
        if (count > 0) {
            // Highlight the default forecast box if exists.
            highlightDayBox(highlightedIndex);
        }
    }

    
    // Creates a vertical box for a single day, containing the date at the top,
    // then the day forecast on top and night forecast below.
    private VBox createDayNightBox(Period dayPeriod, Period nightPeriod) {
        VBox container = new VBox(10);
        container.setAlignment(Pos.CENTER);
        container.setStyle("-fx-background-color: #29ABE2; -fx-padding: 20; -fx-border-radius: 10;");
        container.setPrefWidth(180);

        // Create date label from the day's startTime or night if day is null
        String dateStr = "";
        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy");
        if (dayPeriod != null && dayPeriod.startTime != null) {
            dateStr = sdf.format(dayPeriod.startTime);
        } else if (nightPeriod != null && nightPeriod.startTime != null) {
            dateStr = sdf.format(nightPeriod.startTime);
        }
        Label dateLabel = new Label(dateStr);
        dateLabel.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: black;");
        container.getChildren().add(dateLabel);

        // Add day forecast sub-box if available
        if (dayPeriod != null) {
            VBox dayBox = buildSingleForecastBox(dayPeriod, "Daytime");
            container.getChildren().add(dayBox);
        }
        // Add night forecast sub-box if available
        if (nightPeriod != null) {
            VBox nightBox = buildSingleForecastBox(nightPeriod, "Night");
            VBox.setMargin(nightBox, new Insets(25, 0, 0, 0));
            container.getChildren().add(nightBox);
        }
        return container;
    }

    
    //Builds a small forecast box for either day or night.
    private VBox buildSingleForecastBox(Period period, String labelText) {
        VBox vBox = new VBox(5);
        vBox.setAlignment(Pos.CENTER);

        // Create a label for the forecast period name.
        Label label = new Label(period.name);
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: black;");

        // Create and configure the weather icon.
        ImageView weatherIcon = AnimatedWeatherIcons.getIcon(period.shortForecast);
        weatherIcon.setFitWidth(50);
        weatherIcon.setPreserveRatio(true);

        // Create a label for the temperature.
        Label tempLabel = new Label(period.temperature + "°" + period.temperatureUnit);
        tempLabel.setStyle("-fx-font-size: 16px; -fx-text-fill: black;");

        // Create a label for the short forecast description.
        Label shortForecastLabel = new Label(period.shortForecast);
        shortForecastLabel.setStyle("-fx-font-size: 14px; -fx-text-fill: black;");

        // Create and configure the precipitation icon.
        ImageView precipitationIcon = new ImageView(new Image(
                getClass().getResource("/icons/Humidity.png").toExternalForm()));
        precipitationIcon.setFitWidth(20);
        precipitationIcon.setPreserveRatio(true);

        // Create a label for the precipitation percentage.
        Integer popValue = period.probabilityOfPrecipitation.value;
        String popText = (popValue == null) ? "—" : popValue + "%";
        Label precipitationLabel = new Label(popText);
        precipitationLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: black;");

        // Group the precipitation icon and label in an HBox.
        HBox precipitationBox = new HBox(5, precipitationIcon, precipitationLabel);
        precipitationBox.setAlignment(Pos.CENTER);

        // Add all forecast details to the VBox.
        vBox.getChildren().addAll(label, weatherIcon, tempLabel, shortForecastLabel, precipitationBox);
        return vBox;
    }

    // Handles the left arrow click.
    // If not at the beginning of the page, moves highlight left.
    // Otherwise, if a previous page exists, loads it and highlights the last item.
    private void highlightPreviousDay(MouseEvent event) {
        if (highlightedIndex > 0) {
            unhighlightDayBox(highlightedIndex);
            highlightedIndex--;
            highlightDayBox(highlightedIndex);
        } else if (currentPage > 0) {
            currentPage--;
            int startIndex = currentPage * pageSize;
            int pageItemCount = Math.min(pageSize, dayNightPairs.size() - startIndex);
            updatePage(pageItemCount - 1);
        }
    }

    // Handles the right arrow click.
    // If not at the end of the current page, moves highlight right.
    // Otherwise, if a next page exists, loads it and highlights the first item.
    private void highlightNextDay(MouseEvent event) {
        int startIndex = currentPage * pageSize;
        int pageItemCount = Math.min(pageSize, dayNightPairs.size() - startIndex);
        if (highlightedIndex < pageItemCount - 1) {
            unhighlightDayBox(highlightedIndex);
            highlightedIndex++;
            highlightDayBox(highlightedIndex);
        } else {
            if ((currentPage + 1) * pageSize < dayNightPairs.size()) {
                currentPage++;
                updatePage(0);
            }
        }
    }
    // Applies a scale transition to visually highlight a forecast box.
    private void highlightDayBox(int index) {
        VBox box = dayBoxes[index];
        if (box == null) return;
        ScaleTransition st = new ScaleTransition(Duration.millis(300), box);
        st.setToX(1.2);
        st.setToY(1.2);
        st.play();
    }

    // Removes the highlight from a forecast box by scaling it back to its original size.
    private void unhighlightDayBox(int index) {
        VBox box = dayBoxes[index];
        if (box == null) return;
        ScaleTransition st = new ScaleTransition(Duration.millis(300), box);
        st.setToX(1.0);
        st.setToY(1.0);
        st.play();
    }
}
