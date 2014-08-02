package com.splinter.app.Service;

import com.splinter.app.Database.Coordinate;
import com.splinter.app.Database.Message;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by geo on 7/23/14.
 */
public class JsonParser {
    public static List<Coordinate> ParseLocationsJson(String json){
        List<Coordinate> coordinates = new ArrayList<Coordinate>();

        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(json);

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray msg = (JSONArray) jsonObject.get("locations");
            Iterator iterator = msg.iterator();

            while (iterator.hasNext()) {
                JSONObject location = (JSONObject) iterator.next();

                String locationId = (String) location.get("RowKey");
                String description = (String) location.get("description");
                Double latitude = (Double) location.get("lat");
                Double longitude = (Double) location.get("long");

                Coordinate coordinate = new Coordinate(
                        0,
                        locationId,
                        latitude.toString(),
                        longitude.toString(),
                        0,
                        description,
                        ""
                );

                coordinates.add(coordinate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return coordinates;
    }

    public static List<Message> ParseMessagesJson(String json){
        List<Message> messages = new ArrayList<Message>();

        JSONParser parser = new JSONParser();

        try {
            Object obj = parser.parse(json);

            JSONObject jsonObject = (JSONObject) obj;

            JSONArray msg = (JSONArray) jsonObject.get("messages");
            Iterator iterator = msg.iterator();

            while (iterator.hasNext()) {
                JSONObject location = (JSONObject) iterator.next();

                long locationId = (Long) location.get("location_id");
                String description = (String) location.get("text");

                Message message = new Message(
                    0,
                    locationId,
                    description
                );

                messages.add(message);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return messages;
    }
}
