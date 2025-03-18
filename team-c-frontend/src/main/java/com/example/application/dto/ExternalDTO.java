package com.example.application.dto;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

/**
 * Gibt die einzelnen Bestandteile des Externen Nutzers als
 * DataTransferObject weiter an das Backend
 */
// DataTransferObject um den External als DTO ins Backend senden zu können
public class ExternalDTO {
    /**
     * Die ID des Nutzers
     */
    private long id;
    /**
     * Der Vorname des Nutzers
     */
    private String firstName;
    /**
     * Der Nachname des Nutzers
     */
    private String lastName;
    /**
     * Das Passwort des Nutzers
     */
    private String password;
    /**
     * Die Firma des Nutzers
     */
    private String company;
    /**
     * Startdatum der Verfügbarkeit des Nutzers
     */
    private LocalDate availabilityStart;
    /**
     * Enddatum der Verfügbarkeit des Nutzers
     */
    private LocalDate availabilityEnd;
    /**
     * Der Motivations-Text des Nutzers
     */
    private String description;
    /**
     * Die E-Mail des Nutzers
     */
    private String email;
    /**
     * Die Titel der Bachelorthemen die der Nutzer anbietet
     */
    private String title;
    /**
     * Die Beschreibung zu den Bachelorthemen die der Nutzer anbietet
     */
    private String bDescription;
    /**
     * Die Liste der Bestandteile die zu den Bachelorthemen gehören
     */
    private List<BachelorSubjectDTO> bachelorSubjects;
    /**
     * Die Liste der Fachbereich in denen der externe Nutzer sich anbietet/auskennt
     */
    private Set<SpecialFieldDTO> specialFields;

    /**
     * Gibt die ID des Nutzers zurück
     * @return ID des Nutzers
     */
    public long getId() {
        return id;
    }

    /**
     * Setz die ID des Nutzers
     * @param id sie zu setzende ID
     */
    public void setId(long id) {
        this.id = id;
    }

    /**
     * Gibt den Vornamen des Nutzers zurück
     * @return Vorname des Nutzers
     */
    public String getFirstName() {
        return firstName;
    }
    /**
     * Setzt den Vornamen des Nutzers
     * @param firstName der zu setzenden Vornamen
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    /**
     * Gibt den Nachnamen des Nutzers zurück
     * @return Nachnamen des Nutzers
     */
    public String getLastName() {
        return lastName;
    }
    /**
     * Setzt den Nachnamen des Nutzers
     * @param lastName der zu setzende Nachname
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    /**
     * Gibt das Passwort des Nutzers zurück
     * @return Passwort des Nutzers
     */
    public String getPassword() {
        return password;
    }
    /**
     * Setzt das Passwort des Nutzers
     * @param password das zu setzende Passwort
     */
    public void setPassword(String password) {
        this.password = password;
    }
    /**
     * Gibt die Firma des Nutzers zurück
     * @return die Firma des Nutzers
     */
    public String getCompany() {
        return company;
    }

    /**
     * Setzt den Namen der Firma des Nutzers
     * @param company den zu setzenden Namen der Firma
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * Gibt das Startdatum der Verfügbarkeit des Nutzers zurück
     * @return Startdatum der Verfügbarkeit des Nutzers
     */
    public LocalDate getAvailabilityStart() {
        return availabilityStart;
    }

    /**
     * Setzt das Startdatum der Verfügbarkeit des Nutzers
     * @param availabilityStart das zu setzende Startdatum der Verfügbarkeit
     */
    public void setAvailabilityStart(LocalDate availabilityStart) {
        this.availabilityStart = availabilityStart;
    }

    /**
     * Gibt das Enddatum der Verfügbarkeit des Nutzers zurück
     * @return Enddatum der Verfügbarkeit des Nutzers
     */
    public LocalDate getAvailabilityEnd() {
        return availabilityEnd;
    }

    /**
     * Setzt das Enddatum der Verfügbarkeit des Nutzers
     * @param availabilityEnd das zu setzende Enddatum der Verfügbarkeit
     */
    public void setAvailabilityEnd(LocalDate availabilityEnd) {
        this.availabilityEnd = availabilityEnd;
    }

    /**
     * Gibt den Motivations-Text des Nutzers zurück
     * @return Motivations-Text des Nutzers
     */
    public String getDescription() {
        return description;
    }
    /**
     * Setzt den Motivations-Text des Nutzers
     * @param description der zu setzende Motivations-Text
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Gibt die E-Mail des Nutzers zurück
     * @return E-Mail des Nutzers
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setzt die E-Mail des Nutzers
     * @param email die zu setzende E-Mail
     */
    public void setEmail(String email) {
        this.email = email;
    }
    /**
     * Gibt die Titel der Bachelorthemen, die der Nutzer anbietet zurück
     * @return Titel der Bachelorthemen
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setzt den Titel der Bachelorthemen die der Nutzer anbietet
     * @param title der zu setzende Titel
     */
    public void setTitle(String title) {
        this.title = title;
    }
    /**
     * Gibt die Beschreibung zu den Bachelorthemen, die der Nutzer anbietet zurück
     * @return  Beschreibung zu den Bachelorthemen
     */
    public String getBDescription() {
        return bDescription;
    }

    /**
     * Setzt die Beschreibung zu den Bachelorthemen die der Nutzer anbietet
     * @param bDescription die zu setzende Beschreibung
     */
    public void setBDescription(String bDescription) {
        this.bDescription = bDescription;
    }
    /**
     * Gibt die Liste der Bestandteile, die zu den Bachelorthemen gehören, die der Nutzer anbietet, zurück
     * @return Liste der Bestandteile, die zu den Bachelorthemen gehören
     */
    public List getBachelorSubjects() {
        return bachelorSubjects;
    }
    /**
     * Setzt die Liste der Bestandteile zu den Bachelorthemen
     * @param bachelorSubjects zu setzende Liste
     */
    public void setBachelorSubjects(List<BachelorSubjectDTO> bachelorSubjects) {
        this.bachelorSubjects = bachelorSubjects;
    }
    /**
     * Gibt die Liste der Fachbereich in denen der externe Nutzer sich anbietet/auskennt zurück
     * @return Liste der Fachbereich
     */
    public Set<SpecialFieldDTO> getSpecialFields() {
        return specialFields;
    }
    /**
     * Setzt die Liste der Bestandteile zu den Fachgebieten
     * @param specialFields die zu setzende Liste
     */
    public void setSpecialFields(Set<SpecialFieldDTO> specialFields) {
        this.specialFields = specialFields;
    }

}
