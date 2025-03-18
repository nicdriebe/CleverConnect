package com.example.application.dto;

import java.util.Set;

/**
 * Gibt die einzelnen Bestandteile der Special Field, also der Fachgebiete,
 * als DataTransferObject weiter
 */
public class SpecialFieldDTO {
    /**
     * ID des Fachgebietes
     */
    private Long id;
    /**
     * Titel des Fachgebietes
     */
    private String name;
    /**
     * ID des Externen Nutzers
     */
    private Set<Long> externalIds;

    /**
     * Konstruktor für ein SpecialField Objekt
     * @param name Titel des Fachgebietes
     */

    public SpecialFieldDTO(String name) {
        this.name = name;
    }

    /**
     * Standard-Konstruktor für ein leeres Benutzerobjekt.
     */
    public SpecialFieldDTO() {}

    /**
     * Gibt die ID des Fachgebietes zurück
     * @return ID des Fachgebietes
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die ID des Fachgebietes
     * @param id die zu setzende ID
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * Gibt den Titel des Fachgebietes zurück
     * @return Titel des Fachgebietes
     */
    public String getName() {
        return name;
    }

    /**
     * Setzt den Titel des Fachgebietes
     * @param name der zu setzende Titel
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gibt die ID des Externen Nutzers zurück
     * @return ID des Externen Nutzers
     */
    public Set<Long> getExternalIds() {
        return externalIds;
    }

    /**
     * Setzt die ID des Externen Nutzers
     * @param externalIds die zu setzende ID
     */
    public void setExternalIds(Set<Long> externalIds) {
        this.externalIds = externalIds;
    }
}
