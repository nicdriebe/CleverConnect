package com.example.application.views.inhaltfooter;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.Route;

/**
 * Die Klasse Impressum repraesentiert die Impressumsseite der Webseite.
 */
@Route(value = "impressum", layout = FooterLayout.class)
public class Impressum extends VerticalLayout {

    /**
     * Konstruktor für das Impressum-Objekt.
     * Hier werden strukturierte Informationen zum Impressum hinzugefuegt.
     */
    public Impressum() {
        setSizeFull();
        setJustifyContentMode(JustifyContentMode.CENTER);
        setAlignItems(Alignment.CENTER);
        getStyle().set("padding", "15% 0 25% 0");

        H2 header = new H2("Impressum");
        header.getElement().getStyle().set("align-self", "center");
        add(header);

        Html html = new Html("<p style='text-align: center;'><strong style='font-size: larger;'>Projekt der HTW</strong><br>" +
                "<strong>Team Clever</strong><br>" +
                "Fürüze Saritoprak, Sabine Matthies, Sude Ural, Nicole Driebe, Julia Neubert, Vivian Grace Glaubig<br>" +
                "<strong style='font-size: larger;'>Institution</strong><br>" +
                "<strong>Hochschule für Technik und Wirtschaft Berlin</strong><br>" +
                "— vertreten durch die Präsidentin Prof. Dr. Annabella Rauscher-Scheibe —<br>" +
                "<strong style='font-size: larger;'>Postanschrift</strong><br>" +
                "HTW Berlin, 10313 Berlin (Postfach)<br>" +
                "Campus Wilhelminenhof: </strong>" +
                "Wilhelminenhofstraße 75A, 12459 Berlin<br>" +
                "Campus Treskowallee: </strong>" +
                "Treskowallee 8, 10318 Berlin<br>" +
                "<strong style='font-size: larger;'>Telefon und Fax</strong><br>" +
                "Telefon: +49 30 5019 - 0 (Zentrale)<br>" +
                "<strong style='font-size: larger;'>Umsatzsteueridentifikationsnummer:</strong><br>" +
                "DE 137 214 568</p>");
        html.getElement().getStyle().set("align-self", "center");
        add(html);

        Button backButton = new Button("Zurück", event -> {
            getUI().ifPresent(ui -> ui.getPage().getHistory().back());
        });
        backButton.getElement().getStyle().set("align-self", "center");
        add(backButton);
    }

}