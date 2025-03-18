package com.example.application.entities;

/**
 * Die Enumeration Role repräsentiert verschiedene Benutzerrollen in der Anwendung.
 * Es werden die Rollen STUDENT, EXTERN und ADMIN definiert.
 * Diese Rollen werden verwendet, um den Zugriff auf bestimmte Funktionen oder Ansichten zu steuern.
 */
public enum Role {
    /**
     * Die Rolle STUDENT repraesentiert eine registrierte Studentin in der Anwendung.
     * Eine Studentin hat bestimmte Rechte und Zugriffe in Abhängigkeit von ihrer Rolle.
     */
    STUDENT,
    /**
     * Die Rolle EXTERN repräsentiert eine externe Benutzerin in der Anwendung.
     * Eine externere Benutzerin hat spezifische Berechtigungen und Zugriffe entsprechend ihrer Rolle.
     */
    EXTERN,
    /**
     * Die Rolle ADMIN repräsentiert eine Administratorin in der Anwendung.
     * Eine Administratorin hat erweiterte Rechte und Zugriffe zur Verwaltung der Anwendung.
     */
    ADMIN
}

