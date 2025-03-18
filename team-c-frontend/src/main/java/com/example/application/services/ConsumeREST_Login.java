package com.example.application.services;

import com.example.application.dto.LoginResponse;
import com.example.application.entities.Role;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import javax.sql.DataSource;

@Service
public class ConsumeREST_Login {

    private final WebClient client;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public ConsumeREST_Login(WebClient.Builder builder, DataSource dataSource) {
        this.client = builder.baseUrl("http://localhost:3000").build();
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    /**
     * Die Methoder um einen User anzumelden
     * @param email ist die E-Mail die der Benutzer zum Anmelden nutzt
     * @param password ist das Passwort das der Benutzer zum Anmelden eingibt
     * @return ist ein Token der für die Session generiert wird, die Rolle des sich anzumeldenden Benutzers und
     * die ID des Benutzers
     */

    public LoginResponse loginUser(String email, String password) {
        ResponseEntity<String> response = client
                .post()
                .uri("/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(new LoginRequest(email, password)))
                .retrieve()
                .toEntity(String.class)
                .block();


        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            // Extrahieren des Tokens, der Rolle und der ID aus dem JSON-String
            JsonObject jsonObject = JsonParser.parseString(response.getBody()).getAsJsonObject();
            JsonElement roleElement = jsonObject.get("role");
            JsonElement tokenElement = jsonObject.get("token");
            JsonElement idElement = jsonObject.get("id");

            String token = null;
            Role role = null;
            Long id = null;

            if (tokenElement.isJsonPrimitive()) {
                token = tokenElement.getAsString();
            }

            if (roleElement.isJsonPrimitive()) {
                role = Role.valueOf(roleElement.getAsString());
            }

            if (idElement.isJsonPrimitive()) {
                id = idElement.getAsLong();
            }


            VaadinSession.getCurrent().setAttribute("jwtToken", token);
            VaadinSession.getCurrent().setAttribute("role", role);
            VaadinSession.getCurrent().setAttribute("id", id);
            System.out.println("User ID: " + id); // Debugging-Statement

            return new LoginResponse(token, role, id);
        }
        else
        {
            return null;
        }
    }

    /**
     * Die Methode logt den Benutzer aus
     * @return gibt ein true zurück wenn der Token wieder auf Null gesetzt wurde
     */

    public Boolean logoutUser() {
        String token = (String) VaadinSession.getCurrent().getAttribute("jwtToken");
        if (token == null) {
            System.out.println("Debug: Kein Token in der Session vorhanden.");
            return false;
        }

        try {
            ResponseEntity<Void> response = client
                    .get()
                    .uri("/user/logout")
                    .header("Authorization", "Bearer " + token)
                    .retrieve()
                    .toBodilessEntity()
                    .block();

            // Entfernen des Tokens aus der Vaadin-Sitzung
            VaadinSession.getCurrent().setAttribute("jwtToken", null);

            // Programmatische Überprüfung und Debugging-Statement
            if (VaadinSession.getCurrent().getAttribute("jwtToken") == null) {
                System.out.println("Logout erfolgreich: Token wurde aus der Session entfernt.");
                return true;
            } else {
                System.err.println("Logout fehlgeschlagen: Token ist noch in der Session.");
                return false;
            }
        } catch (Exception e) {
            System.err.println("Fehler beim Logout: " + e.getMessage());
            return false;
        }
    }



    private static class LoginRequest {
        /**
         * Erfragt die einzelnen Daten für einen Login
         *
         */
        /**
         * Die E-Mail des Benutzers
         */
        private String email;
        /**
         * Das Passwort des Benutzers
         */
        private String password;

        /**
         * Konstruktor der Klasse. Setzt die übergebenen Eingaben als die Objektvariablen
         * @param email E-Mail des Benutzers
         * @param password Passwort des Benutzers
         */

        public LoginRequest(String email, String password) {
            this.email = email;
            this.password = password;
        }

        /**
         * Gibt das Passwort des Benutzers zurück
         * @return das Passwort des Benutzers
         */
        public String getPassword() {
            return password;
        }

        /**
         * Setzt das Passwort des Benutzers
         * @param password das zu setzende Passwort des Benutzers
         */
        public void setPassword(String password) {
            this.password = password;
        }

        /**
         * Setzt die E-Mail des Benutzers
         * @param email die zu setzende E-Mail des Benutzers
         */

        public void setEmail(String email) {
            this.email = email;
        }

        /**
         * Gibt die E-Mail des Benutzers zurück
         * @return die E-Mail des Benutzers
         */
        public String getEmail() {
            return email;
        }
    }




    public ResponseEntity<Object> changePassword(String token, String value, String value1) {
        try {
            // Request-Objekt erstellen
            ChangePasswordRequest request = new ChangePasswordRequest(value, value1);

            //Request an den Server senden und die Antwort speichern
            ResponseEntity<Object> response = client
                    .put()
                    .uri("/user/updatePassword")
                    .header("Authorization", "Bearer " + token)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(BodyInserters.fromValue(request))
                    .retrieve()
                    .toEntity(Object.class)
                    .block();

            //Überprüfen des Status-Codes
            if (response.getStatusCode() == HttpStatus.OK) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(response.getStatusCode()).body(response.getBody());
            }
        } catch (WebClientResponseException e) {
            return ResponseEntity.status(e.getStatusCode()).body(e.getResponseBodyAsString());
        }
    }

    private static class ChangePasswordRequest {
        private String oldPassword;
        private String newPassword;

        public ChangePasswordRequest(String oldPassword, String newPassword) {
            this.oldPassword = oldPassword;
            this.newPassword = newPassword;
        }

        public String getOldPassword() {
            return oldPassword;
        }

        public void setOldPassword(String oldPassword) {
            this.oldPassword = oldPassword;
        }

        public String getNewPassword() {
            return newPassword;
        }

        public void setNewPassword(String newPassword) {
            this.newPassword = newPassword;
        }
    }

}
