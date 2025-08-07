package util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A utility class for making HTTP requests.
 */
public class HttpClientUtil {

    private static final Logger LOGGER = Logger.getLogger(HttpClientUtil.class.getName());
    private static final HttpClient client = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(10))
            .build();
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.registerModule(new JavaTimeModule());
        // CRITICAL FIX: Configure the ObjectMapper to serialize LocalDate as a string
        mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }
    
    /**
     * Sends a simple HTTP GET request to the specified URL and returns the raw response body.
     * @param url The URL to send the request to.
     * @return The response body as a String.
     * @throws IOException if an I/O error occurs when sending or receiving.
     * @throws InterruptedException if the operation is interrupted.
     */
    public static String get(String url) throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();
        
        LOGGER.log(Level.INFO, "GET request to " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            LOGGER.log(Level.SEVERE, "GET error: " + response.statusCode() + ", body: " + response.body());
            throw new IOException("HTTP error: " + response.statusCode() + ", body: " + response.body());
        }

        return response.body();
    }
    
    /**
     * Sends an HTTP POST request to the specified URL with a JSON payload and returns the raw response body.
     * @param url The URL to send the request to.
     * @param jsonPayload The JSON string to send in the request body.
     * @return The response body as a String.
     * @throws Exception if the request fails or the status code is not 2xx.
     */
    public static String post(String url, String jsonPayload) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();
        
        LOGGER.log(Level.INFO, "POST request to " + url + ", body: " + jsonPayload);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        LOGGER.log(Level.INFO, "POST response: " + response.statusCode() + ", body: " + response.body());

        if (response.statusCode() >= 400) {
            throw new Exception("HTTP error: " + response.statusCode() + ", body: " + response.body());
        }

        return response.body();
    }

    /**
     * Generic GET method to retrieve an object from a URL.
     */
    public static <T> T get(String url, Class<T> responseType) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .GET()
                .build();
        LOGGER.log(Level.INFO, "GET request to " + url);
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        LOGGER.log(Level.INFO, "GET response: " + response.statusCode() + ", body: " + response.body());
        if (response.statusCode() != 200) {
            throw new Exception("HTTP error: " + response.statusCode() + ", body: " + response.body());
        }
        return mapper.readValue(response.body(), responseType);
    }

    /**
     * Generic POST method to send an object to a URL and receive an object response.
     * This method automatically serializes the object to JSON.
     */
    public static <T> T post(String url, Object body, Class<T> responseType) throws Exception {
        String jsonBody = mapper.writeValueAsString(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        LOGGER.log(Level.INFO, "POST request to " + url + ", body: " + jsonBody);
        LOGGER.log(Level.INFO, "POST headers: " + request.headers());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        LOGGER.log(Level.INFO, "POST response: " + response.statusCode() + ", body: " + response.body());
        if (response.statusCode() >= 400) {
            throw new Exception("HTTP error: " + response.statusCode() + ", body: " + response.body());
        }
        return mapper.readValue(response.body(), responseType);
    }

    /**
     * New generic POST method that takes a pre-formatted JSON string.
     * This is useful when you need to manually control the payload.
     */
    public static <T> T post(String url, String jsonPayload, Class<T> responseType) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
                .build();
        LOGGER.log(Level.INFO, "POST request to " + url + ", body: " + jsonPayload);
        LOGGER.log(Level.INFO, "POST headers: " + request.headers());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        LOGGER.log(Level.INFO, "POST response: " + response.statusCode() + ", body: " + response.body());
        if (response.statusCode() >= 400) {
            throw new Exception("HTTP error: " + response.statusCode() + ", body: " + response.body());
        }
        return mapper.readValue(response.body(), responseType);
    }
    
    public static <T> T put(String url, Object body, Class<T> responseType) throws Exception {
        String jsonBody = mapper.writeValueAsString(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        LOGGER.log(Level.INFO, "PUT request to " + url + ", body: " + jsonBody);
        LOGGER.log(Level.INFO, "PUT headers: " + request.headers());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        LOGGER.log(Level.INFO, "PUT response: " + response.statusCode() + ", body: " + response.body());
        if (response.statusCode() >= 400) {
            throw new Exception("HTTP error: " + response.statusCode() + ", body: " + response.body());
        }
        if (responseType != Void.class) {
            return mapper.readValue(response.body(), responseType);
        }
        return null;
    }

    /**
     * Sends an HTTP DELETE request to the specified URL.
     * @param url The URL to send the request to.
     * @param responseType The class of the object to map the response body to.
     * @param <T> The type of the response object.
     * @return An object of the specified responseType, or null for a void response.
     * @throws Exception if the request fails.
     */
    public static <T> T delete(String url, Class<T> responseType) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .DELETE()
                .build();
        LOGGER.log(Level.INFO, "DELETE request to " + url);
        LOGGER.log(Level.INFO, "DELETE headers: " + request.headers());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        LOGGER.log(Level.INFO, "DELETE response: " + response.statusCode() + ", body: " + response.body());
        if (response.statusCode() >= 400) {
            throw new Exception("HTTP error: " + response.statusCode() + ", body: " + response.body());
        }
        if (responseType != Void.class) {
            return mapper.readValue(response.body(), responseType);
        }
        return null;
    }

    public static <T> T login(String url, Object body, Class<T> responseType) throws Exception {
        String jsonBody = mapper.writeValueAsString(body);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        LOGGER.log(Level.INFO, "Login request to " + url + ", body: " + jsonBody);
        LOGGER.log(Level.INFO, "Login headers: " + request.headers());
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        LOGGER.log(Level.INFO, "Login response: " + response.statusCode() + ", body: " + response.body());
        if (response.statusCode() >= 400) {
            throw new Exception("HTTP error: " + response.statusCode() + ", body: " + response.body());
        }
        return mapper.readValue(response.body(), responseType);
    }
}
