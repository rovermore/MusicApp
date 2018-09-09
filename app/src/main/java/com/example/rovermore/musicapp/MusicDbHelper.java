package com.example.rovermore.musicapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MusicDbHelper extends SQLiteOpenHelper{

        private static final String TEXT_TYPE = " TEXT";
        private static final String COMMA = ",";
        private static final String NOT_NULL = " NOT NULL";
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + MusicContract.MusicEntry.TABLE_NAME + " (" +
                        MusicContract.MusicEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA +
                        MusicContract.MusicEntry.ARTIST + TEXT_TYPE + NOT_NULL + COMMA +
                        MusicContract.MusicEntry.ALBUM + TEXT_TYPE + NOT_NULL + COMMA +
                        MusicContract.MusicEntry.SONG + TEXT_TYPE + NOT_NULL +
                        " )";

        private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS " + MusicContract.MusicEntry.TABLE_NAME;

        private static final int DATABASE_VERSION = 1;
        private static final String DATABASE_NAME = "music.db";

    public MusicDbHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) { db.execSQL(SQL_CREATE_ENTRIES);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
    }
