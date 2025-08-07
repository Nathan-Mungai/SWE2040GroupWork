package services;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import models.QueuedPatient;
import models.Queue;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class QueueService {

    private static final Logger LOGGER = Logger.getLogger(QueueService.class.getName());
    private static final String BASE_URL = "http://localhost:8080/api/queues";
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public QueueService() {
        this.httpClient = HttpClient.newBuilder().build();
        this.objectMapper = new ObjectMapper();
        this.objectMapper.registerModule(new JavaTimeModule());
        this.objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
    }

    /**
     * Adds a new patient to the queue by sending a POST request to the API.
     * @param newQueue The Queue object to be added.
     * @throws IOException if an I/O error occurs during the HTTP request or if the response status is not successful.
     * @throws InterruptedException if the operation is interrupted.
     */
    public void addToQueue(Queue newQueue) throws IOException, InterruptedException {
        String jsonPayload = objectMapper.writeValueAsString(newQueue);
        
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(BASE_URL))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(jsonPayload))
            .build();
            
        LOGGER.log(Level.INFO, "POST request to " + BASE_URL + ", body: " + jsonPayload);

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            LOGGER.log(Level.INFO, "POST response: " + response.statusCode() + ", body: " + response.body());
        } else {
            LOGGER.log(Level.SEVERE, "POST response: " + response.statusCode() + ", body: " + response.body());
            throw new IOException("Failed to add to queue. HTTP error: " + response.statusCode() + ", body: " + response.body());
        }
    }

    /**
     * Retrieves the current queue for a specific doctor.
     * @param doctorId The ID of the doctor whose queue is to be retrieved.
     * @return A list of Queue objects.
     * @throws IOException if an I/O error occurs during the HTTP request.
     * @throws InterruptedException if the operation is interrupted.
     */
    public List<Queue> getQueueByDoctor(Long doctorId) throws IOException, InterruptedException {
        String url = BASE_URL + "/doctor/" + doctorId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Accept", "application/json")
                .GET()
                .build();
        
        LOGGER.log(Level.INFO, "GET request to " + url);

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        LOGGER.log(Level.INFO, "GET response: " + response.statusCode() + ", body: " + response.body());
        
        if (response.statusCode() == 200) {
            return objectMapper.readValue(response.body(), new TypeReference<List<Queue>>() {});
        } else {
            LOGGER.log(Level.SEVERE, "GET response failed: " + response.statusCode() + ", body: " + response.body());
            return List.of();
        }
    }

    /**
     * Removes a patient from the queue.
     * @param queueId The ID of the queue item to remove.
     * @throws IOException if an I/O error occurs during the HTTP request.
     * @throws InterruptedException if the operation is interrupted.
     */
    public void removeFromQueue(Long queueId) throws IOException, InterruptedException {
        String url = BASE_URL + "/" + queueId;
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .DELETE()
                .build();

        HttpResponse<Void> response = httpClient.send(request, HttpResponse.BodyHandlers.discarding());

        // Fix: Allow both 200 (OK) and 204 (No Content) as successful responses.
        if (response.statusCode() != 200 && response.statusCode() != 204) {
            throw new IOException("Failed to remove from queue. HTTP error: " + response.statusCode());
        }
    }
    
    /**
     * Marks a patient as seen by removing them from the queue.
     * This is typically a DELETE operation on the queue item.
     * @param queuedPatient The queued patient object to remove.
     * @throws IOException if an I/O error occurs during the HTTP request.
     * @throws InterruptedException if the operation is interrupted.
     */
    public void markPatientAsSeen(QueuedPatient queuedPatient) throws IOException, InterruptedException {
        if (queuedPatient != null) {
            removeFromQueue(queuedPatient.getQueueId());
        }
    }
}
