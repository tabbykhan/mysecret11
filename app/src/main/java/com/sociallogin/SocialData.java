package com.sociallogin;


import com.model.BaseModel;

/**
 * Created by ubuntu on 12/5/16.
 */
public class SocialData extends BaseModel {
    private String id;
    private String name;
    private String firstName;
    private String lastName;
    private String email;
    private String loginFrom;

    public String getId () {
        return id;
    }

    public void setId (String id) {
        this.id = id;
    }

    public String getName () {
        return name;
    }

    public void setName (String name) {
        this.name = name;
    }

    public String getFirstName () {
        return getValidString(firstName);
    }

    public void setFirstName (String firstName) {
        this.firstName = firstName;
    }

    public String getLastName () {
        return getValidString(lastName);
    }

    public void setLastName (String lastName) {
        this.lastName = lastName;
    }

    public String getEmail () {
        return getValidString(email);
    }

    public void setEmail (String email) {
        this.email = email;
    }

    public String getLoginFrom () {
        return getValidString(loginFrom);
    }

    public void setLoginFrom (String loginFrom) {
        this.loginFrom = loginFrom;
    }


    @Override
    public String toString () {
        return "id=" + getId() +
                ", firstName=" + firstName +
                ", lastName=" + lastName +
                ", email=" + email +
                ", loginFrom=" + loginFrom;
    }

    public boolean isValidEmail(){
        return isValidString(getEmail());
    }
}
