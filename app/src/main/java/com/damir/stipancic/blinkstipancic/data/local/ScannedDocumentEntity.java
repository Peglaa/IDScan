package com.damir.stipancic.blinkstipancic.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity(tableName = "scanned_document")
public class ScannedDocumentEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String firstName = "";

    private String lastName = "";

    private String gender = "";

    private String OIB = "";

    private String dateOfBirth = "";

    private String nationality = "";

    private String documentNumber = "";

    private String dateOfExpiry = "";

    private String faceImage = "";

    private String frontImage = "";

    private String backImage = "";

    public ScannedDocumentEntity() {
    }

    public ScannedDocumentEntity(
            ScannedDocumentBuilder builder){

        this.firstName = builder.firstName;
        this.lastName = builder.lastName;
        this.gender = builder.gender;
        this.OIB = builder.OIB;
        this.dateOfBirth = builder.dateOfBirth;
        this.nationality = builder.nationality;
        this.documentNumber = builder.documentNumber;
        this.dateOfExpiry = builder.dateOfExpiry;
        this.faceImage = builder.faceImage;
        this.frontImage = builder.frontImage;
        this.backImage = builder.backImage;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getGender() {
        return gender;
    }

    public String getOIB() {
        return OIB;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getNationality() {
        return nationality;
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public String getDateOfExpiry() {
        return dateOfExpiry;
    }

    public String getFaceImage() {
        return faceImage;
    }

    public String getFrontImage() {
        return frontImage;
    }

    public String getBackImage() {
        return backImage;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setOIB(String OIB) {
        this.OIB = OIB;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public void setDateOfExpiry(String dateOfExpiry) {
        this.dateOfExpiry = dateOfExpiry;
    }

    public void setFaceImage(String faceImage) {
        this.faceImage = faceImage;
    }

    public void setFrontImage(String frontImage) {
        this.frontImage = frontImage;
    }

    public void setBackImage(String backImage) {
        this.backImage = backImage;
    }

    public static class ScannedDocumentBuilder{

        private final String firstName;

        private final String lastName;

        private String gender;

        private String OIB;

        private String dateOfBirth;

        private String nationality;

        private String documentNumber;

        private String dateOfExpiry;

        private String faceImage;

        private String frontImage;

        private String backImage;

        public ScannedDocumentBuilder(String firstName, String lastName){
            this.firstName = firstName;
            this.lastName = lastName;
        }

        public ScannedDocumentBuilder gender(String gender){
            this.gender = gender;
            return this;
        }

        public ScannedDocumentBuilder OIB(String OIB){
            this.OIB = OIB;
            return this;
        }

        public ScannedDocumentBuilder dateOfBirth(String dateOfBirth){
            this.dateOfBirth = dateOfBirth;
            return this;
        }

        public ScannedDocumentBuilder nationality(String nationality){
            this.nationality = nationality;
            return this;
        }

        public ScannedDocumentBuilder documentNumber(String documentNumber){
            this.documentNumber = documentNumber;
            return this;
        }

        public ScannedDocumentBuilder dateOfExpiry(String dateOfExpiry){
            this.dateOfExpiry = dateOfExpiry;
            return this;
        }

        public ScannedDocumentBuilder faceImage(String faceImage){
            this.faceImage = faceImage;
            return this;
        }

        public ScannedDocumentBuilder frontImage(String frontImage){
            this.frontImage = frontImage;
            return this;
        }

        public ScannedDocumentBuilder backImage(String backImage){
            this.backImage = backImage;
            return this;
        }

        public ScannedDocumentEntity build(){
            return new ScannedDocumentEntity(this);
        }

    }
}
