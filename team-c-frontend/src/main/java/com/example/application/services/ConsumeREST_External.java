package com.example.application.services;

import com.example.application.dto.ExternalDTO;
import com.example.application.dto.ExternalRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.UI;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;


@Service
public class ConsumeREST_External {

    private final WebClient client;

    @Autowired
    public ConsumeREST_External(WebClient.Builder builder) {
        this.client = builder.baseUrl("http://localhost:3000").build();
    }

    /**
     * diese Methode legt einen "External" (User) an und sendet die Daten an den Server
     * die Antwort (Http Status) des Servers wird ausgelesen
     * wenn ein neuer "External" angelegt wurde, wird auf eine ander Seite weitergeleitet
     * wenn nicht, wird eine Fehlermeldung ausgegeben "Email ist bereits vergeben"
     * Sollte ein andere Fehler aufgetreten sein, wird eine Standard-Error-Message ausgegeben
     * Die Error-Messages werden in der HandeleCreateExternalError-Methode behandelt
     *
     * @param external ExternalDTO object, das alle übergeben Daten des Externals enthält
     */
    public void createExternal(ExternalDTO external) {
        System.out.println("Sending external to server: " + external);

        UI currentUI = UI.getCurrent();
        try {
            ResponseEntity<Void> responseEntity = client.post()
                    .uri("/external/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(external))
                    .retrieve()
                    .toBodilessEntity()
                    .block();  // This will wait for the server response

            HttpStatus statusCode = HttpStatus.valueOf(responseEntity.getStatusCodeValue());

            System.out.println("HTTP Status Code: " + statusCode.value());

            if (statusCode == HttpStatus.CREATED) {
                // wenn Externer erfolgreich gespeichert wurde, wird User auf ErfolgreichRegistrierenView weitergeleitet
                UI.getCurrent().navigate("registered");
            } else {
                // Fehlermeldung, wenn Externe nicht angelegt wurde, weil E-mail schon vergeben war
                System.out.println("Email ist bereits vergeben");
                handleCreateExternalError(new WebClientResponseException("Error", statusCode.value(), "Error", null, null, null), external, currentUI);
            }
        } catch (WebClientResponseException e) {
            // andere Fehlermeldungen
            handleCreateExternalError(e, external, currentUI);
        }
    }


    public boolean createExternalByAdmin(ExternalDTO external) {
        System.out.println("Sending external to server: " + external);

        // Token aus der Vaadin-Sitzung holen
        String token = (String) VaadinSession.getCurrent().getAttribute("jwtToken");

        UI currentUI = UI.getCurrent();
        try {
            ResponseEntity<Void> responseEntity = client.post()
                    .uri("/admin/createExternal")
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + token) // Token im Header setzen
                    .body(BodyInserters.fromValue(external))
                    .retrieve()
                    .toBodilessEntity()
                    .block();  // This will wait for the server response

            HttpStatus statusCode = HttpStatus.valueOf(responseEntity.getStatusCodeValue());

            System.out.println("HTTP Status Code: " + statusCode.value());

            if (statusCode == HttpStatus.CREATED) {
                System.out.println("User wurde angelegt");
                String fullName = external.getFirstName().toUpperCase() + " " + external.getLastName().toUpperCase();
                String successMessage = "User*in  " + fullName + "  wurde erfolgreich angelegt!";
                Notification.show(successMessage, 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                return true; // Operation was successful
            } else {
                // Fehlermeldung, wenn Externe nicht angelegt wurde, weil E-mail schon vergeben war
                System.out.println("Email ist bereits vergeben");
                handleCreateExternalError(new WebClientResponseException("Error", statusCode.value(), "Error", null, null, null), external, currentUI);
                return false; // Operation failed
            }
        } catch (WebClientResponseException e) {
            // andere Fehlermeldungen
            handleCreateExternalError(e, external, currentUI);
            return false; // Operation failed
        }
    }



    /**
     * geht mit den Error , die beim Erstellen eines "Externals" auftreten können
     * This method extracts relevant information
     * from the provided Throwable, such as the error message and additional details, and communicates the error to the
     * user interface (UI) through the event bus.
     *
     * @param throwable repräsentiert den error oder exception der beim Anlegen des Externals  auftaucht
     * @param person ist das ExternalDTO object und repräsentiert den External bei dem es Fehler beim anlegen(create) gab
     * @param ui ist die UI instance verbunden mit dem user interface wo der Fehler gemeldet/angezeigt werden soll
     */

    public void handleCreateExternalError(Throwable throwable, ExternalDTO person, UI ui) {
        String errorMessage = "Failed to create external: " + person.getFirstName() + " " + person.getLastName();

        if (throwable instanceof WebClientResponseException) {
            WebClientResponseException responseException = (WebClientResponseException) throwable;
            String responseBody = responseException.getResponseBodyAsString();

            // Print or log the response body for inspection
            System.out.println("Response Body: " + responseBody);

            try {
                // Try to analyze the JSON and extract the value of the "error" key
                JsonNode json = new ObjectMapper().readTree(responseBody);
                if (json.has("error")) {
                    errorMessage = json.get("error").asText();
                }
            } catch (IOException e) {
                // If there's an error parsing the JSON, retain the original error message
            }
        }

        final String finalErrorMessage = errorMessage;
        ui.access(() -> {
            // Post the error message to the event bus
            MyEventBus.getInstance().post(new ErrorEvent(finalErrorMessage));
        });
    }


    /**
     * Holt sich eine Liste aller Externen (ExternalDTO objects)
     * ruft dafür den entsprechenden Endpunkt der API auf.
     *
     * @return eine Liste der ExternalDTO objects, also der Externen, die von der API geladen wurden.
     *         Wenn der API call erfolgreich war, enthält die Liste alle Externen;
     *         falls nicht oder falls keine Externen in Datenbank sind, wird nur eine leere Liste übergeben.
     */

    public List<ExternalDTO> loadExternals() {
        // Make API call to retrieve External objects
        String apiUrl = "/external/load";

        //Token aus der Vaadin-Sitzung holen
        String token = (String) VaadinSession.getCurrent().getAttribute("jwtToken");

        try {
            List<ExternalDTO> externals = client.get()
                    .uri(apiUrl)
                    .header("Authorization", "Bearer " + token) // Token im Header setzen
                    .retrieve()
                    .bodyToFlux(ExternalDTO.class)
                    .collectList()
                    .block(); // block to wait for the response

            return externals != null ? externals : Collections.emptyList();
        } catch (WebClientResponseException e) {
            System.err.println("Fehler beim Laden der Externen: " + e.getMessage());
            return Collections.emptyList();
        } catch (Exception e) {
            System.err.println("Ein unerwarteter Fehler ist aufgetreten: " + e.getMessage());
            return Collections.emptyList();
        }

    }


    public ExternalDTO loadExternalById(Long id) {
        //Token aus der Vaadin-Sitzung holen
        String token = (String) VaadinSession.getCurrent().getAttribute("jwtToken");

        // Make API call to retrieve a specific External object by ID
        String apiUrl = "/external/load/{id}";

        ExternalDTO external = client.get()
                .uri(apiUrl, id)
                .header("Authorization", "Bearer " + token) // Token im Header setzen
                .retrieve()
                .bodyToMono(ExternalDTO.class)
                .block(); // block to wait for the response

        return external;
    }


    public ResponseEntity<Object> updateExternal(Long id, ExternalRequest external) {
        // Token aus der Vaadin-Sitzung holen
        String token = (String) VaadinSession.getCurrent().getAttribute("jwtToken");
        // Request an die Server senden und die Antwort speichern
        try {
            ResponseEntity<Object> response = client
                    .put()
                    .uri("/external/update/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .header("Authorization", "Bearer " + token) // Token im Header setzen
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(external))
                    .retrieve()
                    .toEntity(Object.class)
                    .block(); // block to wait for the response


            // Überprüfen des Status-Codes
            if (response.getStatusCode() == HttpStatus.OK) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }


}


