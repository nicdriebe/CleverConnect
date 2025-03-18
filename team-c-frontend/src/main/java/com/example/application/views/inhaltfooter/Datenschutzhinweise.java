package com.example.application.views.inhaltfooter;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;

/**
 * Die Klasse Datenschutzhinweise repraesentiert eine Ansicht fuer Datenschutzhinweise
 * und informiert die Benutzerinnen ueber den Umgang mit ihren personenbezogenen Daten auf der Webseite.
 */
@Route(value = "datenschutz", layout = FooterLayout.class)
public class Datenschutzhinweise extends VerticalLayout {

    /**
     * Konstruktor fuer das Datenschutzhinweise-Objekt.
     * Hier werden Informationen zu Datenschutzhinweisen fuer die Benutzerinnen dargestellt.
     */
    public Datenschutzhinweise() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        getStyle().set("padding", "0% 5cm 30% 5cm");

        add(new H2("Datenschutzhinweise"));

        add(new Paragraph("Vielen Dank für Ihr Interesse an unserer Hochschule. "+
                "Der Schutz Ihrer Daten ist uns ein wichtiges Anliegen. " +
                "Deshalb verarbeiten wir die Daten, die Sie beim Besuch auf unseren Webseiten hinterlassen, " +
                "nur nach den Vorgaben der relevanten datenschutzrechtlichen Bestimmungen, " +
                "insbesondere der Datenschutzgrundverordnung und des Bundesdatenschutzgesetzes. " +
                "An dieser Stelle möchten wir Sie über Art, Umfang und Zweck der Verarbeitung Ihrer personenbezogenen Daten informieren."));

        Button backButton = new Button("Zurück", event -> {
            getUI().ifPresent(ui -> ui.getPage().getHistory().back());
        });

        add(backButton);
    }
}