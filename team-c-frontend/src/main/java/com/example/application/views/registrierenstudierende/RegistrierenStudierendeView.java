package com.example.application.views.registrierenstudierende;

import com.example.application.data.SamplePerson;
import com.example.application.services.ConsumeREST;
import com.example.application.views.hauptseite.HauptseiteView;
import com.example.application.views.inhaltfooter.FooterLayout;
import com.example.application.services.MyEventBus;
import com.example.application.services.ErrorEvent;

import com.google.common.eventbus.Subscribe;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;

import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Diese Klasse repraesentiert die View zum Registrieren von Studierenden.
 * Sie enthaelt ein Formular zur Eingabe von Benutzerdaten wie Vorname, Nachname,
 * HTW E-Mail, Passwort und Passwortbestaetigung.
 */
@PageTitle("Registrieren Studierende")
@Route(value = "person-form2", layout = FooterLayout.class)
@Uses(PasswordField.class)
public class RegistrierenStudierendeView extends Composite<VerticalLayout> {
    private final ConsumeREST service;

    private final TextField firstName;
    private final TextField lastName;
    private final EmailField email;
    private final PasswordField password;
    private final PasswordField passwordConfirm;

    /**
     * Konstruktor für die RegistrierenStudierendeView.
     * @param service Der Service fuer Benutzerdaten.
     */
    public RegistrierenStudierendeView(ConsumeREST service) {
        this.service = service;
        MyEventBus.getInstance().register(this);

        H3 headline = new H3("Erstelle deinen Account");

        Span requiredIndicators = new Span("*Pflichtfelder");
        requiredIndicators.getStyle().set("font-size", "small");

        FormLayout formLayout = new FormLayout();
        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout buttonLayout = new HorizontalLayout();

        Paragraph textSmall = new Paragraph();
        textSmall.add(requiredIndicators);

        firstName = new TextField("Vorname*");
        firstName.setRequiredIndicatorVisible(true);
        lastName = new TextField("Name*");
        lastName.setRequiredIndicatorVisible(true);
        email = new EmailField("HTW E-Mail*");
        email.setRequiredIndicatorVisible(true);

        password = createPasswordField("Passwort*");
        UI.getCurrent().navigate(HauptseiteView.class);
        passwordConfirm = createPasswordField("Passwort wiederholen*");
        UI.getCurrent().navigate(HauptseiteView.class);

        Button registerButton = createRegisterButton();
        Button cancelButton = createCancelButton();

        // Styles
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.setWidth("100%");
        verticalLayout.setMaxWidth("800px");
        verticalLayout.setHeight("min-content");
        formLayout.setWidth("800px");
        formLayout.setHeight("350px");
        buttonLayout.addClassName(LumoUtility.Gap.MEDIUM);
        buttonLayout.setWidth("100%");
        buttonLayout.getStyle().set("flex-grow", "1");

        formLayout.add(
                firstName,
                lastName,
                email,
                password,
                passwordConfirm
        );

        getContent().add(verticalLayout);
        verticalLayout.add(
                headline,
                textSmall,
                formLayout,
                buttonLayout
        );
        buttonLayout.add(
                registerButton,
                cancelButton
        );
    }

    /**
     * Event-Handler-Methode, die aufgerufen wird, wenn ein Fehler im System auftritt.
     *
     * @param event Das ErrorEvent-Objekt, das den aufgetretenen Fehler enthaelt.
     */
    @Subscribe
    public void onErrorEvent(ErrorEvent event) {
        UI.getCurrent().access(() -> {
            Notification.show(event.getMessage(), 5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        });
    }

    /**
     * Event-Handler-Methode, die aufgerufen wird, wenn die View vom UI entfernt wird.
     *
     * @param detachEvent Das DetachEvent-Objekt, das das Entfernen der View vom UI repraesentiert.
     */
    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        MyEventBus.getInstance().unregister(this);
    }

