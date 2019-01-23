package com.rultech.naman.syncnewcontact;

public class ContactVD {
    private String ContactImage;
    private String ContactName;
    private String ContactNumber;
    private String id;


    public ContactVD() {
        ContactImage = "";
        ContactName = "";
        ContactNumber = "";
        id="";
    }

    public String getContactName() {
        return ContactName;
    }

    public String getContactImage() {
        return ContactImage;
    }

    public void setContactImage(String contactImage) {
        ContactImage = contactImage;
    }

    public void setContactName(String contactName) {
        ContactName = contactName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
