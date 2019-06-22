package com.example.project1.chat;

import java.util.Map;

public class MsnSend extends Msn {
    private Map hour;

    public MsnSend() {
    }

    public MsnSend(Map hour) {
        this.hour = hour;
    }

    public MsnSend(String msn, String userID, String name, String photoPerfil, String type_msn, Map hour) {
        super(msn, userID, name, photoPerfil, type_msn);
        this.hour = hour;
    }

    public MsnSend(String msn, String userID, String urlPhoto, String name, String photoPerfil, String type_msn, Map hour) {
        super(msn, userID, urlPhoto, name, photoPerfil, type_msn);
        this.hour = hour;
    }

    public Map getHour() {
        return hour;
    }

    public void setHour(Map hour) {
        this.hour = hour;
    }
}