    /**
     * Hilfsmethode zur Erstellung eines PasswordFields mit bestimmten Konventionen und Styles.
     *
     * @param label Das Label für das PasswordField.
     * @return Ein PasswordField mit den spezifizierten Konventionen und Styles.
     */
    private PasswordField createPasswordField(String label) {
        PasswordField passwordField = new PasswordField(label);
        passwordField.setWidth("min-content");
        passwordField.setClearButtonVisible(true);

        if (label.equals("Passwort*")) {
            passwordField.setPattern("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!?@(){}\\[\\]\\\\/~$%&#*-+.,_])(?=\\S+$).{8,}$");
            passwordField.setErrorMessage("Mindestens 8 Zeichen, 1 Großbuchstabe, 1 Kleinbuchstabe, 1 Sonderzeichen, 1 Ziffer");
        }

        return passwordField;
    }

    /**
     * Erstellt den Registrieren-Button.
     *
     * @return Der erstellte Registrieren-Button.
     */
    private Button createRegisterButton() {
        Button registerButton = new Button("Registrieren");
        registerButton.getStyle().set("margin-top", "20px");
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registerButton.addClickListener(e -> registerUser());

        return registerButton;
    }

    /**
     * Erstellt den Abbrechen-Button.
     *
     * @return Der erstellte Abbrechen-Button.
     */
    private Button createCancelButton() {
        Button cancelButton = new Button("Abbrechen");
        cancelButton.addClickListener(event -> navigateToStartseite());
        cancelButton.getStyle().set("margin-top", "20px");
        cancelButton.addClickListener(e -> clearForm());

        return cancelButton;
    }

    /**
     * Methode zur Durchfuehrung der Benutzerregistrierung.
     */
    private void registerUser() {
        firstName.setInvalid(firstName.isEmpty());
        lastName.setInvalid(lastName.isEmpty());
        email.setInvalid(email.isEmpty());
        password.setInvalid(password.isEmpty());
        passwordConfirm.setInvalid(passwordConfirm.isEmpty());

        // Pflichtfelder validieren
        if (areRequiredFieldsEmpty()) {
            // Error anzeigen
            Notification.show("Pflichtfelder ausfüllen",
                    3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);

            return;
        }

        String emailErrorMessage = validateEmail();
        if (emailErrorMessage != null) {
            email.setErrorMessage(emailErrorMessage);
            email.setInvalid(true);

            return;
        }

        SamplePerson newUser = new SamplePerson();
        newUser.setFirstName(firstName.getValue());
        newUser.setLastName(lastName.getValue());
        newUser.setEmail(email.getValue());
        newUser.setPassword(password.getValue());

        if (confirmPassword()) {
            try {
                service.createUser(newUser);

            } catch (WebClientResponseException e) {
                // Error cases behandeln
                service.handleCreateUserError(e, newUser, UI.getCurrent());
            }
        }
    }

    /**
     * Validiert die E-Mail einer Benutzerin nach den Konventionen.
     *
     * @return Eine Fehlermeldung, wenn die Validierung fehlschlaegt, sonst null.
     */
    private String validateEmail() {
        String email = this.email.getValue().toLowerCase();
        if (!email.endsWith("@student.htw-berlin.de") && !email.endsWith("@htw-berlin.de")) {
            return "Trage deine HTW E-Mail ein";
        }

        return null;
    }

    /**
     * Ueberprueft, ob die Pflichtfelder im Formular leer sind.
     *
     * @return True, wenn mindestens ein Pflichtfeld leer ist, sonst false.
     */
    private boolean areRequiredFieldsEmpty() {
        return firstName.isEmpty() || lastName.isEmpty() || email.isEmpty() ||
                password.isEmpty() || passwordConfirm.isEmpty();
    }

    /**
     * Ueberprueft, ob das eingegebene Passwort mit dem bestaetigten Passwort uebereinstimmt
     * und den Konventionen entspricht.
     *
     * @return True, wenn das Passwort gueltig ist, sonst false.
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
                Notification.show("Passwort entspricht nicht den Konventionen.",
                        3000 ,Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        } else {
            Notification.show("Passwort stimmt nicht überein",
                    3000 ,Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
        }

        return false;
    }

    /**
     * Leert alle Eingabefelder im Formular.
     */
    private void clearForm() {
        firstName.clear();
        lastName.clear();
        email.clear();
        password.clear();
        passwordConfirm.clear();
    }

    /**
     * Navigiert zur Startseite der Anwendung.
     */
    private void navigateToStartseite() {
        UI.getCurrent().navigate("startseite");
    }

}