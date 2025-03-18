package com.example.application.views.inhaltfooter;

import com.example.application.views.MainLayout;
import com.vaadin.flow.component.HasElement;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.ParentLayout;
import com.vaadin.flow.router.RouterLayout;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.FlexComponent;

/**
 * Die Klasse FooterLayout repräsentiert das Layout der Fußzeile der Website.
 * Das Layout enthält Links zu verschiedenen Seiten.
 */
@ParentLayout(MainLayout.class)
public class FooterLayout extends VerticalLayout implements RouterLayout {
    private Div container = new Div();
    private Div footer = new Div();

    /**
     * Konstruktor für das FooterLayout-Objekt.
     * Hier werden die verschiedenen Links in der Fußzeile erstellt und dem Layout hinzugefuegt.
     */
    public FooterLayout() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setMargin(false);

        container.setSizeFull();

        Anchor htwBerlinLink = createStyledAnchor("https://www.htw-berlin.de", "@HTW Berlin");
        Anchor impressumLink = createStyledAnchor("/impressum", "Impressum");
        Anchor datenschutzLink = createStyledAnchor("/datenschutz", "Datenschutzhinweise");

        HorizontalLayout footerLayout = new HorizontalLayout(htwBerlinLink, impressumLink, datenschutzLink);
        footerLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        footerLayout.setWidth("100%");

        // Hinzufügen der Links zur Fußzeile
        footer.add(footerLayout);
        footer.getStyle().set("position", "fixed").set("bottom", "0");
        footer.getStyle().set("margin-top", "auto");
        footer.getStyle().set("width", "100%");
        footer.getStyle().set("background", "#F5F5F5");
        footer.getStyle().set("padding", "var(--lumo-space-m) var(--lumo-space-m)");

        add(container, footer);
        expand(container);
    }

    /**
     * Erstellt und gibt einen stilisierten Anchor (Link) zurück.
     *
     * @param href Die Ziel-URL des Anchors.
     * @param text Der angezeigte Text des Anchors.
     * @return Der erstellte stilisierte Anchor.
     */
    private Anchor createStyledAnchor(String href, String text) {
        Anchor anchor = new Anchor(href, text);
        anchor.getStyle().set("color", "black");

        return anchor;
    }

    /**
     * Diese Methode fuegt den uebergebenen Inhalt (content) zum Container-Element hinzu.
     * Wird durch das RouterLayout-Interface bereitgestellt.
     *
     * @param content Das Element, das dem Layout hinzugefuegt werden soll.
     */
    @Override
    public void showRouterLayoutContent(HasElement content) {
        container.getElement().appendChild(content.getElement());
    }
}