package com.example.application.views.erfolgreicheregistrierung;

import com.example.application.dto.LoginResponse;
import com.example.application.entities.Role;
import com.example.application.services.ConsumeREST_Login;
import com.example.application.views.hauptseite.HauptseiteView;
import com.example.application.views.hauptseite.HauptseiteViewProfs;
import com.example.application.views.inhaltfooter.FooterLayout;
import com.example.application.views.profil.ProfilView;

import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;
import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Die Klasse ErfolgreicheRegistrierungView repraesentiert die Ansicht nach einer erfolgreichen Registrierung.
 * Hier kann sich die Benutzerin anmelden und auf die Hauptseite der Anwendung navigieren.
 */
@PageTitle("Erfolgreiche Registrierung")
@Route(value = "registered", layout = FooterLayout.class)
@Uses(Icon.class)
public class ErfolgreicheRegistrierungView extends Composite<VerticalLayout> {

    private final ConsumeREST_Login service;
    private TextField email;
    private PasswordField password;
    private Role role;

    /**
     * Konstruktor fuer die ErfolgreicheRegistrierungView.
     *
     * @param service Der Service für den Benutzerlogin/-logout.
     */
    public ErfolgreicheRegistrierungView(ConsumeREST_Login service) {
        this.service = service;
        VerticalLayout mainLayout = new VerticalLayout();

        Image logo = new Image("themes/batchmatch1/views/Logo.png", "CC");
        logo.setHeight("200px");
        mainLayout.add(logo);

        VerticalLayout layoutColumn2 = new VerticalLayout();
        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");

        H3 successMessage = new H3("Registrierung erfolgreich abgeschlossen!");
        successMessage.setWidth("max-content");

        layoutColumn2.setWidth("100%");
        layoutColumn2.getStyle().set("flex-grow", "1");

        Paragraph message = new Paragraph("Starte jetzt und finde deinen wissenschaftlichen Begleiter für eine erfolgreiche Zusammenarbeit.");
        message.setWidth("100%");
        message.getStyle().set("font-size", "var(--lumo-font-size-m)");
        message.getStyle().set("text-align", "center");

        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        mainLayout.setSizeFull();

        VerticalLayout verticalLayout = new VerticalLayout();
        VerticalLayout textFieldsLayout = new VerticalLayout();

        layoutColumn2.add(successMessage, message);
        layoutColumn2.setAlignItems(FlexComponent.Alignment.CENTER);

        // Anmeldeformular
        email = new TextField("E-Mail Adresse");
        password = new PasswordField("Passwort");
        Button anmeldenButton = new Button("Anmelden");
        anmeldenButton.addClickShortcut(Key.ENTER);

        // Layout der Anmeldefelder
        textFieldsLayout.add(
                logo,
                email,
                password,
                anmeldenButton
        );
        textFieldsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        textFieldsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);

        // Event-Listener: Benutzerin wird je nach Rolle an eine andere Seite weitergeleitet
        anmeldenButton.addClickListener(e -> {
            try {
                LoginResponse loginResponse = service.loginUser(email.getValue(), password.getValue());
                String token = loginResponse.getToken();
                role = loginResponse.getRole();

                if (token != null && role != null) {
                    Long userId = loginResponse.getId();

                    if (userId != null) {
                        if (role == Role.EXTERN) {
                            // Navigate to ProfilView with userId parameter
                            UI.getCurrent().navigate(ProfilView.class, new RouteParameters("userId", userId.toString()));
                        } else if (role == Role.STUDENT) {
                            UI.getCurrent().navigate(HauptseiteView.class);
                        } else if (role == Role.ADMIN) {
                            UI.getCurrent().navigate(HauptseiteViewProfs.class);
                        }
                    } else {
                        // Handle the case where userId is null
                        Notification.show("Benutzer-ID konnte nicht abgerufen werden",
                                        3000, Notification.Position.MIDDLE)
                                .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    }

                }
            } catch (WebClientResponseException.Unauthorized ex) {
                // Anmeldung fehlgeschlagen
                Notification.show("Anmeldung fehlgeschlagen. Überprüfen Sie Ihre E-Mail und Ihr Passwort.", 3000, Notification.Position.MIDDLE ).addThemeVariants(NotificationVariant.LUMO_ERROR);

            } catch (Exception ex) {
                // Logge den Fehler
                ex.printStackTrace();
                Notification.show("Ein Fehler ist aufgetreten. Bitte versuchen Sie es erneut.", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        // Hinzufügen aller Elemente zum Hauptlayout
        getContent().add(textFieldsLayout); // Login-Formular wird hinzugefügt
        getContent().add(verticalLayout);

        verticalLayout.add(
                layoutColumn2,
                textFieldsLayout
        );
    }

}


