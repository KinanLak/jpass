package com.jpass.model;

import java.time.LocalDate;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Représente une entrée d'identité.
 */
@JsonTypeName("identity")
public class IdentityEntry extends Entry {
    @JsonProperty("firstName")
    private String firstName;

    @JsonProperty("lastName")
    private String lastName;

    @JsonProperty("email")
    private String email;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("address")
    private String address;

    @JsonProperty("birthDate")
    private LocalDate birthDate;

    // Default constructor for Jackson
    public IdentityEntry() {
        super();
    }

    @JsonCreator
    public IdentityEntry(
            @JsonProperty("title") String title,
            @JsonProperty("firstName") String firstName,
            @JsonProperty("lastName") String lastName,
            @JsonProperty("email") String email,
            @JsonProperty("phoneNumber") String phoneNumber,
            @JsonProperty("address") String address,
            @JsonProperty("birthDate") LocalDate birthDate) {
        super(title);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = "";
        this.address = "";
        this.birthDate = null;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}