package com.jpass.model;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * Représente une entrée de mot de passe pour un site web.
 */
@JsonTypeName("password")
public class PasswordEntry extends Entry {
    @JsonProperty("url")
    private String url;
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("password")
    private String password;
    
    @JsonProperty("favicon")
    private String favicon;
    
    @JsonProperty("expirationDate")
    private LocalDateTime expirationDate;



    // Default constructor for Jackson
    public PasswordEntry() {
        super();
    }

    @JsonCreator
    public PasswordEntry(
        @JsonProperty("title") String title,
        @JsonProperty("username") String username,
        @JsonProperty("password") String password,
        @JsonProperty("url") String url) {
        super(title);
        this.username = username;
        this.password = password;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
        addModification();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
        addModification();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        addModification();
    }

    public String getFavicon() {
        return favicon;
    }

    public void setFavicon(String favicon) {
        this.favicon = favicon;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(LocalDateTime expirationDate) {
        this.expirationDate = expirationDate;
        addModification();
    }

    //public boolean isExpired() {
    //    return expirationDate != null && LocalDateTime.now().isAfter(expirationDate);
    //}
}