package com.example.project1.model;

public class User {

   // private String userID;
    private String name;
    private String photoPerfil;

    public User(){

    }

    public User( String name, String photoPerfil) {
        //this.userID = userID;
        this.name = name;
        this.photoPerfil = photoPerfil;
    }


   /* public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
*/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoPerfil() {
        return photoPerfil;
    }

    public void setPhotoPerfil(String photoPerfil) {
        this.photoPerfil = photoPerfil;
    }
}
