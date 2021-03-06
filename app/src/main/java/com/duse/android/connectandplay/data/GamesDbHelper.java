package com.duse.android.connectandplay.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


/**
 * Created by Mahesh Gaya on 10/20/16.
 */

public class GamesDbHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "games.db";
    public static final int DATABASE_VERSION = 3;

    public GamesDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //LOCATION TABLE
        /**
         * CREATE TABLE location (
         * _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
         * address TEXT NOT NULL,
         * latitude REAL NOT NULL,
         * longitude REAL NOT NULL
         * );
         */
        final String SQL_CREATE_LOCATION_TABLE =
                "CREATE TABLE " + GamesContract.LocationEntry.TABLE_NAME + "(" +
                        GamesContract.LocationEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        GamesContract.LocationEntry.COLUMN_ADDRESS + " TEXT NOT NULL, " +
                        GamesContract.LocationEntry.COLUMN_LATITUDE + " REAL NOT NULL, " +
                        GamesContract.LocationEntry.COLUMN_LONGITUDE + " REAL NOT NULL " +
                        ");";

        //PARTICIPATE TABLE
        /**
         * CREATE TABLE participate (
         * _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
         * game_id INTEGER NOT NULL,
         * FOREIGN KEY (game_id) REFERENCES sport(_id),
         * );
         */
        final String SQL_CREATE_PARTICIPATE_TABLE =
                "CREATE TABLE " + GamesContract.ParticipateEntry.TABLE_NAME + "(" +
                        GamesContract.ParticipateEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        GamesContract.ParticipateEntry.COLUMN_GAME_ID + " INTEGER NOT NULL, " +
                        "FOREIGN KEY (" + GamesContract.ParticipateEntry.COLUMN_GAME_ID + ") " +
                        "REFERENCES " + GamesContract.GameEntry.TABLE_NAME + "(" + GamesContract.GameEntry._ID + ")" +
                        ");";

        //USER TABLE
        /**
         * CREATE TABLE user (
         * _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
         * username TEXT NOT NULL,
         * first_name TEXT NOT NULL,
         * last_name TEXT NOT NULL,
         * biography TEXT NOT NULL,
         * current_user TEXT NOT NULL
         * );
         */
        final String SQL_CREATE_USER_TABLE =
                "CREATE TABLE " + GamesContract.UserEntry.TABLE_NAME + "(" +
                        GamesContract.UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        GamesContract.UserEntry.COLUMN_USERNAME + " TEXT NOT NULL, " +
                        GamesContract.UserEntry.COLUMN_FIRST_NAME + " TEXT NOT NULL, " +
                        GamesContract.UserEntry.COLUMN_LAST_NAME + " TEXT NOT NULL, " +
                        GamesContract.UserEntry.COLUMN_BIOGRAPHY + " TEXT NOT NULL, " +
                        GamesContract.UserEntry.COLUMN_CURRENT_USER + " TEXT NOT NULL" +
                        ");";

        //SPORT TABLE
        /**
         * CREATE TABLE sport (
         * _id INTEGER PRIMARY KEY AUTOINCREMENT,
         * sport_name TEXT NOT NULL
         * );
         */
        final String SQL_CREATE_SPORT_TABLE =
                "CREATE TABLE " + GamesContract.SportEntry.TABLE_NAME + "(" +
                        GamesContract.SportEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        GamesContract.SportEntry.COLUMN_SPORT_NAME + " TEXT NOT NULL " +
                        ");";

        //GAME TABLE
        /**
         * CREATE TABLE game (
         * _id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
         * game_name TEXT NOT NULL,
         * sport_id INTEGER NOT NULL,
         * time TEXT NOT NULL,
         * date TEXT NOT NULL,
         * location_id INTEGER NOT NULL,
         * description TEXT NOT NULL,
         * people_needed INTEGER NOT NULL,
         * user_id INTEGER NOT NULL,
         * FOREIGN KEY (sport_id) REFERENCES sport(_id),
         * FOREIGN KEY (user_id) REFERENCES user(_id),
         * FOREIGN KEY (location_id) REFERENCES location(_id)
         * );
         */
        final String SQL_CREATE_GAME_TABLE =
                "CREATE TABLE " + GamesContract.GameEntry.TABLE_NAME + "(" +
                        GamesContract.GameEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        GamesContract.GameEntry.COLUMN_GAME_NAME + " TEXT NOT NULL, " +
                        GamesContract.GameEntry.COLUMN_SPORT_ID + " INTEGER NOT NULL, " +
                        GamesContract.GameEntry.COLUMN_TIME + " TEXT NOT NULL, " +
                        GamesContract.GameEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                        GamesContract.GameEntry.COLUMN_LOCATION_ID + " INTEGER NOT NULL, " +
                        GamesContract.GameEntry.COLUMN_SHORT_DESC + " TEXT NOT NULL, " +
                        GamesContract.GameEntry.COLUMN_PEOPLE_NEEDED + " INTEGER NOT NULL, " +
                        GamesContract.GameEntry.COLUMN_ORGANIZER_ID + " INTEGER NOT NULL, " +
                        "FOREIGN KEY (" + GamesContract.GameEntry.COLUMN_SPORT_ID + ") " +
                            "REFERENCES " + GamesContract.SportEntry.TABLE_NAME + "(" + GamesContract.SportEntry._ID + ")," +
                        "FOREIGN KEY (" + GamesContract.GameEntry.COLUMN_ORGANIZER_ID + ") " +
                            "REFERENCES " + GamesContract.UserEntry.TABLE_NAME + "(" + GamesContract.UserEntry._ID + ")," +
                        "FOREIGN KEY (" + GamesContract.GameEntry.COLUMN_LOCATION_ID + ") " +
                            "REFERENCES " + GamesContract.LocationEntry.TABLE_NAME + "(" + GamesContract.LocationEntry._ID + ")" +
                        ");";

        //Create tables in sqlite
        sqLiteDatabase.execSQL(SQL_CREATE_LOCATION_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_SPORT_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_GAME_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_PARTICIPATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GamesContract.ParticipateEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GamesContract.GameEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GamesContract.UserEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GamesContract.SportEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + GamesContract.LocationEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
