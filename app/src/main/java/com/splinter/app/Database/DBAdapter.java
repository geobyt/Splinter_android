package com.splinter.app.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by geo on 7/19/14.
 */
public class DBAdapter {

    //Geofence table
    public static final String KEY_GEOFENCE_ID = "geofence_id";
    public static final String KEY_GEOFENCE_LATITUDE = "latitude";
    public static final String KEY_GEOFENCE_LONGITUDE = "longitude";
    public static final String KEY_GEOFENCE_RADIUS = "radius";
    public static final String KEY_GEOFENCE_EXPIRATION = "expiration";
    public static final String KEY_GEOFENCE_TRANSITION_TYPE = "transition_type";

    //Coordinate table
    public static final String KEY_COORDINATE_LOCATION_ID = "location_id";
    public static final String KEY_COORDINATE_LATITUDE = "latitude";
    public static final String KEY_COORDINATE_LONGITUDE = "longitude";
    public static final String KEY_COORDINATE_COUNT = "count";
    public static final String KEY_COORDINATE_DATE = "date";
    public static final String KEY_COORDINATE_DESCRIPTION = "description";
    public static final String KEY_COORDINATE_ROWID = "_id";

    private static final String TAG = "DBAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Table creation sql statement
     */
    private static final String COORDINATE_TABLE_CREATE =
            "create table coordinate (_id integer primary key autoincrement, "
                    + "location_id text null, "
                    + "latitude text null, "
                    + "longitude text null, "
                    + "count integer null, "
                    + "description text null, "
                    + "date text null"
                    + ");";

