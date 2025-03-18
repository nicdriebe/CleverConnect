package com.example.application.views.profil;

import com.example.application.dto.BachelorSubjectDTO;
import com.example.application.dto.ExternalDTO;
import com.example.application.dto.ExternalRequest;
import com.example.application.dto.SpecialFieldDTO;
import com.example.application.services.ConsumeREST;
import com.example.application.services.ConsumeREST_External;
import com.example.application.services.ConsumeREST_Login;
import com.example.application.views.dialogs.DeleteProfileDialog;
import com.example.application.views.inhaltfooter.FooterLayout;
import com.example.application.views.startseite.StartseiteView;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Die Klasse ProfilView repraesentiert die Profilseite der Benutzerinnen (Zweitbetreuer*innen).
 * Sie ermoeglicht Benutzerinnen das Bearbeiten ihres Profils und das Aendern ihres Passworts.
 */
@PageTitle("profile")
@Route(value = "profile/:userId", layout = FooterLayout.class)
public class ProfilView extends Composite<VerticalLayout> implements BeforeEnterObserver {
    private final TextField firstName;
    private final TextField lastName;
    private final TextField email;
    private final TextField company;
    private final MultiSelectComboBox<SpecialFieldDTO> specialFields;
    private final TextArea bachelorSubjectTitle;
    private final DatePicker startDatePicker;
    private final DatePicker endDatePicker;
    private final TextArea description;
    private final TextArea bDescription;
    private Button deleteButton;
    private Button saveButton;
    private Button cancelButton;
    // Kopie der Variablen um die originalen Werte zu speichern
    private String initialFirstName;
    private String initialLastName;
    private String initialCompany;
    private LocalDate initialStartDate;
    private LocalDate initialEndDate;
    private String initialDescription;
    private Set<SpecialFieldDTO> initialSpecialFields;
    private String initialBachelorSubjectTitle;
    private String initialBachelorSubjectDescription;
    private AtomicReference<Notification> notificationRef = new AtomicReference<>();
    private final ConsumeREST_Login userService;
    private final ConsumeREST users;
    private final ConsumeREST_External externalService;

    private ExternalDTO externalDTO;

    /**
     * Konstruktor fuer das ProfilView-Objekt.
     * Hier werden die UI-Komponenten initialisiert und angeordnet.
     *
     * @param userService Der Service fuer den Benutzerlogin/-logout.
     * @param users      Der Service fuer Benutzerdaten.
     */
    public ProfilView(ConsumeREST_Login userService, ConsumeREST users, ConsumeREST_External externalService) {
        this.userService = userService;
        this.users = users;
        this.externalService = externalService;
        this.externalDTO = new ExternalDTO();

        Button logoutButton = new Button("Logout", e -> handleLogout());
        VerticalLayout layoutColumn2 = new VerticalLayout();

        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setMaxWidth("100%");
        H3 h3 = new H3();
        h3.setText("Profil bearbeiten und noch besser gefunden werden");
        Image logo = new Image("themes/batchmatch1/views/Logo_search.png", "CC");
        logo.setHeight("44px");
        headerLayout.add(h3, logo);

        FormLayout formLayout2Col = new FormLayout();
        firstName = new TextField("Vorname");
        lastName = new TextField("Nachname");
        email = new TextField("E-Mail");
        email.setReadOnly(true);
        company = new TextField("Firma");

        PasswordChangeDialog passwordChangeDialog = new PasswordChangeDialog(userService);
        Button passwordChangeButton = new Button("Passwort ändern");
        passwordChangeButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        passwordChangeButton.addClickListener(e -> passwordChangeDialog.open());

        Hr hr = new Hr();
        hr.setMaxWidth("100%");
        hr.setHeight("3px");

        FormLayout formLayout2Col2 = new FormLayout();
        startDatePicker = new DatePicker("Verfügbarkeit: Von");
        specialFields = new MultiSelectComboBox<>();
        specialFields.setLabel("Fachgebiete");
        specialFields.setWidth("min-content");

        // Methodenaufruf um Daten zu setzen (Fachgebiete)
        setMultiSelectComboBoxSampleData(specialFields);

        endDatePicker = new DatePicker("Verfügbarkeit: Bis");
        bachelorSubjectTitle = new TextArea();
        description = new TextArea();
        bDescription = new TextArea();
        VerticalLayout layoutColumn3 = new VerticalLayout();
        VerticalLayout layoutColumn4 = new VerticalLayout();
        Checkbox checkbox = new Checkbox();
        Checkbox checkbox2 = new Checkbox();
        HorizontalLayout layoutRow = new HorizontalLayout();

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);
        getContent().setAlignSelf(FlexComponent.Alignment.END, logoutButton);

