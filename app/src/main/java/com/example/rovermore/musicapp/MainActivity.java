package com.example.rovermore.musicapp;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    String[] projection = new String[] {
            MusicContract.MusicEntry._ID,
            MusicContract.MusicEntry.ARTIST,
            MusicContract.MusicEntry.SONG,
            MusicContract.MusicEntry.ALBUM
    };
    MusicCursorAdapter musicCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertDummyData();

        ListView listItemView = findViewById(R.id.list_item);

        listItemView.setAdapter(musicCursorAdapter);

        getSupportLoaderManager().initLoader(0, null, this);

    }

    public void insertDummyData(){

        ContentValues contentValues = new ContentValues();
        contentValues.put(MusicContract.MusicEntry.ARTIST, "Oasis");
        contentValues.put(MusicContract.MusicEntry.SONG,"Morning Glory");
        contentValues.put(MusicContract.MusicEntry.ALBUM,"Morning glory");

        getBaseContext().getContentResolver().insert(MusicContract.MusicEntry.CONTENT_URI,contentValues);

    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

       return new CursorLoader(
               this,
               MusicContract.MusicEntry.CONTENT_URI,
               projection,
               null,
               null,
               null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        musicCursorAdapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        musicCursorAdapter.swapCursor(null);
    }

}
