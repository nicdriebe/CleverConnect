package com.example.application.views.hauptseite;

import com.example.application.entities.LoginUser;
import com.example.application.entities.Role;
import com.example.application.services.ConsumeREST;
import com.example.application.services.ConsumeREST_External;
import com.example.application.services.ConsumeREST_Login;
import com.example.application.views.inhaltfooter.FooterLayout;
import com.example.application.views.profil.PasswordChangeDialog;
import com.example.application.views.startseite.StartseiteView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.provider.ListDataProvider;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinSession;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Die Klasse HauptseiteViewProfs repraesentiert die Startseite,
 * die von Professorinnen/Administratorinnen gesehen wird,
 * wenn sie sich einloggen, um Benutzerinnen auf der Seite zu betrachten und zu loeschen.
 */
@Route(value = "main-page-prof", layout = FooterLayout.class)
public class HauptseiteViewProfs extends VerticalLayout implements BeforeEnterObserver {
    private final ConsumeREST users;
    private final ConsumeREST_Login userService;
    private final ConsumeREST_External service;

    private Grid<LoginUser> listViewUser;

    /**
     * Konstruktor fuer das HauptseiteViewProfs-Objekt.
     *
     * @param users       Der Service für Benutzerdaten.
     * @param userService Der Service für Benutzerlogin/-logout.
     * @param service     Der Service für externe Benutzerdaten.
     */
    public HauptseiteViewProfs(ConsumeREST users, ConsumeREST_Login userService, ConsumeREST_External service) {
        this.users = users;
        this.userService = userService;
        this.service = service;


        H2 titel = new H2("Übersichtsliste aller Zweitbetreuer*innen");

        // Dialog für Passwortänderung
        PasswordChangeDialog passwordChangeDialog = new PasswordChangeDialog(userService);
        Button changePwdButton = new Button("Passwort ändern");
        changePwdButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        changePwdButton.addClickListener(e -> passwordChangeDialog.open());


        Button logoutButton = new Button("Logout", e -> handleLogout());

        // Dialog für Registrierung
        PopUpRegistration popUpRegistration = new PopUpRegistration(service);

        Button newUserButton = new Button("neue Userin anlegen", e -> {
            popUpRegistration.getDialog().open();
            popUpRegistration.getDialog().addOpenedChangeListener(event -> {
                if (!event.isOpened()) {
                    refreshGrid(listViewUser);
                }
            });
        });

        newUserButton.addThemeVariants(ButtonVariant.LUMO_CONTRAST);

        HorizontalLayout buttonLayout = new HorizontalLayout(changePwdButton, logoutButton);
        buttonLayout.setSpacing(true); // Add spacing between buttons

        add(buttonLayout, titel, newUserButton);


        setHorizontalComponentAlignment(Alignment.END, buttonLayout, newUserButton);
        GridContentAllUser();
    }

    /**
     * Erstellt den Inhalt der Grid-Komponente für alle Benutzerinnen.
     */
    private void GridContentAllUser() {
        listViewUser = new Grid<>(LoginUser.class, false);

        listViewUser.addColumn(LoginUser::getFirstName).setHeader("Vorname");
        listViewUser.addColumn(LoginUser::getLastName).setHeader("Nachname").setSortable(true);
        listViewUser.addColumn(LoginUser::getEmail).setHeader("Email");
        listViewUser.addColumn(LoginUser::getRegistrationDate).setHeader("Registrierungsdatum").setSortable(true);
        listViewUser.setMultiSort(true, Grid.MultiSortPriority.APPEND);

        listViewUser.addColumn(new ComponentRenderer<>(externalDTO -> {
            Dialog confirm = new Dialog();
            confirm.setHeaderTitle("Benutzer löschen");
            confirm.add("Sicher dass Sie Benutzer*in " + externalDTO.getFirstName().toUpperCase() + " "
                    + externalDTO.getLastName().toUpperCase() + " wirklich löschen möchten?");

            Button cancel = new Button("Abbrechen", clickevent2 -> confirm.close());

            Button finaleDelete = new Button("Löschen", clickevent1 -> {
                users.deleteUserById(externalDTO.getId());
                confirm.close();
                // Grid neu laden nach Löschen einer Benutzerin
                refreshGrid(listViewUser);

                String deletedUserName = externalDTO.getFirstName().toUpperCase() + " " + externalDTO.getLastName().toUpperCase();
                Notification.show("Benutzer*in " + deletedUserName + " wurde gelöscht.", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            });
            finaleDelete.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
            cancel.getStyle().set("margin-inline-end", "auto");
            confirm.getFooter().add(cancel, finaleDelete);

            Button deleteButton = new Button("Löschen", clickevent -> {confirm.open();});
            deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);

            return deleteButton;
        })).setHeader(" ");

        List<LoginUser> allUser = users.loadUser();

        ListDataProvider<LoginUser> dataProvider = new ListDataProvider<>(getExternUsers(allUser));
        listViewUser.setDataProvider(dataProvider);

        listViewUser.setItems(getExternUsers(allUser));
        add(listViewUser);
    }

    /**
     * Filtert externe Benutzer aus der Liste aller Benutzerinnen.
     *
     * @param users Die Liste aller Benutzerinenn.
     * @return Eine gefilterte Liste von externen Benutzerinnen.
     */
    private List<LoginUser> getExternUsers(List<LoginUser> users) {
        return users.stream()
                .filter(user -> Role.EXTERN.equals(user.getRole()))
                .collect(Collectors.toList());
    }

    /**
     * Aktualisiert die Grid-Komponente nach dem Loeschen einer Benutzerin.
     *
     * @param grid Die Grid-Komponente.
     */
    private void refreshGrid(Grid<LoginUser> grid) {
        List<LoginUser> updatedList = users.loadUser();
        listViewUser.getDataProvider().refreshAll();
        listViewUser.setItems(getExternUsers(updatedList));
    }

    /**
     * Vor dem Betreten der Hauptseite ueberpruefen, ob die Benutzerin eingeloggt ist.
     *
     * @param event Das BeforeEnterEvent.
     */
    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        // Überprüfen, ob der Benutzer eingeloggt ist
        if (VaadinSession.getCurrent().getAttribute("jwtToken") == null) {
            // Nicht eingeloggt, Umleitung zur Startseite
            event.forwardTo(StartseiteView.class);
        }
    }

    /**
     * Behandelt den Logout-Vorgang.
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

}
