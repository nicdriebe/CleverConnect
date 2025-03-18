package com.example.application.dto;

import java.time.LocalDate;
import java.util.List;

public class ExternalRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String company;
    LocalDate availabilityStart;
    LocalDate availabilityEnd;
    private String description;
    private List<BachelorSubjectDTO> bachelorSubjects;
    private List<SpecialFieldDTO> specialFields;

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<BachelorSubjectDTO> getBachelorSubjects() {
        return bachelorSubjects;
    }

    public void setBachelorSubjects(List<BachelorSubjectDTO> bachelorSubjects) {
        this.bachelorSubjects = bachelorSubjects;
    }

    public List<SpecialFieldDTO> getSpecialFields() {
        return specialFields;
    }

    public void setSpecialFields(List<SpecialFieldDTO> specialFields) {
        this.specialFields = specialFields;
    }
}
