package com.example.application.views.profil;

import com.example.application.dto.BachelorSubjectDTO;
import com.example.application.dto.ExternalDTO;

import com.example.application.dto.SpecialFieldDTO;
import com.example.application.services.ConsumeREST_External;
import com.example.application.services.ConsumeREST_Login;
import com.example.application.views.hauptseite.HauptseiteView;
import com.example.application.views.inhaltfooter.FooterLayout;

import com.example.application.views.startseite.StartseiteView;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;


import com.vaadin.flow.component.notification.Notification;

import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;


import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Set;
import java.util.List;

/**
 * Diese Klasse stellt die Detailansicht eines externen Benutzers dar.
 */
@PageTitle("Profile Detail")
@Route(value = "profile_detail", layout = FooterLayout.class)
public class ProfilDetailView extends Composite<VerticalLayout> implements HasUrlParameter<String>, BeforeEnterObserver  {
    private final ConsumeREST_External externalService;
    private final ConsumeREST_Login userService;

    private final TextField name;
    private final TextField email;
    private final TextField company;
    private final TextField availability;
    private final TextArea specialFields;
    private final TextArea bachelorSubjects;
    private final TextArea description;

    private H3 h3;

    /**
     * Konstruktor, in dem die UI-Elemente initialisiert und konfiguriert werden.
     * @param externalService - Service für externe Benutzer
     * @param userService - Service für Benutzer
     */
    public ProfilDetailView(ConsumeREST_External externalService, ConsumeREST_Login userService) {
        this.externalService = externalService;
        this.userService = userService;

        Button logoutButton = new Button("Logout", e -> handleLogout());
        Button backButton = new Button ("Zurück zur Übersicht", e -> navigateToHauptseiteView());

        VerticalLayout content = new VerticalLayout();
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        content.addClassName(LumoUtility.Gap.SMALL);
        content.setWidth("800px");
        content.setMaxWidth("800px");
        content.getStyle().set("flex-grow", "1").set("margin-bottom", "10%");
        content.setAlignItems(FlexComponent.Alignment.CENTER);
        content.setMaxWidth("100%"); // Adjusting max width for responsiveness
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        HorizontalLayout headerLayout = new HorizontalLayout();
        h3 = new H3();
        h3.setText("Profil von ");
        headerLayout.add( h3);
        content.add(headerLayout);

        FormLayout externalDetails = new FormLayout();
        name = new TextField("Name");
        name.setReadOnly(true);
        email = new TextField("E-Mail");
        email.setReadOnly(true);
        company = new TextField("Firma");
        company.setReadOnly(true);
        availability = new TextField("Verfügbarkeit");
        availability.setReadOnly(true);
        specialFields = new TextArea("Fachgebiet(e)");
        specialFields.setReadOnly(true);
        specialFields.getStyle().set("maxHeight", "500px");

        bachelorSubjects = new TextArea("Bachelorthema");
        bachelorSubjects.setReadOnly(true);
        bachelorSubjects.getStyle().set("maxHeight", "1000px");

        description = new TextArea("ich bin hier, weil...");
        description.setReadOnly(true);
        description.getStyle().set("maxHeight", "1000px");

        VerticalLayout bButton = new VerticalLayout();
        bButton.add(backButton);

        content.add(externalDetails);
        externalDetails.add(name, email, company,  availability, specialFields , bachelorSubjects, description, bButton);

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        getContent().setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        getContent().setAlignItems(FlexComponent.Alignment.CENTER);
        getContent().setAlignSelf(FlexComponent.Alignment.END, logoutButton);

        getContent().add(logoutButton);
        getContent().add(content);
    }

