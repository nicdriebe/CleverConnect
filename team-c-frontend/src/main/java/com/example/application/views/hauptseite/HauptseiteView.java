package com.example.application.views.hauptseite;

import com.example.application.dto.BachelorSubjectDTO;
import com.example.application.dto.ExternalDTO;
import com.example.application.dto.SpecialFieldDTO;
import com.example.application.services.ConsumeREST;
import com.example.application.services.ConsumeREST_External;
import com.example.application.services.ConsumeREST_Login;
import com.example.application.views.dialogs.DeleteProfileDialog;
import com.example.application.views.inhaltfooter.FooterLayout;
import com.example.application.views.profil.PasswordChangeDialog;
import com.example.application.views.profil.ProfilDetailView;
import com.example.application.views.startseite.StartseiteView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Set;

/**
 * Die Klasse HauptseiteView repraesentiert die Hauptseite der Anwendung fuer Benutzerinnen (Studierende) nach dem Login.
 * Hier koennen Benutzerinnen Zweitbetreuerinnen fuer ihre Bachelorarbeiten suchen und kontaktieren.
 */
@Route(value = "main-page", layout = FooterLayout.class)
public class HauptseiteView extends VerticalLayout implements BeforeEnterObserver {
    private final ConsumeREST_External service;
    private final ConsumeREST_Login userService;
    private final ConsumeREST users;

    /**
     * Konstruktor fuer die HauptseiteView.
     *
     * @param service     Der Service fuer externe Benutzerdaten.
     * @param userService Der Service fuer den Benutzerlogin/-logout.
     * @param users      Der Service fuer Benutzerdaten.
     */
    public HauptseiteView(ConsumeREST_External service, ConsumeREST_Login userService, ConsumeREST users) {
        this.service = service;
        this.userService = userService;
        this.users = users;

        Button logoutButton = new Button("Logout", e -> handleLogout());


        // Dialog zum Ändern des Passworts
        PasswordChangeDialog passwordChangeDialog = new PasswordChangeDialog(userService);

        Button changePwdButton = new Button("Passwort ändern");
        changePwdButton.addClickListener(e -> passwordChangeDialog.open());
        changePwdButton.getStyle().set("color", "black");


        // Dialog zum Löschen des Profils
        DeleteProfileDialog deleteProfileDialog = new DeleteProfileDialog(service, userService, this.users);

        Button deleteProfileButton = new Button("Profil löschen");
        deleteProfileButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteProfileButton.addClickListener(e -> {
            Long userId = (Long) VaadinSession.getCurrent().getAttribute("id");
            if(userId != null) {
                deleteProfileDialog.open(userId);
            }
        });

        HorizontalLayout buttonLayout = new HorizontalLayout(changePwdButton, deleteProfileButton, logoutButton);
        buttonLayout.setSpacing(true);

        add(buttonLayout);

        setHorizontalComponentAlignment(Alignment.END, buttonLayout);


        add(new H2("Willkommen"), new Paragraph("Hier findest du die richtige Zweitbetreuung für deine Bachelorarbeit!"));

        Div centerDiv = new Div();
        centerDiv.setSizeFull();

        // Inhalte der Hauptseite (Grid mit Zweitbetreuerinnen)
        GridContent();
    }

