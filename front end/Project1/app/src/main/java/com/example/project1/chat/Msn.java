package com.example.project1.chat;

public class Msn {

    private String msn;
    private String urlPhoto;
    private String name;
    private String photoPerfil;
    private String type_msn;
    private String userID;

    public Msn() {
    }

    public Msn(String msn,String userID, String name, String photoPerfil, String type_msn) {
        this.msn = msn;
        this.userID = userID;
        this.name = name;
        this.photoPerfil = photoPerfil;
        this.type_msn = type_msn;
    }

    public Msn(String msn,String userID, String urlPhoto, String name, String photoPerfil, String type_msn) {
        this.msn = msn;
        this.userID = userID;
        this.urlPhoto = urlPhoto;
        this.name = name;
        this.photoPerfil = photoPerfil;
        this.type_msn = type_msn;
    }

    public String getMsn() {
        return msn;
    }

    public void setMsn(String msn) {
        this.msn = msn;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

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

    public String getType_msn() {
        return type_msn;
    }

    public void setType_msn(String type_msn) {
        this.type_msn = type_msn;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }
}
