package com.example.application.views.registrierenzweitbetreuerin;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import java.time.LocalDate;

/**
 * Diese Klasse repraesentiert einen DateRangePicker für die Auswahl eines Zeitraums.
 * Sie ermoeglicht es Benutzern, ein Start- und Enddatum auszuwaehlen.
 */
public class DateRangePicker extends HorizontalLayout {
    private final DatePicker availabilityStart = new DatePicker("Verfügbarkeit: Von");
    private final DatePicker availabilityEnd = new DatePicker("Verfügbarkeit: Bis");

    /**
     * Konstruktor fuer den DateRangePicker.
     * Initialisiert die DatePickers und fuegt ihnen Event-Listener hinzu.
     */
    public DateRangePicker() {
        availabilityStart.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                availabilityEnd.setMin(e.getValue());
            }
        });

        availabilityEnd.addValueChangeListener(e -> {
            if (e.getValue() != null) {
                availabilityStart.setMax(e.getValue());
            }
        });

        add(availabilityStart, availabilityEnd);
    }

    /**
     * Gibt den DatePicker fuer das Startdatum zurueck.
     *
     * @return Der DatePicker fuer das Startdatum.
     */
    public DatePicker getAvailabilityStart() {
        return availabilityStart;
    }

    /**
     * Gibt den DatePicker fuer das Enddatum zurueck.
     *
     * @return Der DatePicker fuer das Enddatum.
     */
    public DatePicker getAvailabilityEnd() {
        return availabilityEnd;
    }

    /**
     * Gibt das ausgewaehlte Startdatum zurueck.
     *
     * @return Das ausgewaehlte Startdatum.
     */
    public LocalDate getSelectedStartDate() {
        return availabilityStart.getValue();
    }

    /**
     * Gibt das ausgewaehlte Enddatum zurueck.
     *
     * @return Das ausgewaehlte Enddatum.
     */
    public LocalDate getSelectedEndDate() {
        return availabilityEnd.getValue();
    }

}
