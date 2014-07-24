package com.splinter.app.Service;

import com.splinter.app.Database.Coordinate;

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
    public static List<Coordinate> ParseJson(String json){
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
                Double latitude = (Double) location.get("lat");
                Double longitude = (Double) location.get("long");

                Coordinate coordinate = new Coordinate(
                        0,
                        locationId,
                        latitude.toString(),
                        longitude.toString(),
                        0,
                        ""
                );

                coordinates.add(coordinate);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return coordinates;
    }
}