        logoutButton.setWidth("min-content");
        layoutColumn2.addClassName(LumoUtility.Gap.SMALL);
        layoutColumn2.setWidth("800px");
        layoutColumn2.setMaxWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1").set("margin-bottom", "10%");
        layoutColumn2.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        layoutColumn2.setAlignItems(FlexComponent.Alignment.CENTER);
        layoutColumn2.add(formLayout2Col);
        layoutColumn2.setAlignSelf(FlexComponent.Alignment.CENTER, h3);
        h3.setWidth("max-content");

        formLayout2Col.setWidth("100%");
        formLayout2Col2.setWidth("100%");

        email.setWidth("min-content");
        company.setWidth("min-content");

        Div bachelorthemaContainer = new Div();
        bachelorthemaContainer.add(bachelorSubjectTitle);
        bachelorSubjectTitle.setLabel("Bachelorthema");
        bachelorSubjectTitle.setWidth("min-content");
        bachelorSubjectTitle.setWidth("100%");
        bachelorSubjectTitle.setHeight("80px");

        description.setLabel("Ich bin hier, weil ...");
        description.setWidthFull();
        description.getStyle().set("width", "80%");
        description.setHeight("100px");

        bDescription.setLabel("Bachelorthema Beschreibung");
        bDescription.setWidth("100%");
        bDescription.getStyle().set("width", "80%");
        bDescription.setHeight("100px");

        layoutColumn3.setHeightFull();
        VerticalLayout wrapperLayout = new VerticalLayout();
        wrapperLayout.setWidthFull();
        wrapperLayout.add(formLayout2Col2);
        wrapperLayout.setFlexGrow(1, layoutColumn3);

        layoutColumn3.setWidth("100%");
        layoutColumn3.getStyle().set("flex-grow", "1");
        layoutColumn4.setWidthFull();
        layoutColumn3.setFlexGrow(1, layoutColumn4);
        layoutColumn4.setWidth("100%");
        layoutColumn4.getStyle().set("flex-grow", "1");

        checkbox.setLabel("Profil sichtbar für Studierende");
        checkbox.setWidth("100%");
        checkbox2.setLabel("E-Mail Adresse sichtbar für Studierende");
        checkbox2.setWidth("100%");

        layoutRow.setWidthFull();
        layoutColumn2.setFlexGrow(1, layoutRow);
        layoutRow.addClassName(LumoUtility.Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.getStyle().set("flex-grow", "1");

        // Speichern-Button
        saveButton = new Button("Speichern", event -> {
            // Abrufen der userId aus der VaadinSession
            Long userId = (Long) VaadinSession.getCurrent().getAttribute("id");

            // External-Objekt erstellen und die Daten aktualsieren
            ExternalRequest externalRequest = createExternalRequest();
            updateExternal(userId, externalRequest);
        });

        saveButton.setWidth("min-content");
        saveButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        DeleteProfileDialog deleteProfileDialog = new DeleteProfileDialog(this.externalService, userService, users);

        deleteButton = new Button("Profil Löschen");
        deleteButton.setWidth("min-content");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(e -> {
            Long userId = (Long) VaadinSession.getCurrent().getAttribute("id");
            if(userId != null) {
                System.out.println("User ID: " + userId); // Debugging-Ausgabe
                deleteProfileDialog.open(userId);
            } else {
                System.out.println("User ID is null"); // Debugging-Ausgabe
            }
        });

        cancelButton = new Button("Abbrechen");
        cancelButton.setWidth("min-content");
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        cancelButton.addClickListener(event -> resetToInitialState());

        getContent().add(logoutButton);
        getContent().add(layoutColumn2);
        layoutColumn2.add(
                headerLayout,
                formLayout2Col,
                hr,
                formLayout2Col2,
                layoutRow
        );
        formLayout2Col.add(
                firstName,
                lastName,
                email,
                company,
                passwordChangeButton
        );
        formLayout2Col2.add(
                startDatePicker,
                endDatePicker,
                specialFields,
                bachelorSubjectTitle,
                description,
                bDescription,
                layoutColumn3
        );
        layoutColumn3.add(layoutColumn4);
        layoutColumn4.add(
                checkbox,
                checkbox2)
        ;
        layoutRow.add(
                saveButton,
                deleteButton,
                cancelButton
        );
    }

