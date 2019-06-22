package com.example.project1.chat;

public class MsnReceive extends Msn {

    private Long hour;

    public MsnReceive() {
    }

    public MsnReceive(Long hour) {
        this.hour = hour;
    }

    public MsnReceive(String msn, String userID, String urlPhoto, String name, String photoPerfil, String type_msn, Long hour) {
        super(msn, userID, urlPhoto, name, photoPerfil, type_msn);
        this.hour = hour;
    }

    public Long getHour() {
        return hour;
    }

    public void setHour(Long hour) {
        this.hour = hour;
    }
}
