package com.example.project1;

class GooglePlace {
    private String name;
    private String latitude;
    private String longitude;
    private String description;
    private int type;

    static final int ALL=0;
    static final int SUPERMARKET = 1;
    static final int OCIO = 2;
    static final int SECOND_HAND = 3;
    static final int RESTAURANT =4;

    public GooglePlace() {
        this.name = "";
        this.latitude = "";
        this.longitude = "";
        this.description = "";
        this.type = ALL;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getName() {
        return name;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

}