    /**
     * Diese Methode wird aufgerufen, wenn die URL der View geändert wird.
     * @param event - Event, das die Parameterübergabe ermöglicht
     * @param parameter - Parameter, der in der URL übergeben wird
     *                  (in diesem Fall die ID des externen Benutzers)
     */
    @Override
    public void setParameter(BeforeEvent event, @OptionalParameter String parameter) {
        if (parameter != null && !parameter.isEmpty()) {
            Long externalId = Long.parseLong(parameter);

            ExternalDTO external = externalService.loadExternalById(externalId);

            if (external != null) {
                name.setValue(external.getFirstName() + " " + external.getLastName());
                email.setValue( external.getEmail());
                company.setValue(external.getCompany());
                // Abrufen der Beschreibung
                String descriptionText = external.getDescription();
                // Überprüfen, ob die Beschreibung null ist
                if (descriptionText == null) {
                    descriptionText = "";
                }
                // Setzen der Beschreibung im TextArea-Feld
                description.setValue(descriptionText);

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
                LocalDate startDate = external.getAvailabilityStart();
                String formattedStartDate = startDate != null ? startDate.format(dateFormatter) : "";
                LocalDate endDate = external.getAvailabilityEnd();
                String formattedEndDate = endDate != null ? endDate.format(dateFormatter) : "";
                availability.setValue(formattedStartDate + " - " + formattedEndDate);

                Set<SpecialFieldDTO> externalSpecialFields = external.getSpecialFields();
                if (externalSpecialFields != null && !externalSpecialFields.isEmpty()) {
                    StringBuilder specialFieldNames = new StringBuilder(" ");
                    for (SpecialFieldDTO specialField : externalSpecialFields) {
                        specialFieldNames.append(specialField.getName()).append(", ");
                    }
                    specialFieldNames.delete(specialFieldNames.length() - 2, specialFieldNames.length()); // Remove the trailing comma
                    specialFields.setValue(specialFieldNames.toString());
                } else {
                    specialFields.setValue("bisher keine Angabe");
                }

                List<BachelorSubjectDTO> externalBachelorSubjects = external.getBachelorSubjects();
                if (externalBachelorSubjects != null && !externalBachelorSubjects.isEmpty()) {
                    StringBuilder bachelorSubjectsText = new StringBuilder(" ");
                    for (BachelorSubjectDTO bachelorSubject : externalBachelorSubjects) {
                        bachelorSubjectsText.append(bachelorSubject.getTitle())
                                .append(" - ")
                                .append(bachelorSubject.getBDescription())
                                .append(", ");
                    }
                    bachelorSubjectsText.delete(bachelorSubjectsText.length() - 2, bachelorSubjectsText.length()); // Remove the trailing comma
                    bachelorSubjects.setValue(bachelorSubjectsText.toString());
                } else {
                    bachelorSubjects.setValue("bisher keine Angabe");
                }

                h3.setText("Profil von " + external.getFirstName() + " " + external.getLastName());

            } else {

                Notification.show("Error: External user not found");
            }
        } else {

            Notification.show("Error: Invalid parameter");
        }
    }


    /**
     * Diese Methode wird aufgerufen, wenn der Benutzer auf den Logout-Button klickt.
     * Sie leitet den Benutzer zur Startseite weiter, wenn der Logout erfolgreich war.
     */
    private void handleLogout() {
        boolean logoutSuccess = userService.logoutUser();
        if (logoutSuccess) {
            // Erfolgreiches Logout
            Notification.show("Sie sind ausgeloggt", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);;
            UI.getCurrent().navigate(StartseiteView.class);
        } else {
            // Fehler beim Logout
            Notification.show("Fehler beim Logout", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * Diese Methode leitet den Benutzer zur Hauptseite weiter.
     */
    private void navigateToHauptseiteView() {
        UI.getCurrent().navigate(HauptseiteView.class);
    }

    /**
     * Diese Methode wird aufgerufen, bevor die View betreten wird.
     * @param event - Event, das die Navigation ermöglicht
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Überprüfen, ob der Benutzer eingeloggt ist
        if (VaadinSession.getCurrent().getAttribute("jwtToken") == null) {
            // Nicht eingeloggt, Umleitung zur Startseite
            event.forwardTo(StartseiteView.class);
        }
    }

}


