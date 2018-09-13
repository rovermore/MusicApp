package com.example.rovermore.musicapp;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class MusicContract {

    private MusicContract(){

    }

    public static final String CONTENT_AUTHORITY = "com.example.rovermore.musicapp";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MUSIC = "music";

    public static class MusicEntry implements BaseColumns {

        public static final String TABLE_NAME = "music";
        public static final String _ID = BaseColumns._ID;
        public static final String ARTIST = "artist";
        public static final String SONG = "song";
        public static final String ALBUM = "album";

        //Constant to access the content URI for the table items
        public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_MUSIC);

        //The MIME type of the {@link #CONTENT_URI} for a list of items.
        public static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MUSIC;

        //The MIME type of the {@link #CONTENT_URI} for a single item.
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MUSIC;

    }
}
