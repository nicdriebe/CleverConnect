package com.example.application.views.hauptseite;

import com.example.application.dto.ExternalDTO;
import com.example.application.services.ConsumeREST_External;
import com.example.application.views.registrierenzweitbetreuerin.ExternalEmail;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Die Klasse PopUpRegistration repraesentiert ein Popup-Fenster zur Registrierung
 * neuer Benutzerinnen (Zweitbetreuerinnen) durch die Administratorin auf der Hauptseite der Anwendung.
 */
public class PopUpRegistration extends Div {
    private Dialog dialog;

    private TextField firstName;
    private TextField lastName;
    private ExternalEmail email;
    private TextField company;
    private TextField password;
    private TextField passwordConfirm;

    private final ConsumeREST_External service;

    /**
     * Konstruktor fuer das PopUpRegistration-Objekt.
     *
     * @param service Der Service für externe Benutzerdaten.
     */
    public PopUpRegistration(ConsumeREST_External service){
        this.service = service;
        dialog = createDialog();
    }

    /**
     * Erstellt und konfiguriert das Dialogfenster fuer die Registrierung.
     *
     * @return Das erstellte Dialogobjekt.
     */
    private Dialog createDialog() {
        Dialog newDialog = new Dialog();

        newDialog.add(new H3("Neue Userin anlegen"));
        newDialog.add(new Paragraph("*Pflichtfelder"));
        VerticalLayout dialogLayout = createDialogLayout();
        newDialog.add(dialogLayout);

        Button saveButton = createSaveButton(newDialog);
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelButton = new Button("Abbrechen", e -> {
            // Setzt die Werte der Eingabefelder zurueck, wenn der Abbrechen-Button geklickt wird
            resetInputFields();
            newDialog.close();
            // Grid neu laden nach Löschen einer Benutzerin
        });

        newDialog.getFooter().add(cancelButton, saveButton);

        return newDialog;
    }

    /**
     * Erstellt das Layout fuer das Dialogfenster mit den Eingabefeldern.
     *
     * @return Das erstellte Layout für das Dialogfenster.
     */
    private VerticalLayout createDialogLayout() {
        firstName = new TextField("Vorname*");
        lastName = new TextField("Nachname*");
        email= new ExternalEmail();
        company = new TextField("Firma");
        password = new TextField("Passwort*");
        passwordConfirm = new TextField("Passwortwiederholung*");

        firstName.setWidth("100%");
        lastName.setWidth("100%");
        email.setWidth("101%");
        company.setWidth("100%");
        password.setWidth("100%");
        passwordConfirm.setWidth("100%");

        password.setPattern("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!?@(){}\\[\\]\\\\/~$%&#*-+.,_])(?=\\S+$).{8,}$");
        password.setErrorMessage("mindestens 8 Zeichen, " +
                "1 Großbuchstabe, 1 Kleinbuchstabe, 1 Sonderzeichen, 1 Ziffer");
        password.setClearButtonVisible(true);

        passwordConfirm.addValueChangeListener(e -> validateMatchingPasswords(password, passwordConfirm));
        passwordConfirm.setClearButtonVisible(true);

        VerticalLayout dialogLayout = new VerticalLayout(firstName, lastName, email, company, password, passwordConfirm);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.getStyle().set("width", "20rem").set("max-width", "100%");

        return dialogLayout;
    }

