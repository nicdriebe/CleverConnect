package com.example.application.services;

public class ErrorEvent {
    private final String message;

    /**
     * Wird für verschiedene Error Nachrichten verwendet
     * @param message erhält einen String der zur Nachricht der Klasse wird
     */

    public ErrorEvent(String message) {
        this.message = message;
    }

    /**
     * Gibt eine Error Nachricht zurück
     * @return die Error Nachricht
     */

    public String getMessage() {
        return message;
    }
}
