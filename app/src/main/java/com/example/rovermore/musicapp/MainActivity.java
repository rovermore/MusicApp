package com.example.rovermore.musicapp;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    String[] projection = new String[] {
            MusicContract.MusicEntry._ID,
            MusicContract.MusicEntry.ARTIST,
            MusicContract.MusicEntry.SONG,
            MusicContract.MusicEntry.ALBUM
    };
    MusicCursorAdapter musicCursorAdapter;

    MusicDbHelper musicDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        insertDummyData();

        musicDbHelper = new MusicDbHelper(this);

        musicCursorAdapter = new MusicCursorAdapter(this, null);

        ListView listItemView = findViewById(R.id.list_view);

        listItemView.setAdapter(musicCursorAdapter);

        listItemView.setEmptyView(findViewById(R.id.empty_view));

        getSupportLoaderManager().initLoader(0, null, this);

    }

    public void insertDummyData(){

        ContentValues contentValues = new ContentValues();
        contentValues.put(MusicContract.MusicEntry.ARTIST, "Soriasis");
        contentValues.put(MusicContract.MusicEntry.SONG,"Morning Glory");
        contentValues.put(MusicContract.MusicEntry.ALBUM,"Whats the story");

        Uri newMusicIserted = getBaseContext().getContentResolver().insert(MusicContract.MusicEntry.CONTENT_URI,contentValues);

        Toast.makeText(this, "Music added in your library", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case R.id.insert_dummy_data:
                insertDummyData();
                return true;
            case R.id.delete_all:
                showDeleteAllDialog();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showDeleteAllDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you want to delete all entries?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface!=null){
                dialogInterface.dismiss();
                }

            }
        } );
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface!=null){

                    deleteAllMusic();
                }
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private int deleteAllMusic() {

        int rowsDeleted = getContentResolver().delete(MusicContract.MusicEntry.CONTENT_URI,null,null);

        Toast.makeText(this, "Rows deleted" + rowsDeleted,Toast.LENGTH_SHORT).show();

        return rowsDeleted;
    }


}