    /**
     * Die Methode erstellt ein ExternalRequest-Objekt, das die Daten aus den UI-Komponenten enthaelt.
     * @return Das ExternalRequest-Objekt, das die Daten aus den UI-Komponenten enthaelt.
     */
    private ExternalRequest createExternalRequest() {
        ExternalRequest externalRequest = new ExternalRequest();
        externalRequest.setFirstName(firstName.getValue());
        externalRequest.setLastName(lastName.getValue());
        externalRequest.setEmail(email.getValue());
        externalRequest.setCompany(company.getValue());
        externalRequest.setAvailabilityStart(startDatePicker.getValue());
        externalRequest.setAvailabilityEnd(endDatePicker.getValue());
        externalRequest.setDescription(description.getValue());

        Set<SpecialFieldDTO> selectedSpecialFields = specialFields.getSelectedItems();
        externalRequest.setSpecialFields(new ArrayList<>(selectedSpecialFields));

        List<BachelorSubjectDTO> bachelorSubjects = new ArrayList<>();
        String titleValue = bachelorSubjectTitle.getValue();
        String descriptionValue = bDescription.getValue();

        BachelorSubjectDTO bachelorSubject = new BachelorSubjectDTO();
        bachelorSubject.setTitle(titleValue);
        bachelorSubject.setBDescription(descriptionValue);

        // erstellte BachelorSubjectDTO-Objekt zur Liste hinzufügen
        bachelorSubjects.add(bachelorSubject);

        externalRequest.setBachelorSubjects(bachelorSubjects);

        return externalRequest;
    }


