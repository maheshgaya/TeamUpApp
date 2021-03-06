package com.duse.android.connectandplay.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by Mahesh Gaya on 10/20/16.
 */

public class GamesProvider  extends ContentProvider {
    private static final String TAG = GamesProvider.class.getSimpleName();
    private GamesDbHelper mOpenDbHelper;
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    //code for UriMatcher
    private static final int GAME = 100; //dir
    private static final int GAME_WITH_ID = 101; //inner join item
    private static final int GAME_WITH_USER_ID = 102; //inner join dir
    private static final int GAME_WITH_SPORT_ID = 103; //inner join dir

    private static final int USER = 200; //dir
    private static final int USER_WITH_ID = 201; //item
    private static final int USER_CURRENT = 202; //item

    private static final int SPORT = 300; //dir
    private static final int SPORT_WITH_ID = 301; //item

    private static final int PARTICIPATE = 400; //dir
    private static final int PARTICIPATE_WITH_ID = 401; //item
    private static final int PARTICIPATE_WITH_GAME_ID = 402; //item
    private static final int PARTICIPATE_GAME_SPORT_USER_LOCATION = 403; //dir join with sport, game, user, location and participate

    private static final int LOCATION = 500; //dir


    //inner joins
    private static final SQLiteQueryBuilder sGameSportUserLocationQueryBuilder; //inner join with sport and game and user and location
    private static final SQLiteQueryBuilder sGameParticipateQueryBuilder; //inner join with game and participate
    private static final SQLiteQueryBuilder sParticipateGameSportUserLocationQueryBuilder;

    static {
        sGameSportUserLocationQueryBuilder = new SQLiteQueryBuilder();
        /**
         * game INNER JOIN sport ON game.sport_id = sport._id INNER JOIN user ON user._id = user_id
         * INNER JOIN location ON game.location_id = location_id
         */
        sGameSportUserLocationQueryBuilder.setTables(
                GamesContract.GameEntry.TABLE_NAME + " INNER JOIN " +
                        GamesContract.SportEntry.TABLE_NAME +
                        " ON " + GamesContract.GameEntry.TABLE_NAME +
                        "." + GamesContract.GameEntry.COLUMN_SPORT_ID +
                        " = " + GamesContract.SportEntry.TABLE_NAME +
                        "." + GamesContract.SportEntry._ID +
                        " INNER JOIN " + GamesContract.UserEntry.TABLE_NAME +
                        " ON " + GamesContract.UserEntry.TABLE_NAME +
                        "." + GamesContract.UserEntry._ID +
                        " = " + GamesContract.GameEntry.COLUMN_ORGANIZER_ID +
                        " INNER JOIN " + GamesContract.LocationEntry.TABLE_NAME +
                        " ON " + GamesContract.LocationEntry.TABLE_NAME +
                        "." + GamesContract.LocationEntry._ID +
                        " = " + GamesContract.GameEntry.COLUMN_LOCATION_ID

        );
    }

    //game._id = ?
    private  static final String sGameGameIdSelection =
            GamesContract.GameEntry.TABLE_NAME +
                    "." + GamesContract.GameEntry._ID + " = ? ";

