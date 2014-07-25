package com.splinter.app.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.splinter.app.Database.Coordinate;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by geo on 7/19/14.
 */
public class DBAdapter {
    public static final String KEY_LOCATION_ID = "location_id";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_COUNT = "count";
    public static final String KEY_DATE = "date";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_ROWID = "_id";

    private static final String TAG = "DBAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;

    /**
     * Database creation sql statement
     */
    private static final String DATABASE_CREATE =
            "create table coordinate (_id integer primary key autoincrement, "
                    + "location_id text null, "
                    + "latitude text null, "
                    + "longitude text null, "
                    + "count integer null, "
                    + "description text null, "
                    + "date text null"
                    + ");";

    private static final String DATABASE_NAME = "data";
    private static final String DATABASE_TABLE = "coordinate";
    private static final int DATABASE_VERSION = 1;

    private final Context mCtx;

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);
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

    public long createCoordinate(Coordinate coordinate) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(KEY_LOCATION_ID, coordinate.locationId);
        initialValues.put(KEY_LATITUDE, coordinate.latitude);
        initialValues.put(KEY_LONGITUDE, coordinate.longitude);
        initialValues.put(KEY_COUNT, coordinate.count);
        initialValues.put(KEY_DESCRIPTION, coordinate.description);
        initialValues.put(KEY_DATE, coordinate.date);

        return mDb.insert(DATABASE_TABLE, null, initialValues);
    }

    public boolean deleteCoordinate(long rowId) {

        return mDb.delete(DATABASE_TABLE, KEY_ROWID + "=" + rowId, null) > 0;
    }

    public List<Coordinate> fetchAllCoordinates() {
        List<Coordinate> coordinates = new ArrayList<Coordinate>();

        Cursor mCursor =
                mDb.query(DATABASE_TABLE, new String[] {
                    KEY_ROWID,
                    KEY_LOCATION_ID,
                    KEY_LATITUDE,
                    KEY_LONGITUDE,
                    KEY_COUNT,
                    KEY_DESCRIPTION,
                    KEY_DATE
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
                mDb.query(true, DATABASE_TABLE, new String[] {
                    KEY_ROWID,
                    KEY_LOCATION_ID,
                    KEY_LATITUDE,
                    KEY_LONGITUDE,
                    KEY_COUNT,
                    KEY_DESCRIPTION,
                    KEY_DATE
            }, KEY_ROWID + "=" + rowId, null, null, null, null, null);

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
        args.put(KEY_LOCATION_ID, coordinate.locationId);
        args.put(KEY_LATITUDE, coordinate.latitude);
        args.put(KEY_LONGITUDE, coordinate.longitude);
        args.put(KEY_COUNT, coordinate.count);
        args.put(KEY_DESCRIPTION, coordinate.description);
        args.put(KEY_DATE, coordinate.date);

        return mDb.update(DATABASE_TABLE, args, KEY_ROWID + "=" + rowId, null) > 0;
    }
}
