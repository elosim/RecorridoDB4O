package com.example.ivan.recorridodb4o.pojo;

/**
 * Created by ivan on 3/6/2016.
 */
public class Position {
    private double latitude;
    private double longitude;
    private String day;

    public Position(double latitude, double longitude, String day) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.day = day;
    }

    public Position() {
    }


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    @Override
    public String toString() {
        return "Position{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", day=" + day +
                '}';
    }
}
