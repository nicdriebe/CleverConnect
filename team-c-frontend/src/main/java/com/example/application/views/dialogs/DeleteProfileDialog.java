package com.example.application.views.dialogs;

import com.example.application.services.ConsumeREST;
import com.example.application.services.ConsumeREST_External;
import com.example.application.services.ConsumeREST_Login;
import com.example.application.views.startseite.StartseiteView;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;

/**
 * Die Klasse DeleteProfileDialog repraesentiert ein Dialogfenster,
 * das es dem User erlaubt, sein Profil zu loeschen.
 */

public class DeleteProfileDialog {
    private final Dialog dialog;
    private final ConsumeREST_External service;
    private final ConsumeREST_Login userService;
    private final ConsumeREST users;
    private Long userId; // ID des zu löschenden Benutzers

    public DeleteProfileDialog(ConsumeREST_External service, ConsumeREST_Login userService, ConsumeREST users) {
        this.service = service;
        this.userService = userService;
        this.users = users;
        this.dialog = new Dialog();

        dialog.setHeaderTitle("Profil löschen");

        Span messageLabel = new Span("Sind Sie sicher, dass Sie Ihr Profil löschen möchten?");

        dialog.add(messageLabel);

        Button confirmButton = new Button("Bestätigen", event -> {
            users.deleteUserById(userId);
            userService.logoutUser(); // Logout des Users
            dialog.close();
            Notification.show("Profil erfolgreich gelöscht.", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            UI.getCurrent().navigate(StartseiteView.class);
        });
        confirmButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        Button cancelButton = new Button("Abbrechen", event ->
            dialog.close());
        cancelButton.getStyle().set("margin-inline-end", "auto");

        dialog.getFooter().add(cancelButton, confirmButton);
    }

    /**
     * Öffnet den Dialog.
     * @param userId - ID des zu löschenden Benutzers
     */
    public void open(Long userId) {
        this.userId = userId;
        dialog.open();
    }

}

