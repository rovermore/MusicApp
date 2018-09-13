package com.example.rovermore.musicapp;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

public class MusicProvider extends ContentProvider {

    private MusicDbHelper musicDbHelper;

    private final static int MUSICS = 101;
    private final static int MUSIC_ID = 102;

    private final static UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {

        sUriMatcher.addURI(MusicContract.CONTENT_AUTHORITY,MusicContract.PATH_MUSIC,MUSICS);
        sUriMatcher.addURI(MusicContract.CONTENT_AUTHORITY, MusicContract.PATH_MUSIC + "/#", MUSIC_ID);
    }

    @Override
    public boolean onCreate() {

        musicDbHelper = new MusicDbHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection,
                        @Nullable String[] selectionArgs, @Nullable String sortOrder) {

        Cursor cursor;

        SQLiteDatabase database = musicDbHelper.getReadableDatabase();

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MUSICS:

                cursor = database.query(MusicContract.MusicEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;

            case MUSIC_ID:

                selection = MusicContract.MusicEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                cursor = database.query(MusicContract.MusicEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {

        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MUSICS:
                return MusicContract.MusicEntry.CONTENT_LIST_TYPE;
            case MUSIC_ID:
                return MusicContract.MusicEntry.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);
        }
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {

        if (areContentValuesValidated(contentValues)) {

            SQLiteDatabase database = musicDbHelper.getWritableDatabase();

            long idNewRow;

            final int match = sUriMatcher.match(uri);

            switch (match) {

                case MUSICS:

                    idNewRow = database.insert(MusicContract.MusicEntry.TABLE_NAME, null, contentValues);

                    getContext().getContentResolver().notifyChange(uri,null);

                    return ContentUris.withAppendedId(MusicContract.MusicEntry.CONTENT_URI, idNewRow);

                default:
                    throw new IllegalStateException("Unknown URI " + uri + " with match " + match);

            }
        }

        throw new IllegalStateException("Values not validated");
    }

    private boolean areContentValuesValidated(ContentValues contentValues){

        if (contentValues.containsKey(MusicContract.MusicEntry.ARTIST)){
            String artist = contentValues.getAsString(MusicContract.MusicEntry.ARTIST);
            if(TextUtils.isEmpty(artist)){
                throw new IllegalArgumentException("Artist name required");
            }
        }

        if (contentValues.containsKey(MusicContract.MusicEntry.SONG)){
            String song = contentValues.getAsString(MusicContract.MusicEntry.SONG);
            if(TextUtils.isEmpty(song)){
                throw new IllegalArgumentException("Song name required");
            }
        }

        if (contentValues.containsKey(MusicContract.MusicEntry.ALBUM)){
            String album = contentValues.getAsString(MusicContract.MusicEntry.ALBUM);
            if(TextUtils.isEmpty(album)){
                throw new IllegalArgumentException("Album name required");
            }
        }

        return true;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {

        int rowsDeleted;

        SQLiteDatabase database = musicDbHelper.getWritableDatabase();

        final int match = sUriMatcher.match(uri);

        switch (match) {

            case MUSICS:

                rowsDeleted = database.delete(MusicContract.MusicEntry.TABLE_NAME, selection, selectionArgs);

                getContext().getContentResolver().notifyChange(uri,null);

                return rowsDeleted;

            case MUSIC_ID:

                selection = MusicContract.MusicEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                rowsDeleted = database.delete(MusicContract.MusicEntry.TABLE_NAME, selection, selectionArgs);

                getContext().getContentResolver().notifyChange(uri,null);

                return rowsDeleted;

            default:
                throw new IllegalStateException("Unknown URI " + uri + " with match " + match);

        }

    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues,
                      @Nullable String selection, @Nullable String[] selectionArgs) {



        if(areContentValuesValidated(contentValues)){

            int rowsUpdated;

            SQLiteDatabase database = musicDbHelper.getWritableDatabase();

            int match = sUriMatcher.match(uri);

            switch (match){

                case MUSICS:
                    rowsUpdated = database.update(MusicContract.MusicEntry.TABLE_NAME,contentValues,selection,selectionArgs);

                    getContext().getContentResolver().notifyChange(uri,null);

                    return rowsUpdated;
                case MUSIC_ID:

                    selection = MusicContract.MusicEntry._ID + "=?";
                    selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                    rowsUpdated = database.update(MusicContract.MusicEntry.TABLE_NAME,contentValues,selection,selectionArgs);

                    getContext().getContentResolver().notifyChange(uri,null);

                    return rowsUpdated;
            }
        }

        throw new IllegalStateException("Values are not valid");
    }
}
