package com.jpass.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonCreator;
import java.time.LocalDate;

/**
 * Représente une entrée de carte bancaire.
 */
@JsonTypeName("creditcard")
public class CreditCardEntry extends Entry {
    @JsonProperty("cardholderName")
    private String cardholderName;

    @JsonProperty("cardNumber")
    private String cardNumber;

    @JsonProperty("expiryDate")
    private LocalDate expiryDate;

    @JsonProperty("cvv")
    private String cvv;

    @JsonProperty("type")
    private String type;

    // Default constructor for Jackson
    public CreditCardEntry() {
        super();
    }

    @JsonCreator
    public CreditCardEntry(
            @JsonProperty("title") String title,
            @JsonProperty("cardholderName") String cardholderName,
            @JsonProperty("cardNumber") String cardNumber,
            @JsonProperty("expiryDate") LocalDate expiryDate,
            @JsonProperty("cvv") String cvv) {
        super(title);
        this.cardholderName = cardholderName;
        this.cardNumber = cardNumber;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
        addModification();
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
        addModification();
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
        addModification();
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
        addModification();
    }

    public String getCardholderName() {
        return cardholderName;
    }

    public void setCardholderName(String cardholderName) {
        this.cardholderName = cardholderName;
        addModification();
    }
}