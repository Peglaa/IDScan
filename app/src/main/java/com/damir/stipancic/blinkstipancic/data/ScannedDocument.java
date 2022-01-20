package com.damir.stipancic.blinkstipancic.data;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ScannedDocument {

    private String firstName;
    private String lastName;
    private String gender;
    private String OIB;
    private Date dateOfBirth;
    private String nationality;
    private String documentNumber;
    private Date dateOfExpiry;
}
