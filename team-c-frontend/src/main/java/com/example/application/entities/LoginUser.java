package com.example.application.entities;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

/**
 * Die Klasse LoginUser repraesentiert ein Benutzerobjekt mit Anmeldedaten in der Anwendung.
 * Sie enthaelt Informationen wie Vorname, Nachname, E-Mail, Passwort, Anmeldedatum, Benutzerrolle,
 * sowie Statusinformationen über Sperrung und Aktivierung.
 */
public class LoginUser {
    private Long id;
    /**
     * Der Vorname der Benutzerin.
     */
    private String firstName;
    /**
     * Der Nachname der Benutzerin.
     */
    private String lastName;
    /**
     * Die E-Mail-Adresse der Benutzerin.
     */
    private String email;
    /**
     * Das Passwort der Benutzerin.
     */
    private String password;
    /**
     * Das Anmeldedatum der Benutzerin.
     */
    private LocalDate registrationDate;
    /**
     * Die Rolle der Benutzerin.
     */
    private Role role;
    /**
     * Gibt an, ob das Benutzerkonto gesperrt ist.
     */
    boolean locked;
    /**
     * Gibt an, ob das Benutzerkonto aktiviert ist.
     */
    boolean enabled;

    /**
     * Konstruktor für ein vollständiges Benutzerobjekt mit allen Attributen.
     *
     * @param firstName         Der Vorname der Benutzerin.
     * @param lastName          Der Nachname der Benutzerin.
     * @param email             Die E-Mail-Adresse der Benutzerin.
     * @param password          Das Passwort der Benutzerin.
     * @param registrationDate  Das Anmeldedatum der Benutzerin.
     * @param role              Die Rolle der Benutzerin.
     * @param locked            Gibt an, ob das Benutzerkonto gesperrt ist.
     * @param enabled           Gibt an, ob das Benutzerkonto aktiviert ist.
     */
    public LoginUser(String firstName,
                     String lastName,
                     String email,
                     String password,
                     LocalDate registrationDate,
                     Role role,
                     boolean locked,
                     boolean enabled) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.role = role;
        this.locked = locked;
        this.enabled = enabled;
    }

    /**
     * Konstruktor für ein Benutzerobjekt mit zusätzlicher ID.
     *
     * @param id                Die eindeutige Identifikationsnummer des Benutzers.
     * @param firstName         Der Vorname der Benutzerin.
     * @param lastName          Der Nachname der Benutzerin.
     * @param email             Die E-Mail-Adresse der Benutzerin.
     * @param password          Das Passwort der Benutzerin.
     * @param registrationDate  Das Anmeldedatum der Benutzerin.
     * @param role              Die Rolle der Benutzerin.
     * @param locked            Gibt an, ob das Benutzerkonto gesperrt ist.
     * @param enabled           Gibt an, ob das Benutzerkonto aktiviert ist.
     */
    public LoginUser(Long id,
                     String firstName,
                     String lastName,
                     String email,
                     String password,
                     LocalDate registrationDate,
                     Role role,
                     boolean locked,
                     boolean enabled) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate != null ? registrationDate : LocalDate.now();
        this.role = role;
        this.locked = locked;
        this.enabled = enabled;
    }

    /**
     * Standard-Konstruktor für ein leeres Benutzerobjekt.
     */
    public LoginUser() {
    }

    /**
     * Gibt die eindeutige Identifikationsnummer (ID) der Benutzerin zurück.
     *
     * @return Die ID der Benutzerin.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die eindeutige Identifikationsnummer (ID) der Benutzerin.
     *
     * @param id Die zu setzende ID der Benutzerin.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Gibt den Vornamen der Benutzerin zurück.
     *
     * @return Der Vorname der Benutzerin.
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setzt den Vornamen der Benutzerin.
     *
     * @param firstName Der zu setzende Vorname der Benutzerin.
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Gibt den Nachnamen der Benutzerin zurück.
     *
     * @return Der Nachname der Benutzerin.
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Setzt den Nachnamen der Benutzerin.
     *
     * @param lastName Der zu setzende Nachname der Benutzerin.
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Gibt die E-Mail-Adresse der Benutzerin zurück.
     *
     * @return Die E-Mail-Adresse der Benutzerin.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setzt die E-Mail-Adresse der Benutzerin.
     *
     * @param email Die zu setzende E-Mail-Adresse der Benutzerin.
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Gibt das Passwort der Benutzerin zurück.
     *
     * @return Das Passwort der Benutzerin.
     */
    public String getPassword() {
        return password;
    }

    /**
     * Setzt das Passwort der Benutzerin.
     *
     * @param password Das zu setzende Passwort der Benutzerin.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Gibt das Anmeldedatum der Benutzerin zurück.
     *
     * @return Das Anmeldedatum der Benutzerin.
     */
    public LocalDate getRegistrationDate() {
        return registrationDate;
    }

    /**
     * Setzt das Anmeldedatum der Benutzerin.
     *
     * @param registrationDate Das zu setzende Anmeldedatum der Benutzerin.
     */
    public void setRegistrationDate(LocalDate registrationDate) {
        this.registrationDate = registrationDate;
    }

    /**
     * Gibt die Rolle der Benutzerin zurück.
     *
     * @return Die Rolle der Benutzerin.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Setzt die Rolle der Benutzerin.
     *
     * @param role Die zu setzende Rolle der Benutzerin.
     */
    public void setRole(Role role) {
        this.role = role;
    }
}
