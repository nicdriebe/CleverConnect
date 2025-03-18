package com.example.application.views.registrierenzweitbetreuerin;

import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

/**
 * Diese Klasse repraesentiert ein Feld zur Eingabe einer externen E-Mail-Adresse.
 * Sie ermoeglicht es Benutzern, eine E-Mail-Adresse einzugeben und zu validieren.
 */
public class ExternalEmail extends VerticalLayout {
    private EmailField emailField;

    /**
     * Konstruktor fuer ein ExternalEmail-Objekt.
     * Initialisiert das Textfeld fuer die E-Mail-Adresse und fuegt es der Ansicht hinzu.
     */
    public ExternalEmail() {
        emailField = createEmailField("E-Mail-Adresse*", "Bitte geben Sie eine gültige E-Mail-Adresse ein.");
        add(emailField);
    }

    /**
     * Erstellt ein EmailField mit angegebenem Label und Fehlermeldung.
     *
     * @param label       Das Label für das Textfeld.
     * @param errorMessage Die Fehlermeldung, die angezeigt wird, wenn die Eingabe ungueltig ist.
     * @return Das erstellte EmailField.
     */
    private EmailField createEmailField(String label, String errorMessage) {
        emailField = new EmailField();
        emailField.setLabel(label);
        emailField.getElement().setAttribute("name", "email");
        emailField.setErrorMessage(errorMessage);
        emailField.setClearButtonVisible(true);
        emailField.setWidth("105%");
        emailField.getStyle().set("margin-left", "-10px");

        emailField.addValueChangeListener(event -> validateEmail(emailField.getValue(), emailField));

        return emailField;
    }

    /**
     * Validiert die eingegebene E-Mail-Adresse.
     *
     * @param email      Die eingegebene E-Mail-Adresse.
     * @param emailField Das EmailField-Objekt, das validiert wird.
     * @return true, wenn die E-Mail-Adresse gueltig ist, ansonsten false.
     */
    private boolean validateEmail(String email, EmailField emailField) {
        // E-Mail muss ein @ und . enthalten
        boolean isValid = email.matches(".*@.*");
        emailField.setInvalid(!isValid);
        return isValid;
    }

    /**
     * Validiert die eingegebene E-Mail-Adresse im Feld.
     *
     * @return true, wenn die E-Mail-Adresse gueltig ist, ansonsten false.
     */
    public boolean validate() {
        return validateEmail(emailField.getValue(), emailField);
    }

    /**
     * Gibt die eingegebene E-Mail-Adresse zurueck.
     *
     * @return Die eingegebene E-Mail-Adresse.
     */
    public String getEmail() {
        return emailField.getValue();
    }

    /**
     * Loescht den Inhalt des E-Mail-Felds.
     */
    public void clear() {
        emailField.clear();
    }

}