    /**
     * Erstellt den "Speichern"-Button fuer das Dialogfenster.
     *
     * @param dialog Das Dialogobjekt.
     * @return Der erstellte "Speichern"-Button.
     */
    private Button createSaveButton(Dialog dialog) {
        Button saveButton = new Button("Speichern", e -> {
            ExternalDTO newUser = createNewUser();

            if (validateFields()) {
                if (confirmPassword()) {
                    ExternalDTO external = createNewUser();
                    ConsumeREST_External consumeRESTExternal = new ConsumeREST_External(WebClient.builder());
                    try {
                        boolean registrationSuccess = consumeRESTExternal.createExternalByAdmin(external);

                        if (registrationSuccess) {
                            resetInputFields();
                            dialog.close();
                            System.out.println("New User: " + newUser);
                        } else {
                            Notification.show("Email bereits vergeben", 3000, Notification.Position.MIDDLE)
                                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
                        }

                    } catch (WebClientResponseException ex) {
                        // Fehler behandeln und entsprechende Meldung anzeigen
                        consumeRESTExternal.handleCreateExternalError(ex, external, UI.getCurrent());
                    }
                }
            } else {
                // Zeige eine Fehlermeldung oder markiere leere Pflichtfelder rot
                Notification.show("Pflichtfelder ausfüllen", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        return saveButton;
    }

    /**
     * Gibt das Dialogobjekt zurueck.
     *
     * @return Das Dialogobjekt.
     */
    public Dialog getDialog() {
        return dialog;
    }

    /**
     * Erstellt ein neues Zweitbetreuerinnen-Objekt mit den eingegebenen Benutzerdaten.
     *
     * @return Das erstellte Zweitbetreuerinnen-Objekt.
     */
    private ExternalDTO createNewUser (){
        ExternalDTO external = new ExternalDTO();
        external.setFirstName(firstName.getValue());
        external.setLastName(lastName.getValue());
        external.setEmail(email.getEmail());
        external.setPassword(password.getValue());
        external.setCompany(company.getValue());

        return external;
    }

    /**
     * Validiert, ob die Passwoerter in den Eingabefeldern uebereinstimmen.
     *
     * @param passwordField1 Das erste Passwort-Eingabefeld.
     * @param passwordField2 Das zweite Passwort-Eingabefeld zur Bestaetigung.
     */
    private void validateMatchingPasswords(TextField passwordField1, TextField passwordField2) {
        String password1 = passwordField1.getValue();
        String password2 = passwordField2.getValue();

        if (!password1.equals(password2)) {
            passwordField2.setInvalid(true);
            passwordField2.setErrorMessage("Die Passwörter stimmen nicht überein. Bitte Überprüfen Sie Ihre Eingabe.");
        } else {
            passwordField2.setInvalid(false);
            passwordField2.setErrorMessage(null); // Setzt die Fehlermeldung zurueck, wenn die Passwoerter uebereinstimmen.
        }
    }

    /**
     * Ueberprueft, ob das Passwort den Konventionen entspricht und gibt entsprechende Rueckmeldungen aus.
     *
     * @return True, wenn das Passwort den Konventionen entspricht, ansonsten false.
     */
    private boolean confirmPassword () {
        final String passwordField = this.password.getValue();
        final String confirmPasswordField = this.passwordConfirm.getValue();
        if (passwordField.equals(confirmPasswordField)) {
            if (passwordField.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!?@(){}\\[\\]\\\\/~$%&#*-+.,_])(?=\\S+$).{8,}$")) {
                return true;
            }
            else
            {
                Notification.show("Passwort entspricht nicht den Konventionen.", 3000 ,Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        } else {
            Notification.show("Passwort stimmt nicht überein", 3000 ,Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }

        return false;
    }

    /**
     * Validiert ein Textfeld, ob es leer ist oder nicht.
     *
     * @param textField Das zu validierende Textfeld.
     * @return True, wenn das Textfeld nicht leer ist, ansonsten false.
     */
    private boolean validateTextField(TextField textField) {
        boolean isEmpty = textField.isEmpty();
        textField.setInvalid(isEmpty);
        return !isEmpty;
    }

    /**
     * Validiert alle Eingabefelder des Dialogfensters.
     *
     * @return True, wenn alle Pflichtfelder ausgefuellt sind, ansonsten false.
     */
    private boolean validateFields(){
        boolean isValid = true;

        isValid &= validateTextField(firstName);
        isValid &= validateTextField(lastName);
        isValid &= validateTextField(password);
        isValid &= validateTextField(passwordConfirm);
        isValid &= email.validate();

        return isValid;
    }

    /**
     * Setzt die Werte der Eingfabefelder zurueck.
     */
    private void resetInputFields() {
        firstName.clear();
        lastName.clear();
        email.clear();
        company.clear();
        password.clear();
        passwordConfirm.clear();
    }

}



