import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;

public class WeatherApp {

    // Replace with your OpenWeatherMap API Key
    private static final String API_KEY = "cd2f9387137233cfb3cf1920460bdb6b";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            String city = JOptionPane.showInputDialog("Enter City Name:");
            if (city != null && !city.trim().isEmpty()) {
                String weatherData = getWeatherData(city.trim());
                createAndShowGUI(city, weatherData);
            }
        });
    }

    private static String getWeatherData(String city) {
        try {
            String apiUrl = "https://api.openweathermap.org/data/2.5/weather?q="
                    + city + "&appid=" + API_KEY + "&units=metric";

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

            reader.close();

            // Parse JSON response
            JSONObject obj = new JSONObject(response.toString());
            String description = obj.getJSONArray("weather")
                    .getJSONObject(0).getString("description");
            double temp = obj.getJSONObject("main").getDouble("temp");
            int humidity = obj.getJSONObject("main").getInt("humidity");

            return String.format("<html>Temperature: %.1f°C<br/>"
                    + "Description: %s<br/>Humidity: %d%%</html>", temp, description, humidity);

        } catch (Exception e) {
            return "Error fetching weather data.";
        }
    }

    private static void createAndShowGUI(String city, String weatherData) {
        JFrame frame = new JFrame("Weather in " + city);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200);

        JLabel weatherLabel = new JLabel(weatherData, SwingConstants.CENTER);
        weatherLabel.setFont(new Font("Arial", Font.PLAIN, 16));
        frame.getContentPane().add(weatherLabel);

        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
