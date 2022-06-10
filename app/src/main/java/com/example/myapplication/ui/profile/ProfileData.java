package com.example.myapplication.ui.profile;

public class ProfileData {

    String fullname,dob,phone,image_uri;

    public ProfileData() {
    }

    public ProfileData(String fullname, String dob, String phone, String image_uri) {
        this.fullname = fullname;
        this.dob = dob;
        this.phone = phone;
        this.image_uri = image_uri;
    }

    public String getFullname() {
        return fullname;
    }

    public String getDob() {
        return dob;
    }

    public String getPhone() {
        return phone;
    }

    public String getImage_uri() {
        return image_uri;
    }
}
