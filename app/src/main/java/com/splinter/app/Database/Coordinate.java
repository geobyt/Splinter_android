package com.splinter.app.Database;

/**
 * Created by geo on 7/19/14.
 */
public class Coordinate {
    //private variables
    int id;
    String locationId;
    String latitude;
    String longitude;
    int count;
    String date;

    public Coordinate(){

    }

    public Coordinate(int id, String locationId, String latitude, String longitude,
                      int count, String date){
        this.id = id;
        this.locationId = locationId;
        this.latitude= latitude;
        this.longitude = longitude;
        this.count = count;
        this.date = date;
    }

    public int getID(){
        return this.id;
    }

    public void setID(int id){
        this.id = id;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
