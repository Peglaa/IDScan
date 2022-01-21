package com.damir.stipancic.blinkstipancic.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "scanned_document")
public class ScannedDocumentEntity {

    private String firstName;

    private String lastName;

    private String gender;

    @NonNull
    @PrimaryKey
    private String OIB;

    private String dateOfBirth;

    private String nationality;

    private String documentNumber;

    private String dateOfExpiry;

    private String faceImage;

    private String frontImage;

    private String backImage;

    public ScannedDocumentEntity(
            String firstName,
            String lastName,
            String gender,
            @NonNull String OIB,
            String dateOfBirth,
            String nationality,
            String documentNumber,
            String dateOfExpiry,
            String faceImage,
            String frontImage,
            String backImage){

        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.OIB = OIB;
        this.dateOfBirth = dateOfBirth;
        this.nationality = nationality;
        this.documentNumber = documentNumber;
        this.dateOfExpiry = dateOfExpiry;
        this.faceImage = faceImage;
        this.frontImage = frontImage;
        this.backImage = backImage;

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @NonNull
    public String getOIB() {
        return OIB;
    }

    public void setOIB(@NonNull String OIB) {
        this.OIB = OIB;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDateOfExpiry() {
        return dateOfExpiry;
    }

    public void setDateOfExpiry(String dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public String getFrontImage() {
        return frontImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    public String getBackImage() {
        return backImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }
}
