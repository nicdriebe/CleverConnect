package com.example.application.services;

import com.example.application.data.SamplePerson;
import com.example.application.dto.ExternalDTO;
import com.example.application.entities.LoginUser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;


@Service
public class ConsumeREST {

    private final WebClient client;

    @Autowired
    public ConsumeREST(WebClient.Builder builder) {
        this.client = builder.baseUrl("http://localhost:3000").build();
    }


    /**
     * Gibt den user mit allen Daten an den Server
     * geht mit Rückgabe des servers um und mit eventuell auftretenden Fehlern
     * @param user SamplePerson object, dass alle übergeben Daten des Users enthält
     *
     */
    public void createUser(SamplePerson user) {
        System.out.println("Sending external to server: " + user);

        UI currentUI = UI.getCurrent();
        try {
            ResponseEntity<Void> responseEntity = client.post()
                    .uri("/user/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(user))
                    .retrieve()
                    .toBodilessEntity()
                    .block();  // This will wait for the server response

            HttpStatus statusCode = HttpStatus.valueOf(responseEntity.getStatusCodeValue());

            System.out.println("HTTP Status Code: " + statusCode.value());

            if (statusCode == HttpStatus.CREATED) {
                // External created successfully, navigate to the desired site
                UI.getCurrent().navigate("registered");
            } else if (statusCode == HttpStatus.CONFLICT) {
                // Email already exists, show an error message
                System.out.println("Email ist bereits vergeben");
                handleCreateUserError(new WebClientResponseException("Error", statusCode.value(), "Error", null, null, null), user, currentUI);
            } else {
                // Handle the case when the response status code is not 201 CREATED or 409 CONFLICT
                handleCreateUserError(new WebClientResponseException("Error", statusCode.value(), "Error", null, null, null), user, currentUI);
            }
        } catch (WebClientResponseException e) {
            // Handle other error cases
            handleCreateUserError(e, user, currentUI);
        }
    }





    /**
     * Holt sich eine Liste aller User (LoginUser Objekte)
     * ruft dazu den entsprechenden Endpunkt auf
     * @return gibt eine Liste der LoginUser Objekte, also der User die durch den API cll geladen wurden,
     *          falls keine User vorhanden oder Fehler beim Call, wird eine leere Liste zurückgegeben
     */
    public List<LoginUser> loadUser() {
        // Make API call to retrieve User objects
        String apiUrl = "/user/load";

        // Token aus der Vaadin-Sitzung holen
        String token = (String) UI.getCurrent().getSession().getAttribute("jwtToken");

        try{
            List<LoginUser> users = client.get()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + token) // Token im Header setzen
                    .retrieve()
                    .bodyToFlux(LoginUser.class)
                    .collectList()
                    .block(); // block to wait for the response

            return users != null ? users : Collections.emptyList();
        } catch (WebClientResponseException e) {
            System.err.println("Fehler beim Laden der Benutzer: " + e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Ein unerwarteter Fehler ist aufgetreten: " + e.getMessage());
            return Collections.emptyList();
        }
    }


    /**
     * geht mit Fehlern, die während des Anlegens eines Users (SamplePerson) entstehen können
     * Fehlermeldungen werden als Notification angezeigt
     * @param throwable repräsentiert die Fehlermedlung, die während des Anlegens des User aufgetreten ist
     * @param person ist das SamplePerson Objekt für welches das Anlegen/Speichern gescheitert ist
     * @param ui ist die UI instance verbunden mit dem user interface wo der Fehler gemeldet/angezeigt werden soll
     */
    public void handleCreateUserError(Throwable throwable, SamplePerson person, UI ui) {
        AtomicReference<String> errorMessageRef = new AtomicReference<>("Failed to create external: " + person.getFirstName() + " " + person.getLastName());

        if (throwable instanceof WebClientResponseException) {
            WebClientResponseException responseException = (WebClientResponseException) throwable;
            String responseBody = responseException.getResponseBodyAsString();

            // Print or log the response body for inspection
            System.out.println("Response Body: " + responseBody);

            try {
                // Try to analyze the JSON and extract the value of the "error" key
                JsonNode json = new ObjectMapper().readTree(responseBody);
                if (json.has("error")) {
                    errorMessageRef.set(json.get("error").asText());
                }
            } catch (IOException e) {
                // If there's an error parsing the JSON, retain the original error message
            }
        }

        // Display the error message using Notification
        ui.access(() -> {
            Notification.show(errorMessageRef.get(), 5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        });
    }


    /**
     * Löscht einen Benutzer mit der entsprechenden ID,
     * ruft dazu den dazugehörigen Endpunkt auf
     * @param id eindeutiges Merkmal des Users
     */
    public void deleteUserById(Long id) {
        String apiUrl = "/user/delete/{id}";

        //Token aus der Vaadin-Sitzung holen
        String token = (String) VaadinSession.getCurrent().getAttribute("jwtToken");

        try{
            client.delete()
                    .uri(apiUrl, id)
                    .header("Authorization", "Bearer " + token) // Token im Header setzen
                    .retrieve()
                    .bodyToMono(Void.class) // Assuming the server returns no content on successful deletion
                    .block(); // block to wait for the response
        } catch (WebClientResponseException e) {
            System.err.println("Fehler beim Löschen des Users: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Ein unerwarteter fehler ist aufgetreten: " + e.getMessage());
        }
    }

}