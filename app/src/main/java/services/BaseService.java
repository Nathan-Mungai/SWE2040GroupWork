package services;

import okhttp3.OkHttpClient;
import java.util.concurrent.TimeUnit;

/**
 * A base class for all service classes.
 * It provides common configurations such as the OkHttpClient instance and the base API URL.
 */
public abstract class BaseService {

    // You should replace this with your actual base URL.
    // For this example, we'll use a placeholder.
    private static final String BASE_URL = "http://localhost:8080/api/";

    protected final String apiUrl;
    protected final OkHttpClient client;

    public BaseService(String endpoint) {
        // Build a client with a timeout
        this.client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .build();
        
        // Construct the full API URL for a specific endpoint
        this.apiUrl = BASE_URL + endpoint;
    }
}
