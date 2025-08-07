package services;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import javafx.scene.control.Alert;

public abstract class ApiService {
    
    protected static final String BASE_URL = "http://localhost:8080";
    protected final HttpClient httpClient = HttpClient.newHttpClient();
    protected final ObjectMapper objectMapper = new ObjectMapper();

    public ApiService() {
        objectMapper.findAndRegisterModules();
    }

    /**
     * Sends a GET request to the specified URL.
     * @param url The URL to send the request to.
     * @return The HttpResponse containing the response body.
     * @throws IOException
     * @throws InterruptedException
     */
    protected HttpResponse<String> sendGetRequest(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Sends a POST request to the specified URL with a JSON body.
     * @param url The URL to send the request to.
     * @param jsonBody The JSON body as a String.
     * @return The HttpResponse containing the response body.
     * @throws IOException
     * @throws InterruptedException
     */
    protected HttpResponse<String> sendPostRequest(String url, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Sends a PUT request to the specified URL with a JSON body.
     * @param url The URL to send the request to.
     * @param jsonBody The JSON body as a String.
     * @return The HttpResponse containing the response body.
     * @throws IOException
     * @throws InterruptedException
     */
    protected HttpResponse<String> sendPutRequest(String url, String jsonBody) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        return httpClient.send(request, HttpResponse.BodyHandlers.ofString());
    }

    /**
     * Handles non-200 HTTP responses by showing an alert.
     * @param response The HttpResponse to handle.
     */
    protected void handleErrorResponse(HttpResponse<String> response) {
        String message = String.format("API call failed with status code %d. Response: %s",
                response.statusCode(), response.body());
        System.err.println(message);
        
        // Use Platform.runLater to ensure UI updates are on the JavaFX application thread
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("API Error");
            alert.setHeaderText(null);
            alert.setContentText("An error occurred with the API: " + message);
            alert.showAndWait();
        });
    }
}
