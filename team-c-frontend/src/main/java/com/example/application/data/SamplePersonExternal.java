package com.example.application.data;

import com.example.application.views.registrierenzweitbetreuerin.DateRangePicker;
import com.example.application.views.registrierenzweitbetreuerin.ExternalEmail;
import com.vaadin.flow.component.textfield.TextField;
import jakarta.persistence.Entity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.List;


@Entity
public class SamplePersonExternal extends AbstractEntity {
    //@NotBlank(message = "Name may not be empty")
    @NotNull
    @NotEmpty
    private String firstName;
    //@NotBlank
    @NotNull
    @NotEmpty
    private String lastName;
    @Email
    @NotEmpty
    private String email;
    private String role;
    @NotEmpty
    private String password;
    private TextField textField;
    private TextField textField2;
    @NotBlank
    private String company;

    //LocalDate., weil auch im Backend so und das Format MUSS gleich sein, sonst kann es Backend nicht erkennen
    private LocalDate availabilityStart;
    private LocalDate availabilityEnd;

    //Motivationstext
    private String description;
    private Long userId;

    private String bDescription;


    public SamplePersonExternal() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getCompany() {
        return company;
    }

    public LocalDate getAvailabilityStart() {
        return availabilityStart;
    }

    public void setAvailabilityStart(LocalDate availabilityStart) {
        this.availabilityStart = availabilityStart;
    }

    public LocalDate getAvailabilityEnd() {
        return availabilityEnd;
    }

    public void setAvailabilityEnd(LocalDate availabilityEnd) {
        this.availabilityEnd = availabilityEnd;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
    public String getBDescription() {
        return bDescription;
    }

    public void setBDescription(String description) {
        this.bDescription = description;
    }

    public Long getUserId() {
        return userId;
    }

    // Neue Methode für die Erstellung einer Instanz von SamplePersonExternal aus den UI-Feldern
    public SamplePersonExternal createSamplePersonExternalFromFields() {
        SamplePersonExternal externalUser = new SamplePersonExternal();
        externalUser.setFirstName(this.textField.getValue());
        externalUser.setLastName(this.textField2.getValue());
        externalUser.setRole("EXTERN");
        return externalUser;
    }
}
