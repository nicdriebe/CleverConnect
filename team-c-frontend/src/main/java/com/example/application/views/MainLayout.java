package com.example.application.views;

import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;

/**
 * Die Klasse MainLayout repraesentiert das Hauptlayout der Anwendung.
 * Es erweitert das Vaadin AppLayout und definiert die Struktur der Oberflaeche,
 * einschließlich der Navigationsleiste und des Seiteninhalts.
 */
public class MainLayout extends AppLayout {

    /**
     * Konstruktor fuer das Hauptlayout.
     * Initialisiert die Navigationsleiste mit einem Logo und Tabs für verschiedene Abschnitte
     */
    public MainLayout() {

        Image logo = new Image("themes/batchmatch1/views/bild_header.png", "HTW Logo");
        logo.setHeight("50px");
        final boolean touchOptimized = true;
        addToNavbar(touchOptimized, new DrawerToggle(), logo);

        Tabs tabs = new Tabs(new Tab("ABOUT"),
                new Tab("FAQ"),
                new Tab("KONTAKT"));

        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        tabs.addThemeVariants(TabsVariant.LUMO_CENTERED);
        tabs.setSizeFull();

        setDrawerOpened(false);
        addToDrawer(tabs);
    }

}

