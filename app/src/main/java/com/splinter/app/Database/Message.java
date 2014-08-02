package com.splinter.app.Database;

/**
 * Created by geo on 8/2/14.
 */
public class Message {
    //private variables
    int id;
    long locationId;
    String description;

    public Message(){

    }

    public Message(int id, long locationId, String description){
        this.id = id;
        this.locationId = locationId;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getLocationId() {
        return locationId;
    }

    public void setLocationId(long locationId) {
        this.locationId = locationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