    /**
     * Die Methode aktualisiert die Daten eines External-Objekts.
     *
     * @param userId          Die ID des Benutzers.
     * @param externalRequest Das ExternalRequest-Objekt, das die zu aktualisierenden Daten enthaelt.
     */
    private void updateExternal(Long userId, ExternalRequest externalRequest) {
        // updateExternal-Methode aufrufen, um die Daten zu aktualisieren
        ResponseEntity<Object> response = externalService.updateExternal(userId, externalRequest);

        if (response.getStatusCode() == HttpStatus.OK) {
            Notification.show("Ihre Änderungen wurden gespeichert!", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        } else {
            Notification.show("Fehler beim Speichern der Änderungen", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * Behandelt den Logout-Prozess.
     */
    private void handleLogout() {
        boolean logoutSuccess = userService.logoutUser();
        if (logoutSuccess) {
            // Erfolgreiches Logout
            Notification.show("Sie sind ausgeloggt", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            UI.getCurrent().navigate(StartseiteView.class);
        } else {
            // Fehler beim Logout
            Notification.show("Fehler beim Logout", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * Die Methode setzt Beispieldaten für das MultiSelectComboBox-Element.
     * Sie initialisiert eine Liste von Fachgebieten und setzt diese als Items im ComboBox.
     *
     * @param multiSelectComboBox Das MultiSelectComboBox-Element, fuer das die Daten gesetzt werden sollen.
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
     * Vor dem Betreten der Hauptseite ueberpruefen, ob die Benutzerin eingeloggt ist.
     *
     * @param event Das BeforeEnterEvent.
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        //Überprüfen ob User eingeloggt ist
        if (!isUserLoggedIn()) {
            // Nicht eingeloggt, Umleitung zur Startseite
            event.rerouteTo(StartseiteView.class);
        } else {
        // Get the userId from the route parameters
        String userIdParameter = event.getRouteParameters().get("userId").orElse(null);

        if (userIdParameter != null) {
            try {
                Long userId = Long.parseLong(userIdParameter);
                // Call backend service to get user data based on the user ID
                this.externalDTO = externalService.loadExternalById(userId);

                if (externalDTO != null) {
                    // Populate UI components with user data
                    firstName.setValue(externalDTO.getFirstName());
                    lastName.setValue(externalDTO.getLastName());
                    email.setValue(externalDTO.getEmail());
                    company.setValue(externalDTO.getCompany());
                    description.setValue(externalDTO.getDescription());
                    startDatePicker.setValue(externalDTO.getAvailabilityStart());
                    endDatePicker.setValue(externalDTO.getAvailabilityEnd());
                    specialFields.setValue(externalDTO.getSpecialFields());
                    bachelorSubjectTitle.setValue(getBachelorSubjectsTitles(externalDTO.getBachelorSubjects()));
                    bDescription.setValue(getBachelorSubjectsDescriptions(externalDTO.getBachelorSubjects()));

                    // ursprüngliche Werte speichern
                    initialFirstName = firstName.getValue();
                    initialLastName = lastName.getValue();
                    initialCompany = company.getValue();
                    initialStartDate = startDatePicker.getValue();
                    initialEndDate = endDatePicker.getValue();
                    initialDescription = description.getValue();
                    initialSpecialFields = specialFields.getValue();
                    initialBachelorSubjectTitle = bachelorSubjectTitle.getValue();
                    initialBachelorSubjectDescription = bDescription.getValue();
                } else {
                    // Handle the case where user data is null (optional)
                    Notification.show("Fehler beim Laden der Userdaten",
                                    3000, Notification.Position.MIDDLE)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                }
            } catch (NumberFormatException e) {
                // Handle the case where userId parameter is not a valid number
                Notification.show("Ungültige Benutzer-ID",
                                3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                event.forwardTo(StartseiteView.class);
            } catch (Exception e) {
                // Handle other exceptions (e.g., network issues, server errors)
                Notification.show("Fehler beim Laden der Userdaten",
                                3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        } else {
            // Handle the case where userId parameter is missing
            Notification.show("Benutzer-ID fehlt in der URL",
                            3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            event.forwardTo(StartseiteView.class);
        }
    }
}

    /**
     * Überprüft, ob der Benutzer eingeloggt ist.
     *
     * @return true, wenn der Benutzer eingeloggt ist, ansonsten false.
     */
    private boolean isUserLoggedIn() {
        String token = (String) VaadinSession.getCurrent().getAttribute("jwtToken");
        return token != null;
    }


    /**
     * Gibt die Beschreibungen der Bachelor-Themen als String zurück.
     *
     * @param bachelorSubjects Die Liste der Bachelor-Themen.
     * @return Die Beschreibungen der Bachelor-Themen als String.
     */
    private String getBachelorSubjectsDescriptions(List<BachelorSubjectDTO> bachelorSubjects) {
        if (bachelorSubjects != null && !bachelorSubjects.isEmpty()) {
            StringBuilder descriptions = new StringBuilder();
            for (BachelorSubjectDTO subject : bachelorSubjects) {
                descriptions.append(subject.getBDescription()).append(", ");
            }
            descriptions.delete(descriptions.length() - 2, descriptions.length()); // Remove the trailing comma and space
            return descriptions.toString();
        } else {
            return "bisher keine Angaben";
        }
    }


    /**
     * Gibt die Titel der Bachelor-Themen als String zurück.
     *
     * @param bachelorSubjects Die Liste der Bachelor-Themen.
     * @return Die Titel der Bachelor-Themen als String.
     */
    private String getBachelorSubjectsTitles(List<BachelorSubjectDTO> bachelorSubjects) {
        if (bachelorSubjects != null && !bachelorSubjects.isEmpty()) {
            StringBuilder titles = new StringBuilder();
            for (BachelorSubjectDTO subject : bachelorSubjects) {
                titles.append(subject.getTitle()).append(", ");
            }
            titles.delete(titles.length() - 2, titles.length()); // Remove the trailing comma and space
            return titles.toString();
        } else {
            return "bisher keine Angaben";
        }
    }

    /**
     * Setzt alle Formularfelder auf ihren urspruenglichen Zustand zurueck.
     * Diese Methode setzt die Werte aller Formularfelder auf ihre urspruenglichen Werte zurueck,
     * die beim ersten Laden der Ansicht erfasst wurden.
     */
    private void resetToInitialState() {
        firstName.setValue(initialFirstName);
        lastName.setValue(initialLastName);
        company.setValue(initialCompany);
        startDatePicker.setValue(initialStartDate);
        endDatePicker.setValue(initialEndDate);
        description.setValue(initialDescription);
        specialFields.setValue(initialSpecialFields);
        bachelorSubjectTitle.setValue(initialBachelorSubjectTitle);
        bDescription.setValue(initialBachelorSubjectDescription);
    }

}


