package com.example.application.views.startseite;

import com.example.application.dto.LoginResponse;
import com.example.application.entities.Role;
import com.example.application.services.ConsumeREST_Login;
import com.example.application.views.hauptseite.HauptseiteView;
import com.example.application.views.hauptseite.HauptseiteViewProfs;
import com.example.application.views.inhaltfooter.FooterLayout;
import com.example.application.views.profil.ProfilView;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import com.vaadin.flow.component.Composite;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.server.VaadinSession;
import com.vaadin.flow.theme.lumo.LumoUtility.Gap;
import com.vaadin.flow.theme.lumo.LumoUtility.Padding;
import com.vaadin.flow.router.RouteParameters;

import org.springframework.web.reactive.function.client.WebClientResponseException;

/**
 * Die Klasse StartseiteView repraesentiert die Startseite der Anwendung.
 * Hier können Benutzerinnen sich anmelden oder registrieren.
 */
@PageTitle("Startseite")
@Route(value = "startseite", layout = FooterLayout.class)
@RouteAlias(value = "", layout = FooterLayout.class)
@Uses(Icon.class)
public class StartseiteView extends Composite<VerticalLayout> {

    private final ConsumeREST_Login service;
    private TextField email;
    private PasswordField password;
    private Button registrationButton;
    private Role role;

    /**
     * Konstruktor fuer die StartseiteView.
     * @param service Der Service fuer den Benutzerlogin/-logout.
     */
    public StartseiteView(ConsumeREST_Login service) {
        this.service = service;

        VerticalLayout mainLayout = new VerticalLayout();
        mainLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        mainLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        mainLayout.setSizeFull();

        Image logo = new Image("themes/batchmatch1/views/Logo.png", "CC");
        logo.setHeight("200px");
        mainLayout.add(logo);
        mainLayout.setPadding(true);
        mainLayout.getStyle().set("padding-top", "5%");

        Paragraph textSmall2 = new Paragraph();

        HorizontalLayout layoutRow = new HorizontalLayout();
        VerticalLayout verticalLayout = new VerticalLayout();
        VerticalLayout textFieldsLayout = new VerticalLayout();

        getContent().setWidth("100%");
        getContent().getStyle().set("flex-grow", "1");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("95px");
        layoutRow.setJustifyContentMode(FlexComponent.JustifyContentMode.START);
        layoutRow.setAlignItems(FlexComponent.Alignment.CENTER);
        textFieldsLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        textFieldsLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        verticalLayout.addClassName(Gap.XSMALL);
        verticalLayout.addClassName(Padding.XSMALL);
        verticalLayout.setWidth("100%");
        verticalLayout.getStyle().set("flex-grow", "1");
        layoutRow.addClassName(Gap.MEDIUM);
        layoutRow.setWidth("100%");
        layoutRow.setHeight("min-content");

        email = new TextField("E-Mail Adresse");
        password = new PasswordField("Passwort");
        Button anmeldenButton = new Button("Anmelden");
        anmeldenButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        anmeldenButton.addClickShortcut(Key.ENTER);

        textFieldsLayout.add(
                email,
                password,
                anmeldenButton
        );

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
                Notification.show("Anmeldung fehlgeschlagen. Überprüfen Sie Ihre E-Mail und Ihr Passwort.", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            } catch (Exception ex) {
                // Log the error
                ex.printStackTrace();
                // Show a notification
                Notification.show("Ein Fehler ist aufgetreten. Bitte versuchen Sie es erneut.", 3000, Notification.Position.MIDDLE).addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });

        textSmall2.setText("Noch kein Account?");
        textSmall2.setWidth("100%");
        textSmall2.getStyle().set("font-size", "var(--lumo-font-size-s)");
        textSmall2.getElement().getStyle().set("text-align", "center");

        // Falls, noch kein Konto existiert:
        registrationButton = new Button("Hier registrieren");

        Dialog pop_up = new Dialog();
        Button studies = new Button("Studierende");
        Button externe = new Button("Externe");
        VerticalLayout pop_upLayout = createDialogLayout();
        pop_up.add(pop_upLayout);
        pop_up.getFooter().add(studies);
        pop_up.getFooter().add(externe);
        studies.addClickListener(e ->studies.getUI().ifPresent(ui-> ui.navigate("person-form2")));
        externe.addClickListener(e ->externe.getUI().ifPresent(ui-> ui.navigate("person-form")));

        // Text und Registrierungsbutton
        VerticalLayout textAndRegistrationLayout = new VerticalLayout();
        textAndRegistrationLayout.add(textSmall2, registrationButton);
        textAndRegistrationLayout.setSpacing(true);
        textAndRegistrationLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.AROUND);
        textAndRegistrationLayout.setAlignItems(FlexComponent.Alignment.CENTER);

        registrationButton.addClickListener(e-> pop_up.open()); // Popup öffnet sich

        layoutRow.add(textAndRegistrationLayout);
        layoutRow.setAlignItems(FlexComponent.Alignment.CENTER);

        // Hinzufügen aller Elemente zum Hauptlayout
        getContent().add(
                mainLayout,
                layoutRow,
                pop_up,
                textAndRegistrationLayout,
                textFieldsLayout,
                verticalLayout
        );

        verticalLayout.add(
                layoutRow,
                textFieldsLayout,
                textAndRegistrationLayout
        );
    }

    /**
     * Erstellt das Layout fuer das Registrierungs-Popup.
     * @return Das VerticalLayout des Registrierungs-Popups.
     */
    private static VerticalLayout createDialogLayout() {

        H2 headline = new H2("Registrierung");
        headline.getStyle().set("margin", "var(--lumo-space-m) 0")
                .set("font-size", "1.5em").set("font-weight", "bold");
        Paragraph paragraph = new Paragraph();
        paragraph.setText("Wählen Sie aus ob Sie sich als Student*in oder Zweitbetreuer*in registrieren möchten");

        VerticalLayout dialogLayout = new VerticalLayout(headline, paragraph);
        dialogLayout.setPadding(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "300px").set("max-width", "100%");
        dialogLayout.setAlignSelf(FlexComponent.Alignment.END);

        return dialogLayout;
    }

}

