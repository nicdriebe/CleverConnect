package com.example.application.services;

import com.google.common.eventbus.Subscribe;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class MyUIComponent extends VerticalLayout {
    public MyUIComponent() {
        MyEventBus.getInstance().register(this);
    }

    @Subscribe
    public void onErrorEvent(ErrorEvent event) {
        UI.getCurrent().access(() -> {
            Notification.show(event.getMessage(), 5000, Notification.Position.TOP_CENTER)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        });
    }
}