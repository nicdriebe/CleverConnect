package com.example.application.dto;

import com.example.application.entities.Role;

/**
 * Überprüft den LogIn und übergibt beim erfolgreichen LogIn folgende Parameter weiter
 * einen Session-Token als String, die Rolle als Role Object und die ID als Long Object
 */
public class LoginResponse {
    /**
     * Der Session Token
     */
    private String token;
    /**
     * Die Rolle des Nutzers
     */
    private Role role;
    /**
     * Die ID des Nutzers
     */
    private Long id;

    /**
     * Konstruktor für ein LoginResponse Objekt
     * @param token der Token für die laufende Session
     * @param role die Rolle des Nutzers
     * @param id die eindeutige ID des Nutzers
     */

    public LoginResponse(String token, Role role, Long id) {
        this.token = token;
        this.role = role;
        this.id = id;
    }

    /**
     * Gibt den Token der Session zurück
     * @return den Token der Session
     */
    public String getToken() {
        return token;
    }

    /**
     * Setzt den Token für die Session
     * @param token der zu setzende Token
     */
    public void setToken(String token) {
        this.token = token;
    }

    /**
     * Gibt die Rolle des Nutzers zurück
     * @return Rolle des Nutzers
     */
    public Role getRole() {
        return role;
    }

    /**
     * Setzt die Rolle für die Session
     * @param role die zu setzende Rolle
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Gibt die ID des Nutzers zurück
     * @return die ID des Nutzers
     */
    public Long getId() {
        return id;
    }

    /**
     * Setzt die ID für die Session
     * @param id die zu setzende ID
     */
    public void setId(Long id) {
        this.id = id;
    }
}