    /**
     * gets game data by game id
     * @param uri
     * @param projection
     * @param sortOrder
     * @return cursor
     */
    private Cursor getGamebyGameId(Uri uri, String[] projection, String sortOrder){
        String gameId = GamesContract.GameEntry.getGameIdFromUri(uri);
        String selection = sGameGameIdSelection;
        String[] selectionArgs = new String[]{gameId};
        return sGameSportUserLocationQueryBuilder.query(
                mOpenDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }
    //game.user_id = ?
    private static final String sGameUserIdSelection =
            GamesContract.GameEntry.TABLE_NAME +
                    "." + GamesContract.GameEntry.COLUMN_ORGANIZER_ID + " = ? ";

    /**
     * gets game data by user id
     * @param uri
     * @param projection
     * @param sortOrder
     * @return cursor
     */
    private Cursor getGameByUserId(Uri uri, String[] projection, String sortOrder) {
        String userId = GamesContract.GameEntry.getUserIdFromUri(uri);
        String selection = sGameUserIdSelection;
        String[] selectionArgs = new String[]{userId};
        return sGameSportUserLocationQueryBuilder.query(
                mOpenDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    //game.sport_id = ?
    private static final String sGameSportIdSelection =
            GamesContract.GameEntry.TABLE_NAME +
                    "." + GamesContract.GameEntry.COLUMN_SPORT_ID + " = ? ";

    /**
     * gets game data by sport
     * @param uri
     * @param projection
     * @param sortOrder
     * @return cursor
     */
    private Cursor getGameBySportId(Uri uri, String[] projection, String sortOrder) {
        String sportId = GamesContract.GameEntry.getSportIdFromUri(uri);
        String selection = sGameSportIdSelection;
        String[] selectionArgs = new String[]{sportId};
        Cursor cursor = sGameSportUserLocationQueryBuilder.query(
                mOpenDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
        return cursor;
    }


    static {
        sGameParticipateQueryBuilder = new SQLiteQueryBuilder();
        /**
         * game INNER JOIN participate ON game._id = participate.game_id
         */
        sGameParticipateQueryBuilder.setTables(
                GamesContract.GameEntry.TABLE_NAME + " INNER JOIN " +
                        GamesContract.ParticipateEntry.TABLE_NAME +
                        " ON " + GamesContract.GameEntry.TABLE_NAME +
                        "." + GamesContract.GameEntry._ID +
                        " = " + GamesContract.ParticipateEntry.TABLE_NAME +
                        "." + GamesContract.ParticipateEntry.COLUMN_GAME_ID
        );

    }

    //game._id = ?
    private static final String sParticipateGameIdSelection =
            GamesContract.GameEntry.TABLE_NAME +
                    "." + GamesContract.GameEntry._ID + " = ? ";

    /**
     * gets participate data by game id
     * @param uri
     * @param projection
     * @param sortOrder
     * @return cursor
     */
    private Cursor getParticipateByGameId(Uri uri, String[] projection, String sortOrder){
        String gameId = GamesContract.ParticipateEntry.getGameIdFromUri(uri);
        String selection = sParticipateGameIdSelection;
        String[] selectionArgs = new String[]{gameId};
        return sGameParticipateQueryBuilder.query(
                mOpenDbHelper.getReadableDatabase(),
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    static{
        sParticipateGameSportUserLocationQueryBuilder = new SQLiteQueryBuilder();
        /**
         * game INNER JOIN sport ON game.sport_id = sport._id INNER JOIN user ON user._id = game.user_id
         * INNER JOIN location ON location._id = location_id INNER JOIN participate ON game._id = participate.game_id
         */
        sParticipateGameSportUserLocationQueryBuilder.setTables(
                GamesContract.GameEntry.TABLE_NAME + " INNER JOIN " +
                        GamesContract.SportEntry.TABLE_NAME +
                        " ON " + GamesContract.GameEntry.TABLE_NAME +
                        "." + GamesContract.GameEntry.COLUMN_SPORT_ID +
                        " = " + GamesContract.SportEntry.TABLE_NAME +
                        "." + GamesContract.SportEntry._ID +
                        " INNER JOIN " + GamesContract.UserEntry.TABLE_NAME +
                        " ON " + GamesContract.UserEntry.TABLE_NAME +
                        "." + GamesContract.UserEntry._ID +
                        " = " + GamesContract.GameEntry.TABLE_NAME +
                        "." +GamesContract.GameEntry.COLUMN_ORGANIZER_ID +
                        " INNER JOIN " + GamesContract.LocationEntry.TABLE_NAME +
                        " ON " + GamesContract.LocationEntry.TABLE_NAME +
                        "." + GamesContract.LocationEntry._ID +
                        " = " + GamesContract.GameEntry.COLUMN_LOCATION_ID +
                        " INNER JOIN " + GamesContract.ParticipateEntry.TABLE_NAME +
                        " ON " + GamesContract.GameEntry.TABLE_NAME +
                        "." + GamesContract.GameEntry._ID +
                        " = " + GamesContract.ParticipateEntry.TABLE_NAME +
                        "." + GamesContract.ParticipateEntry.COLUMN_GAME_ID
        );
    }

    /**
     * gets all paricipate data (inner join among all the tables)
     * @param uri
     * @param projection
     * @param sortOrder
     * @return cursor
     */
    private Cursor getParticipateGameSportUserLocation(Uri uri, String[] projection, String sortOrder){
        return sParticipateGameSportUserLocationQueryBuilder.query(
                mOpenDbHelper.getReadableDatabase(),
                projection,
                null,
                null,
                null,
                null,
                sortOrder
        );
    }

    private static UriMatcher buildUriMatcher() {
        //initialize uri matcher
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = GamesContract.CONTENT_AUTHORITY;
        //add each uri
        //content://authority/game
        matcher.addURI(authority, GamesContract.GameEntry.TABLE_NAME, GAME);
        //content://authority/game/#
        matcher.addURI(authority, GamesContract.GameEntry.TABLE_NAME + "/#", GAME_WITH_ID);
        //content://authority/game/sport/#
        matcher.addURI(authority, GamesContract.GameEntry.TABLE_NAME +
                "/" + GamesContract.SportEntry.TABLE_NAME + "/#", GAME_WITH_SPORT_ID);
        //content://authority/game/user/#
        matcher.addURI(authority, GamesContract.GameEntry.TABLE_NAME +
                "/" + GamesContract.UserEntry.TABLE_NAME + "/#", GAME_WITH_USER_ID);

        //content://authority/user
        matcher.addURI(authority, GamesContract.UserEntry.TABLE_NAME, USER);
        //content://authority/user/#
        matcher.addURI(authority, GamesContract.UserEntry.TABLE_NAME + "/#", USER_WITH_ID);
        //content://authority/user/current_user
        matcher.addURI(authority, GamesContract.UserEntry.TABLE_NAME + "/" +
                GamesContract.UserEntry.COLUMN_CURRENT_USER, USER_CURRENT);

        //content://authority/sport
        matcher.addURI(authority, GamesContract.SportEntry.TABLE_NAME, SPORT);
        //content://authority/sport/#
        matcher.addURI(authority, GamesContract.SportEntry.TABLE_NAME + "/#", SPORT_WITH_ID);

        //content://authority/participate
        matcher.addURI(authority, GamesContract.ParticipateEntry.TABLE_NAME, PARTICIPATE);
        //content://authority/participate/#
        matcher.addURI(authority, GamesContract.ParticipateEntry.TABLE_NAME + "/#", PARTICIPATE_WITH_ID);
        //content://authority/participate/game/#
        matcher.addURI(authority, GamesContract.ParticipateEntry.TABLE_NAME +
                "/" + GamesContract.GameEntry.TABLE_NAME + "/#", PARTICIPATE_WITH_GAME_ID);
        //content://authority/participate/games
        matcher.addURI(authority, GamesContract.ParticipateEntry.TABLE_NAME +
            "/" + GamesContract.GameEntry.TABLE_NAME + "s", PARTICIPATE_GAME_SPORT_USER_LOCATION);

        //content://authority/location
        matcher.addURI(authority, GamesContract.LocationEntry.TABLE_NAME, LOCATION);
        return matcher;
    }

    /**
     * initializes the DBHelper
     * @return false
     */
    @Override
    public boolean onCreate() {
        mOpenDbHelper = new GamesDbHelper(getContext());
        return false;
    }

    /**
     * returns data requested to be queried
     * @param uri
     * @param projection
     * @param selection
     * @param selectionArgs
     * @param sortOrder
     * @return cursor
     */
    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case GAME:{
                retCursor = mOpenDbHelper.getReadableDatabase().query(
                        GamesContract.GameEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case GAME_WITH_ID:{
                retCursor = getGamebyGameId(uri, projection, sortOrder);
                break;
            }
            case GAME_WITH_SPORT_ID:{
                retCursor = getGameBySportId(uri, projection, sortOrder);
                break;
            }
            case GAME_WITH_USER_ID:{
                retCursor = getGameByUserId(uri, projection, sortOrder);
                break;
            }
            case SPORT:{
                retCursor = mOpenDbHelper.getReadableDatabase().query(
                        GamesContract.SportEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case SPORT_WITH_ID:{
                retCursor = mOpenDbHelper.getReadableDatabase().query(
                        GamesContract.SportEntry.TABLE_NAME,
                        projection,
                        GamesContract.SportEntry._ID + " = ? ",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case USER:{
                retCursor = mOpenDbHelper.getReadableDatabase().query(
                        GamesContract.UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case USER_WITH_ID:{
                retCursor = mOpenDbHelper.getReadableDatabase().query(
                        GamesContract.UserEntry.TABLE_NAME,
                        projection,
                        GamesContract.UserEntry._ID + " = ? ",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case USER_CURRENT:{
                retCursor = mOpenDbHelper.getReadableDatabase().query(
                        GamesContract.UserEntry.TABLE_NAME,
                        projection,
                        GamesContract.UserEntry.COLUMN_CURRENT_USER + " = ? ",
                        new String[]{String.valueOf(1)}, //current = True
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case PARTICIPATE:{
                retCursor = mOpenDbHelper.getReadableDatabase().query(
                        GamesContract.ParticipateEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case PARTICIPATE_WITH_ID:{
                retCursor = mOpenDbHelper.getReadableDatabase().query(
                        GamesContract.ParticipateEntry.TABLE_NAME,
                        projection,
                        GamesContract.ParticipateEntry._ID + " = ? ",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case PARTICIPATE_WITH_GAME_ID:{
                retCursor = getParticipateByGameId(uri, projection, sortOrder);
                break;
            }
            case PARTICIPATE_GAME_SPORT_USER_LOCATION:{
                retCursor = getParticipateGameSportUserLocation(uri, projection, sortOrder);
                break;
            }
            case LOCATION:{
                retCursor = mOpenDbHelper.getReadableDatabase().query(
                        GamesContract.LocationEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            default:{
                // Bad URI
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        //notifies changes
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    /**
     * get the type of each uri
     * @param uri
     * @return type
     */
    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (sUriMatcher.match(uri)) {
            case GAME: {
                return GamesContract.GameEntry.CONTENT_TYPE;
            }
            case GAME_WITH_ID: {
                return GamesContract.GameEntry.CONTENT_ITEM_TYPE;
            }
            case GAME_WITH_SPORT_ID: {
                return GamesContract.GameEntry.CONTENT_TYPE;
            }
            case GAME_WITH_USER_ID: {
                return GamesContract.GameEntry.CONTENT_TYPE;
            }
            case USER: {
                return GamesContract.UserEntry.CONTENT_TYPE;
            }
            case USER_WITH_ID: {
                return GamesContract.UserEntry.CONTENT_ITEM_TYPE;
            }
            case USER_CURRENT: {
                return GamesContract.UserEntry.CONTENT_ITEM_TYPE;
            }
            case SPORT: {
                return GamesContract.SportEntry.CONTENT_TYPE;
            }
            case SPORT_WITH_ID: {
                return GamesContract.SportEntry.CONTENT_ITEM_TYPE;
            }
            case PARTICIPATE: {
                return GamesContract.ParticipateEntry.CONTENT_TYPE;
            }
            case PARTICIPATE_WITH_ID: {
                return GamesContract.ParticipateEntry.CONTENT_ITEM_TYPE;
            }
            case PARTICIPATE_WITH_GAME_ID: {
                return GamesContract.ParticipateEntry.CONTENT_ITEM_TYPE;
            }
            case PARTICIPATE_GAME_SPORT_USER_LOCATION:{
                return GamesContract.ParticipateEntry.CONTENT_TYPE;
            }
            case LOCATION:{
                return GamesContract.LocationEntry.CONTENT_TYPE;
            }
        }
        return null;
    }

    /**
     * insert data into table
     * @param uri
     * @param contentValues
     * @return the single unique uri
     */
    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = mOpenDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;
        switch (match) {
            case GAME: {
                long _id = db.insert(GamesContract.GameEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = GamesContract.GameEntry.buildGameUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case SPORT: {
                long _id = db.insert(GamesContract.SportEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = GamesContract.SportEntry.buildSportUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case USER: {
                long _id = db.insert(GamesContract.UserEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = GamesContract.UserEntry.buildUserUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case PARTICIPATE: {
                long _id = db.insert(GamesContract.ParticipateEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = GamesContract.ParticipateEntry.buildParticipateUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            case LOCATION:{
                long _id = db.insert(GamesContract.LocationEntry.TABLE_NAME, null, contentValues);
                if (_id > 0) {
                    returnUri = GamesContract.LocationEntry.buildLocationUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        //notifies changes
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    /**
     * delete data into the database
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return how many records were deleted
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;
        // this makes delete all rows return the number of rows deleted
        if (null == selection) selection = "1";
        switch (match) {
            case GAME: {
                rowsDeleted = db.delete(
                        GamesContract.GameEntry.TABLE_NAME, selection, selectionArgs
                );
                break;
            }
            case SPORT: {
                rowsDeleted = db.delete(
                        GamesContract.SportEntry.TABLE_NAME, selection, selectionArgs
                );
                break;
            }
            case USER: {
                rowsDeleted = db.delete(
                        GamesContract.UserEntry.TABLE_NAME, selection, selectionArgs
                );
                break;
            }
            case PARTICIPATE: {
                rowsDeleted = db.delete(
                        GamesContract.ParticipateEntry.TABLE_NAME, selection, selectionArgs
                );
                break;
            }
            case LOCATION:{
                rowsDeleted = db.delete(
                        GamesContract.LocationEntry.TABLE_NAME, selection, selectionArgs
                );
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

        }

        // Because a null deletes all rows
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    /**
     * update data in database
     * @param uri
     * @param contentValues
     * @param selection
     * @param selectionArgs
     * @return how many records were updated
     */
    @Override
    public int update (Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mOpenDbHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case GAME: {
                rowsUpdated = db.update(
                        GamesContract.GameEntry.TABLE_NAME, contentValues, selection, selectionArgs
                );
                break;
            }
            case SPORT: {
                rowsUpdated = db.update(
                        GamesContract.SportEntry.TABLE_NAME, contentValues, selection, selectionArgs
                );
                break;
            }
            case USER: {
                rowsUpdated = db.update(
                        GamesContract.UserEntry.TABLE_NAME, contentValues, selection, selectionArgs
                );
                break;
            }
            case PARTICIPATE: {
                rowsUpdated = db.update(
                        GamesContract.ParticipateEntry.TABLE_NAME, contentValues, selection, selectionArgs
                );
                break;
            }
            case LOCATION:{
                rowsUpdated = db.update(
                        GamesContract.LocationEntry.TABLE_NAME, contentValues, selection, selectionArgs
                );
                break;
            }
            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }

        }

        // Because a null deletes all rows
        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsUpdated;
    }
}