    /**
     * Vor dem Betreten der Hauptseite ueberpruefen, ob die Benutzerin eingeloggt ist.
     *
     * @param event Das BeforeEnterEvent.
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event){
        // Überprüfen, ob der Benutzer eingeloggt ist
        if (VaadinSession.getCurrent().getAttribute("jwtToken") == null) {
            // Nicht eingeloggt, Umleitung zur Startseite
            event.forwardTo(StartseiteView.class);
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
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);;
            UI.getCurrent().navigate(StartseiteView.class);
        } else {
            // Fehler beim Logout
            Notification.show("Fehler beim Logout", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    /**
     * Erstellt und füllt das Grid mit Zweitbetreuerinnen.
     */
    private void GridContent()
    {
        Grid<ExternalDTO> listView = new Grid<>(ExternalDTO.class, false);

        listView.addColumn(new ComponentRenderer<>(externalDTO -> {
            List<BachelorSubjectDTO> subjects = externalDTO.getBachelorSubjects();
            if (subjects != null && !subjects.isEmpty()) {
                VerticalLayout layout = new VerticalLayout();
                //layout.getStyle().set("overflow", "auto"); // overflow -> Inhalt scrollbar machen
                subjects.forEach(subject -> {
                    Paragraph titleParagraph = new Paragraph(subject.getTitle());
                    titleParagraph.getStyle().set("white-space", "normal");
                    titleParagraph.getStyle().set("font-weight", "bold");
                    layout.add(titleParagraph);
                });
                return layout;
            } else {
                return new Span(" ");
            }
        })).setHeader("Bachelorthema").setFlexGrow(2);

        listView.addColumn(new ComponentRenderer<>(externalDTO -> {
            Set<SpecialFieldDTO> specialFields = externalDTO.getSpecialFields();
            if (specialFields != null && !specialFields.isEmpty()) {
                VerticalLayout layout = new VerticalLayout();
                specialFields.forEach(specialField -> {
                    Paragraph specialParagraph = new Paragraph(specialField.getName());
                    specialParagraph.getStyle().set("white-space", "normal");
                    layout.add(specialParagraph);
                });
                return layout;
            } else {
                return new Span(" ");
            }
        })).setHeader("Fachgebiete").setFlexGrow(2);

        listView.addColumn(new ComponentRenderer<>(externalDTO -> {
            HorizontalLayout layout = new HorizontalLayout();
            //layout.setMaxWidth("15%");
            layout.add(new Span(externalDTO.getFirstName()));
            layout.add(new Span(externalDTO.getLastName()));

            return layout;
        })).setHeader("Name");

        listView.addColumn(new ComponentRenderer<>(externalDTO -> {
            String availabilityStart = formatDate(String.valueOf(externalDTO.getAvailabilityStart()));
            String availabilityEnd = formatDate(String.valueOf(externalDTO.getAvailabilityEnd()));

            VerticalLayout layout = new VerticalLayout();
            if (availabilityStart != null && !availabilityStart.isEmpty()) {
                layout.add(new Span("von: " + availabilityStart));
            }
            if (availabilityEnd != null && !availabilityEnd.isEmpty()) {
                layout.add(new Span("bis : " + availabilityEnd));
            }

            return layout;
        })).setHeader("Verfügbarkeit");

        listView.addColumn(new ComponentRenderer<>(externalDTO -> {
            VerticalLayout buttonLayout = new VerticalLayout();
            Long userId = externalDTO.getId();

            Button moreInfoButton = new Button("mehr Infos", clickEvent -> {
                ProfilDetailView profilDetailView = new ProfilDetailView(service, userService);

                // Benutzerdaten per ID mit Service laden
                ExternalDTO external = service.loadExternalById(userId);

                // Weiterleitung zu ProfilDetailView
                UI.getCurrent().navigate(ProfilDetailView.class, userId.toString());
            });

            moreInfoButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
            moreInfoButton.setWidth("100%");
            buttonLayout.add(moreInfoButton);

            Button emailButton = new Button("Email senden", event -> {
                // JavaScript benutzen um Default Mail Client zu oeffnen
                UI.getCurrent().getPage().executeJs("window.location.href = 'mailto:" + externalDTO.getEmail() + "';");
            });
            emailButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS, ButtonVariant.LUMO_PRIMARY);
            emailButton.setWidth("100%");
            buttonLayout.add(emailButton);

            return buttonLayout;
        })).setHeader("");

        listView.setWidth("95%");

        List<ExternalDTO> externals = service.loadExternals();
        listView.setItems(externals);

        add(listView);
    }

    /**
     * Formatiert das Datum im String-Format.
     *
     * @param dateString Der zu formatierende Datum-String.
     * @return Das formatierte Datum im String-Format.
     */
    private String formatDate(String dateString) {
        if (dateString == null || "null".equals(dateString)) {
            return "";
        }
        try {
            LocalDate date = LocalDate.parse(dateString);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            return date.format(formatter);
        } catch (DateTimeParseException e) {
            return "";
        }
    }

}
