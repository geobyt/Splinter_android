package com.splinter.app.Database;

/**
 * Created by geo on 8/2/14.
 */
public class Message {
    //private variables
    int id;
    String locationId;
    String description;

    public Message(){

    }

    public Message(int id, String locationId, String description){
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

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
