package com.duse.android.connectandplay.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mahesh Gaya on 10/20/16.
 */

public class GamesContract {
    public static final String CONTENT_AUTHORITY = "com.duse.android.connectandplay.app";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Constants for paths
    public static final String PATH_GAME = "game";
    public static final String PATH_USER = "user";
    public static final String PATH_SPORT = "sport";
    public static final String PATH_PARTICIPATE = "participate";
    public static final String PATH_LOCATION = "location";

    public static final class GameEntry implements BaseColumns{
        //defining types and path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_GAME).build();
        //dir type
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAME;
        //item type
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_GAME;

        //Define tables
        //table names
        public static final String TABLE_NAME = "game";
        //Columns
        public static final String COLUMN_GAME_NAME = "game_name";
        public static final String COLUMN_SPORT_ID = "sport_id"; //foreign key
        public static final String COLUMN_TIME = "time";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_LOCATION_ID = "location_id";
        public static final String COLUMN_SHORT_DESC = "description";
        public static final String COLUMN_PEOPLE_NEEDED = "people_needed";
        public static final String COLUMN_ORGANIZER_ID = "user_id";



        //building the paths
        //content://game/[_id]
        public static Uri buildGameUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        //content://game/user/[user_id]
        public static Uri buildGameUserUri(long userId){
            return CONTENT_URI.buildUpon().appendPath(UserEntry.TABLE_NAME)
                    .appendPath(Long.toString(userId)).build();
        }

        //content://game/sport/[sport_id]
        public static Uri buildGameSportUri(long sportId){
            return CONTENT_URI.buildUpon().appendPath(SportEntry.TABLE_NAME)
                    .appendPath(Long.toString(sportId)).build();
        }


        public static String getGameIdFromUri(Uri uri) {
            return uri.getLastPathSegment();
        }

        public static String getSportIdFromUri(Uri uri){
            return uri.getLastPathSegment();
        }

        public static String getUserIdFromUri(Uri uri){
            return uri.getLastPathSegment();
        }

    }

    public static final class UserEntry implements BaseColumns{
        //defining types and path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();
        //dir type
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        //item type
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;

        //Define table
        //table name
        public static final String TABLE_NAME = "user";
        //Columns
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_FIRST_NAME = "first_name";
        public static final String COLUMN_LAST_NAME = "last_name";
        public static final String COLUMN_BIOGRAPHY = "biography";
        public static final String COLUMN_CURRENT_USER = "current_user"; //1 or 0

        //building the paths
        public static Uri buildUserUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class SportEntry implements BaseColumns{
        //defining types and path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_SPORT).build();
        //dir type
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SPORT;
        //item type
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_SPORT;

        //Define table
        //table name
        public static final String TABLE_NAME = "sport";
        //columns
        public static final String COLUMN_SPORT_NAME = "sport_name";

        //building the paths
        public static Uri buildSportUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class ParticipateEntry implements BaseColumns{
        //defining types and path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_PARTICIPATE).build();
        //dir type
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PARTICIPATE;
        //item type
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_PARTICIPATE;

        //Define table
        public static final String TABLE_NAME = "participate";
        public static final String COLUMN_GAME_ID = "game_id";

        //building the paths
        //content://authority/participate/[_id]
        public static Uri buildParticipateUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
        //content://authority/participate/game/[game_id]
        public static Uri buildParticipateGameUri(long gameId){
            return CONTENT_URI.buildUpon().appendPath(GameEntry.TABLE_NAME)
                    .appendPath(Long.toString(gameId)).build();
        }

        //content://authority/participate/games
        public static Uri buildParticipateGamesUri(){
            return CONTENT_URI.buildUpon().appendPath(GameEntry.TABLE_NAME + "s").build();
        }

        public static String getGameIdFromUri(Uri uri){
            return uri.getLastPathSegment();
        }
        

    }

    public static final class LocationEntry implements BaseColumns{
        //defining types and path
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_LOCATION).build();
        //dir type
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;
        //item type
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_LOCATION;

        //Define table
        public static final String TABLE_NAME = "location";
        public static final String COLUMN_ADDRESS = "address";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";

        //content://authority/location/[_id]
        public static Uri buildLocationUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

    }
}
