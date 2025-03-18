package com.example.application.views.registrierenzweitbetreuerin;

import com.example.application.dto.BachelorSubjectDTO;
import com.example.application.dto.ExternalDTO;
import com.example.application.dto.SpecialFieldDTO;
import com.example.application.services.ConsumeREST_External;
import com.example.application.services.ErrorEvent;
import com.example.application.services.MyEventBus;
import com.example.application.views.inhaltfooter.FooterLayout;

import com.google.common.eventbus.Subscribe;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.DetachEvent;
import com.vaadin.flow.component.HasValue;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;

import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Diese Klasse repraesentiert die Ansicht fuer die Registrierung einer Zweitbetreuerin.
 * Benutzerinnen koennen hier ihre persoenlichen Informationen eingeben und sich registrieren.
 */
@PageTitle("Registrieren Zweitbetreuer*in")
@Route(value = "person-form", layout = FooterLayout.class)
@Uses(Icon.class)
public class RegistrierenZweitbetreuerinView extends Composite<VerticalLayout> {
    private TextArea bDescription;
    private TextField firstName;
    private TextField lastName;
    private MultiSelectComboBox<SpecialFieldDTO> specialFields;
    private TextField company;
    private PasswordField password;
    private PasswordField passwordConfirm;
    private TextField title;
    private TextArea description;
    private ExternalEmail email;
    private DateRangePicker availabilityPicker;

    public List<BachelorSubjectDTO> bachelorSubjects = new ArrayList<>();
    private final ConsumeREST_External service;

    /**
     * Konstruktor fuer ein RegistrierenZweitbetreuerinView-Objekt.
     * @param service Der Dienst für die externe Kommunikation.
     */
    public RegistrierenZweitbetreuerinView(ConsumeREST_External service){
        this.service = service;
        MyEventBus.getInstance().register(this);

        VerticalLayout verticalLayout = new VerticalLayout();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        H3 h3 = new H3();
        Paragraph textSmall = new Paragraph();
        FormLayout formLayout1Col = new FormLayout();
        firstName = new TextField();
        lastName = new TextField();
        email = new ExternalEmail();
        email.setWidth("100%");
        specialFields = new MultiSelectComboBox<>();
        company = new TextField();
        title = new TextField();
        password = new PasswordField();
        passwordConfirm = new PasswordField();
        description = new TextArea();
        bDescription = new TextArea();

        Div bachelorthemaContainer = new Div();
        bachelorthemaContainer.add(title);

        // Härt auf Anderungen in textField4
        title.addValueChangeListener(this::handleTextField4Change);

        VerticalLayout buttons = new VerticalLayout();
        HorizontalLayout layoutRow = new HorizontalLayout();
        buttons.setWidth("100%");
        buttons.setMaxWidth("800px");
        buttons.getStyle().set("margin-bottom" , "50px");
        buttons.add(layoutRow);
        Button registerButton = new Button();
        Button cancelButton = new Button();

        bDescription.setLabel("Bachelorthema Beschreibung");
        bDescription.setWidth("380px");
        description.getStyle().set("maxHeight", "300px");
        bDescription.setVisible(false);

        // Styles
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);
        verticalLayout.setWidth("100%");
        verticalLayout.setMaxWidth("800px");
        verticalLayout.setHeight("min-content");
        h3.setText("Erstelle deinen Account!");
        h3.setWidth("100%");
        textSmall.setText("Es ist mindestens ein Bachelor of Science Abschluss erforderlich.");
        textSmall.setWidth("100%");
        textSmall.getStyle().set("font-size", "var(--lumo-font-size-xs)");
        formLayout1Col.setWidth("800px");
        formLayout1Col.getStyle().set("margin-bottom" , "10px");
        Icon info = new Icon(VaadinIcon.INFO_CIRCLE_O);

        Span requiredIndicators = new Span("*Pflichtfelder");
        requiredIndicators.getStyle().set("font-size", "small");
        requiredIndicators.getStyle().set("display", "block");
        textSmall.add(requiredIndicators);

        // Beschriftungen der Felder
        firstName.setLabel("Vorname*");
        lastName.setLabel("Name*");
        specialFields.setLabel("Fachgebiete");
        specialFields.setWidth("min-content");
        setMultiSelectComboBoxSampleData(specialFields);
        company.setLabel("Firma");
        company.setWidth("min-content");
        title.setLabel("Bachelorthema");
        title.setWidth("min-content");
        title.setWidth(firstName.getWidth());
        title.setWidth("100%");

        // Zeitraum der Verfügbarkeit
        availabilityPicker = new DateRangePicker();

