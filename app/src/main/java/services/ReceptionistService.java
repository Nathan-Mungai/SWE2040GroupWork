package services;

import models.Receptionist;
import util.HttpClientUtil;

public class ReceptionistService {
    /**
     * Fetches a receptionist by their email address.
     * @param email The email of the receptionist to fetch.
     * @return A Receptionist object.
     * @throws Exception if the HTTP request fails.
     */
    public Receptionist getReceptionistByEmail(String email) throws Exception {
        String url = "http://localhost:8080/api/receptionists/email/" + email;
        return HttpClientUtil.get(url, Receptionist.class);
    }

    /**
     * Fetches a receptionist by their unique ID.
     * @param receptionistId The ID of the receptionist to fetch.
     * @return A Receptionist object.
     * @throws Exception if the HTTP request fails.
     */
    public Receptionist getReceptionistById(Long receptionistId) throws Exception {
        String url = "http://localhost:8080/api/receptionists/" + receptionistId;
        return HttpClientUtil.get(url, Receptionist.class);
    }
}
