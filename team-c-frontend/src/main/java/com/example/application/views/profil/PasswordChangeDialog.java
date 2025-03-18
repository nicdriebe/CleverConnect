package com.example.application.views.profil;

import com.example.application.services.ConsumeREST_Login;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.server.VaadinSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.vaadin.flow.component.Key;

/**
 * Die Klasse PasswordChangeDialog repraesentiert ein Dialogfenster,
 * das es dem User erlaubt, sein Passwort zu aendern.
 */
public class PasswordChangeDialog {
    private final Dialog dialog;
    private final PasswordField oldPassword;
    private final PasswordField newPassword;
    private final PasswordField newPasswordConfirm;
    private final ConsumeREST_Login userService;


    /**
     * Konstruktor für das Passwortänderungsdialog-Objekt.
     *
     * @param userService Der Service für User-login/-logout.
     */
    public PasswordChangeDialog(ConsumeREST_Login userService) {
        this.userService = userService;
        this.dialog = new Dialog();
        this.oldPassword = new PasswordField("Altes Passwort:");
        oldPassword.setWidth("100%");
        this.newPassword = new PasswordField("Neues Passwort:");
        newPassword.setWidth("100%");
        this.newPasswordConfirm = new PasswordField("Neues Passwort wiederholen:");
        newPasswordConfirm.setWidth("100%");

        Button cancelButton = new Button("Abbrechen");
        cancelButton.addClickListener(event -> {
            oldPassword.clear();
            newPassword.clear();
            newPasswordConfirm.clear();
            dialog.close();
        });


        Button saveButton = new Button("Speichern", event -> {
            //Überprüfen, ob das neue und das wiederholte Passwort übereinstimmen
            if (!newPassword.getValue().equals(newPasswordConfirm.getValue())) {
                Notification.show("Die Passwörter stimmen nicht überein.", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            // Methode zum Ändern des Passworts aufrufen
            String token = (String) VaadinSession.getCurrent().getAttribute("jwtToken");
            ResponseEntity<Object> response = userService.changePassword(token, oldPassword.getValue(), newPassword.getValue());

            // Status der Antwort überprüfen und entsprechende Benachrichtigung anzeigen
            if (response.getStatusCode() == HttpStatus.OK) {
                Notification.show("Das Passwort wurde erfolgreich geändert.", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                dialog.close();

                oldPassword.clear();
                newPassword.clear();
                newPasswordConfirm.clear();
            } else {
                Notification.show("Das Passwort konnte nicht geändert werden.", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        // Enter-Taste als Shortcut für den Speichern-Button
        newPasswordConfirm.addKeyPressListener(Key.ENTER, e -> saveButton.click());

        HorizontalLayout buttonLayout = new HorizontalLayout(cancelButton, saveButton);

        VerticalLayout dialogLayout = new VerticalLayout(new H2("Passwort ändern"), oldPassword, newPassword, newPasswordConfirm, buttonLayout);
        dialogLayout.getStyle().set("width", "20rem").set("max-width", "100%");

        dialog.add(dialogLayout);
    }

    /**
     * Öffnet den Dialog.
     */
    public void open() {
        dialog.open();
    }

}