    private static final String GEOFENCE_TABLE_CREATE =
            "create table geofence (geofence_id integer not null, "
                    + "latitude real null, "
                    + "longitude real null, "
                    + "radius real null, "
                    + "expiration real null, "
                    + "transition_type int null"
                    + ");";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE_COORDINATE = "coordinate";
    private static final String DATABASE_TABLE_GEOFENCE = "geofence";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(COORDINATE_TABLE_CREATE);
            db.execSQL(GEOFENCE_TABLE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS coordinate");
            onCreate(db);
        }
    }

    public DBAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public DBAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    //COORDINATE TABLE HELPERS
    public long createCoordinate(Coordinate coordinate) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_COORDINATE_LOCATION_ID, coordinate.locationId);
        initialValues.put(KEY_COORDINATE_LATITUDE, coordinate.latitude);
        initialValues.put(KEY_COORDINATE_LONGITUDE, coordinate.longitude);
        initialValues.put(KEY_COORDINATE_COUNT, coordinate.count);
        initialValues.put(KEY_COORDINATE_DESCRIPTION, coordinate.description);
        initialValues.put(KEY_COORDINATE_DATE, coordinate.date);

        return mDb.insert(DATABASE_TABLE_COORDINATE, null, initialValues);
    }

    public boolean deleteCoordinate(long rowId) {

        return mDb.delete(DATABASE_TABLE_COORDINATE, KEY_COORDINATE_ROWID + "=" + rowId, null) > 0;
    }

    public List<Coordinate> fetchAllCoordinates() {
        List<Coordinate> coordinates = new ArrayList<Coordinate>();

        Cursor mCursor =
                mDb.query(DATABASE_TABLE_COORDINATE, new String[] {
                    KEY_COORDINATE_ROWID,
                    KEY_COORDINATE_LOCATION_ID,
                    KEY_COORDINATE_LATITUDE,
                    KEY_COORDINATE_LONGITUDE,
                    KEY_COORDINATE_COUNT,
                    KEY_COORDINATE_DESCRIPTION,
                    KEY_COORDINATE_DATE
            }, null, null, null, null, null);

        if (mCursor.moveToFirst()) {
            do {
                Coordinate coordinate = new Coordinate(
                        Integer.parseInt(mCursor.getString(0)),
                        mCursor.getString(1),
                        mCursor.getString(2),
                        mCursor.getString(3),
                        Integer.parseInt(mCursor.getString(4)),
                        mCursor.getString(5),
                        mCursor.getString(6)
                );

                coordinates.add(coordinate);
            } while (mCursor.moveToNext());
        }

        return coordinates;
    }

    public Coordinate fetchCoordinate(long rowId) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE_COORDINATE, new String[] {
                    KEY_COORDINATE_ROWID,
                    KEY_COORDINATE_LOCATION_ID,
                    KEY_COORDINATE_LATITUDE,
                    KEY_COORDINATE_LONGITUDE,
                    KEY_COORDINATE_COUNT,
                    KEY_COORDINATE_DESCRIPTION,
                    KEY_COORDINATE_DATE
            }, KEY_COORDINATE_ROWID + "=" + rowId, null, null, null, null, null);

        if (mCursor != null) {
            mCursor.moveToFirst();
        }

        Coordinate coordinate = new Coordinate(
                Integer.parseInt(mCursor.getString(0)),
                mCursor.getString(1),
                mCursor.getString(2),
                mCursor.getString(3),
                Integer.parseInt(mCursor.getString(4)),
                mCursor.getString(5),
                mCursor.getString(6)
            );

        return coordinate;
    }

    public boolean updateCoordinate(long rowId, Coordinate coordinate) {
        ContentValues args = new ContentValues();
        args.put(KEY_COORDINATE_LOCATION_ID, coordinate.locationId);
        args.put(KEY_COORDINATE_LATITUDE, coordinate.latitude);
        args.put(KEY_COORDINATE_LONGITUDE, coordinate.longitude);
        args.put(KEY_COORDINATE_COUNT, coordinate.count);
        args.put(KEY_COORDINATE_DESCRIPTION, coordinate.description);
        args.put(KEY_COORDINATE_DATE, coordinate.date);

        return mDb.update(DATABASE_TABLE_COORDINATE, args, KEY_COORDINATE_ROWID + "=" + rowId, null) > 0;
    }

    //GEOFENCE TABLE HELPERS
    public long createGeofence(SimpleGeofence geofence) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_GEOFENCE_ID, geofence.getId());
        initialValues.put(KEY_GEOFENCE_LATITUDE, geofence.getLatitude());
        initialValues.put(KEY_GEOFENCE_LONGITUDE, geofence.getLongitude());
        initialValues.put(KEY_GEOFENCE_RADIUS, geofence.getRadius());
        initialValues.put(KEY_GEOFENCE_EXPIRATION, geofence.getExpirationDuration());
        initialValues.put(KEY_GEOFENCE_TRANSITION_TYPE, geofence.getTransitionType());

        return mDb.insert(DATABASE_TABLE_GEOFENCE, null, initialValues);
    }

    public boolean deleteGeofence(int id) {

        return mDb.delete(DATABASE_TABLE_GEOFENCE, KEY_GEOFENCE_ID + "=" + id, null) > 0;
    }

    public List<SimpleGeofence> fetchAllGeofences() {
        List<SimpleGeofence> geofences = new ArrayList<SimpleGeofence>();

        Cursor mCursor =
                mDb.query(DATABASE_TABLE_GEOFENCE, new String[] {
                        KEY_GEOFENCE_ID,
                        KEY_GEOFENCE_LATITUDE,
                        KEY_GEOFENCE_LONGITUDE,
                        KEY_GEOFENCE_RADIUS,
                        KEY_GEOFENCE_EXPIRATION,
                        KEY_GEOFENCE_TRANSITION_TYPE
                }, null, null, null, null, null);

        if (mCursor.moveToFirst()) {
            do {
                SimpleGeofence geofence = new SimpleGeofence(
                        Integer.parseInt(mCursor.getString(0)),
                        Double.parseDouble(mCursor.getString(1)),
                        Double.parseDouble(mCursor.getString(2)),
                        Float.parseFloat(mCursor.getString(3)),
                        Long.parseLong(mCursor.getString(4)),
                        Integer.parseInt(mCursor.getString(5))
                );

                geofences.add(geofence);
            } while (mCursor.moveToNext());
        }

        return geofences;
    }

    public SimpleGeofence fetchGeofence(int id) throws SQLException {
        Cursor mCursor =
                mDb.query(true, DATABASE_TABLE_GEOFENCE, new String[] {
                        KEY_GEOFENCE_ID,
                        KEY_GEOFENCE_LATITUDE,
                        KEY_GEOFENCE_LONGITUDE,
                        KEY_GEOFENCE_RADIUS,
                        KEY_GEOFENCE_EXPIRATION,
                        KEY_GEOFENCE_TRANSITION_TYPE
                }, KEY_GEOFENCE_ID + "=" + id, null, null, null, null, null);

        if (mCursor != null && mCursor.moveToFirst()) {

            SimpleGeofence geofence = new SimpleGeofence(
                    Integer.parseInt(mCursor.getString(0)),
                    Double.parseDouble(mCursor.getString(1)),
                    Double.parseDouble(mCursor.getString(2)),
                    Float.parseFloat(mCursor.getString(3)),
                    Double.valueOf(mCursor.getString(4)).longValue(),
                    Integer.parseInt(mCursor.getString(5))
            );

            return geofence;
        }

        return null;
    }

    public boolean updateGeofence(int id, SimpleGeofence geofence) {
        ContentValues args = new ContentValues();
        args.put(KEY_GEOFENCE_ID, geofence.getId());
        args.put(KEY_GEOFENCE_LATITUDE, geofence.getLatitude());
        args.put(KEY_GEOFENCE_LONGITUDE, geofence.getLongitude());
        args.put(KEY_GEOFENCE_RADIUS, geofence.getRadius());
        args.put(KEY_GEOFENCE_EXPIRATION, geofence.getExpirationDuration());
        args.put(KEY_GEOFENCE_TRANSITION_TYPE, geofence.getTransitionType());

        return mDb.update(DATABASE_TABLE_GEOFENCE, args, KEY_GEOFENCE_ID + "=" + id, null) > 0;
    }
}