        // Passwortfeld mit Anforderungen
        password.setLabel("Passwort*");
        password.setPattern("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!?@(){}\\[\\]\\\\/~$%&#*-+.,_])(?=\\S+$).{8,}$");
        password.setErrorMessage("mindestens 8 Zeichen, " +
                "1 Großbuchstabe, 1 Kleinbuchstabe, 1 Sonderzeichen, 1 Ziffer");
        password.setClearButtonVisible(true);

        passwordConfirm.setLabel("Passwort wiederholen*");
        passwordConfirm.addValueChangeListener(e -> validateMatchingPasswords(password, passwordConfirm));
        passwordConfirm.setClearButtonVisible(true);

        description.setLabel("Ich bin hier, weil ...");
        description.getStyle().set("maxHeight", "300px");
        description.setWidth("100%"); // war 380px

        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");

        registerButton.setText("Registrieren");
        registerButton.setWidth("min-content");
        registerButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        registerButton.addClickListener(event -> {
            if (validateFields()) {
                if(confirmPassword()) {
                    ExternalDTO external = createSamplePersonFromFields();
                    ConsumeREST_External consumeRESTExternal = new ConsumeREST_External(WebClient.builder());

                    // Registriere den externen Benutzer
                    consumeRESTExternal.createExternal(external);
                }
            } else {
                // Zeige eine Fehlermeldung oder markiere leere Pflichtfelder rot
                Notification.show("Pflichtfelder ausfüllen", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        cancelButton.setText("Abbrechen");
        cancelButton.addClickListener(event -> navigateToStartseite());
        cancelButton.setWidth("min-content");

        firstName.setRequiredIndicatorVisible(true);
        lastName.setRequiredIndicatorVisible(true);
        password.setRequiredIndicatorVisible(true);
        passwordConfirm.setRequiredIndicatorVisible(true);

        formLayout1Col.add(
                email,
                firstName,
                lastName,
                company,
                availabilityPicker.getAvailabilityStart(),
                availabilityPicker.getAvailabilityEnd(),
                password,
                passwordConfirm,
                specialFields,
                bachelorthemaContainer, // Bachelorthema Container
                bDescription, // Beschreibung field
                description
        );

        getContent().add(verticalLayout, buttons);
        verticalLayout.add(h3, textSmall, formLayout1Col);
        layoutRow.add(registerButton, cancelButton);
    }

    /**
     * Behandelt ein Fehlerereignis.
     * @param event Das Fehlerereignis.
     */
    @Subscribe
    public void onErrorEvent(ErrorEvent event) {
        UI.getCurrent().access(() -> {
            Notification.show(event.getMessage(), 3000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        });
    }

    /**
     * Wird aufgerufen, wenn die Ansicht beendet wird.
     * @param detachEvent Das DetachEvent.
     */
    @Override
    protected void onDetach(DetachEvent detachEvent) {
        super.onDetach(detachEvent);
        MyEventBus.getInstance().unregister(this);
    }

    /**
     * Ueberprueft, ob das Passwort mit den Konventionen uebereinstimmt.
     * @return true, wenn das Passwort den Konventionen entspricht, ansonsten false.
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
     * Ueberprueft, ob ein Textfeld nicht leer ist.
     * @param textField Das Textfeld, das ueberprueft werden soll.
     * @return true, wenn das Textfeld nicht leer ist, ansonsten false.
     */
    private boolean validateTextField(TextField textField) {
        boolean isEmpty = textField.isEmpty();
        textField.setInvalid(isEmpty);
        return !isEmpty;
    }

    /**
     * Ueberprueft, ob ein Passwortfeld nicht leer ist.
     * @param passwordField Das Passwortfeld, das ueberprueft werden soll.
     * @return true, wenn das Passwortfeld nicht leer ist, ansonsten false.
     */
    private boolean validatePasswordField(PasswordField passwordField)  {
        String password = passwordField.getValue();
        boolean isValid = password != null && !password.isEmpty();
        passwordField.setInvalid(!isValid);
        return isValid;
    }

    /**
     * Behandelt Aenderungen im Textfeld.
     * @param event Das Ereignis der Wertaenderung.
     */
    private void handleTextField4Change(HasValue.ValueChangeEvent<String> event) {
        // Show or hide textField6 based on the content of textField4
        String value = event.getValue();
        boolean showbDescription = value != null && !value.isEmpty();
        bDescription.setVisible(showbDescription);
    }

    /**
     * Validiert alle Eingabefelder.
     * @return true, wenn alle Eingabefelder gueltig sind, ansonsten false.
     */
    private boolean validateFields(){
        boolean isValid = true;
        isValid &= validateTextField(firstName);
        isValid &= validateTextField(lastName);
        isValid &= validatePasswordField(password);
        isValid &= validatePasswordField(passwordConfirm);
        isValid &= email.validate();

        return isValid;
    }

    /**
     * Setzt Beispiel-Daten fuer das MultiSelectComboBox-Feld.
     * @param multiSelectComboBox Das MultiSelectComboBox-Feld.
     */
    private void setMultiSelectComboBoxSampleData(MultiSelectComboBox<SpecialFieldDTO> multiSelectComboBox) {
        List<SpecialFieldDTO> specialFields = new ArrayList<>();
        specialFields.add(new SpecialFieldDTO("Künstliche Intelligenz"));
        specialFields.add(new SpecialFieldDTO("Cybersicherheit") );
        specialFields.add(new SpecialFieldDTO("Datenbanken" ) );
        specialFields.add(new SpecialFieldDTO("Softwareentwicklung"));
        specialFields.add(new SpecialFieldDTO("Webentwicklung"));
        specialFields.add(new SpecialFieldDTO("Mobile Anwendungen"));
        specialFields.add(new SpecialFieldDTO("Datenwissenschaft"));
        specialFields.add(new SpecialFieldDTO("Algorithmen und Datenstrukturen"));
        specialFields.add(new SpecialFieldDTO("Human-Computer Interaction"));
        specialFields.add(new SpecialFieldDTO("Quanteninformatik"));
        specialFields.add(new SpecialFieldDTO("Embedded Systems"));
        specialFields.add(new SpecialFieldDTO("Computergrafik"));
        specialFields.add(new SpecialFieldDTO("Cloud Computing"));
        specialFields.add(new SpecialFieldDTO("IoT (Internet of Things)"));
        specialFields.add(new SpecialFieldDTO("Sonstiges"));
        multiSelectComboBox.setItems(specialFields);
        multiSelectComboBox.setItemLabelGenerator(SpecialFieldDTO::getName);
    }

    /**
     * Validiert, ob die Passwörter uebereinstimmen.
     * @param passwordField1 Das erste Passwortfeld.
     * @param passwordField2 Das zweite Passwortfeld.
     */
    private void validateMatchingPasswords(PasswordField passwordField1, PasswordField passwordField2) {
        String password1 = passwordField1.getValue();
        String password2 = passwordField2.getValue();

        if (!password1.equals(password2)) {
            passwordField2.setInvalid(true);
            passwordField2.setErrorMessage("Die Passwörter stimmen nicht überein. Bitte Überprüfen Sie Ihre Eingabe.");
        } else {
            passwordField2.setInvalid(false);
            passwordField2.setErrorMessage(null); // Setzt die Fehlermeldung zurück, wenn die Passwörter übereinstimmen.
        }
    }

    /**
     * Erstellt ein ExternalDTO-Objekt basierend auf den eingegebenen Feldern.
     * @return Das erstellte ExternalDTO-Objekt.
     */
    private ExternalDTO createSamplePersonFromFields() {
        ExternalDTO external = new ExternalDTO();
        external.setFirstName(firstName.getValue());
        external.setLastName(lastName.getValue());
        external.setPassword(password.getValue());
        external.setCompany(company.getValue());
        external.setAvailabilityStart(availabilityPicker.getSelectedStartDate());
        external.setAvailabilityEnd(availabilityPicker.getSelectedEndDate());
        external.setDescription(description.getValue());
        external.setEmail(email.getEmail());

        Set<SpecialFieldDTO> selectedSpecialFields = specialFields.getSelectedItems();
        external.setSpecialFields(selectedSpecialFields);

        List<BachelorSubjectDTO> bachelorSubjects = new ArrayList<>();

        String titleValue = title.getValue();
        String descriptionValue = bDescription.getValue();

        // Prüfen ob Titel und Beschreibung nicht leer sind bevor BachelorSubject kreiert wird
        if (!titleValue.isEmpty()) {
            BachelorSubjectDTO bachelorSubject = new BachelorSubjectDTO();
            bachelorSubject.setTitle(titleValue);
            bachelorSubject.setBDescription(descriptionValue);

            // kreierten BachelorSubject zur Liste hinzufügen
            bachelorSubjects.add(bachelorSubject);
        }

        external.setBachelorSubjects(bachelorSubjects);

        return external;
    }

    /**
     * Navigiert zur Startseite.
     */
    private void navigateToStartseite() {
        UI.getCurrent().navigate("startseite");
    }

}


