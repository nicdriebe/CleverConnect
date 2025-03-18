package com.example.application.dto;

/**
 * Diese Klasse gibt die einzelnen Bestandteile der Bachelor Subject Liste als
 * DataTransferObject weiter
 *
 */
public class BachelorSubjectDTO {
    /**
     * Die ID des Themas um es zuordnen zu können
     */
    private Long id;
    /**
     * Der Titel des Bachelorthemas
     */
    private String title;
    /**
     * Detailliertere Beschreibung des Bachelorthemas
     */
    private String bDescription;
    /**
     * Die ID um es einen Nutzer zuordnen zu können
     */
    private Long externalId;
    /**
     * Gibt den Titel des Bachelorthemas zurück
     * @return Titel des Bachelorthemas
     */
    public String getTitle() {
        return title;
    }
    /**
     * Setzt den Titel des Bachelorthemas
     * @param title den zu setzenden Titel des Bachelorthemas
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * Gibt die Beschreibung des Bachelorthemas zurück
     * @return Beschreibung des Bachelorthemas
     */
    public String getBDescription() {
        return bDescription;
    }
    /**
     * Setzt die Beschreibung des Bachelorthemas
     * @param bDescription die zu setzende Beschreibung des Bachelorthemas
     */
    public void setBDescription(String bDescription) {
        this.bDescription = bDescription;
    }
    /**
     * Gibt die ID des Themas zurück
     * @return ID des Themas
     */
    public Long getId() {
        return id;
    }
    /**
     * Setzt die ID des Themas
     * @param id setzt ID des Themas
     */
    public void setId(Long id) {
        this.id = id;
    }
    /**
     * Gibt die ID der externen Benutzer zurück
     * @return ID der externen Benutzer
     */

    public Long getExternalId() {
        return externalId;
    }
    /**
     * Setzt die ID des externen Nutzers
     * @param externalId setzt ID des externen Nutzers
     */
    public void setExternalId(Long externalId) {
        this.externalId = externalId;
    }
}
