package com.example.rovermore.musicapp;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>{

    EditText artistEditText;
    EditText songEditText;
    EditText albumEditText;
    Button playButton;

    String[] projection = new String[] {
            MusicContract.MusicEntry._ID,
            MusicContract.MusicEntry.ARTIST,
            MusicContract.MusicEntry.SONG,
            MusicContract.MusicEntry.ALBUM
    };

    Uri uri;
    private boolean musicHasBeenTouched = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);

        View.OnTouchListener mTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                musicHasBeenTouched = true;
                return false;
            }
        };
        setViews();
        setmTouchListener(mTouchListener);
        createDetailContent();
    }

    public void setmTouchListener(View.OnTouchListener mTouchListener){
        albumEditText.setOnTouchListener(mTouchListener);
        songEditText.setOnTouchListener(mTouchListener);
        artistEditText.setOnTouchListener(mTouchListener);
    }
    public void setViews(){
        artistEditText = findViewById(R.id.artist_detail);
        songEditText = findViewById(R.id.song_detail);
        albumEditText = findViewById(R.id.album_detail);
        playButton = findViewById(R.id.play_button);
    }

    public void createDetailContent(){
        Intent intent = getIntent();
        uri = intent.getData();

        if(uri!=null){
            setTitle("Edit Music");
            getLoaderManager().initLoader(0,null,this);

        }else{
            setTitle("New Music");
        }
    }

    public void insertMusic(){

        String artist = artistEditText.getText().toString().trim();
        String song = songEditText.getText().toString().trim();
        String album = albumEditText.getText().toString().trim();

        if(TextUtils.isEmpty(album)){
            album= "Unknown";
        }

        ContentValues contentValues = new ContentValues();
        contentValues.put(MusicContract.MusicEntry.ARTIST,artist);
        contentValues.put(MusicContract.MusicEntry.SONG,song);
        contentValues.put(MusicContract.MusicEntry.ALBUM,album);

        if(!TextUtils.isEmpty(song) && !TextUtils.isEmpty(artist)){

            if(uri!=null){

                int rowsUpdated= getContentResolver().update(uri,contentValues,null,null);

                getContentResolver().notifyChange(uri,null);
            }else {

                Uri newMusicInserted = getContentResolver().insert(MusicContract.MusicEntry.CONTENT_URI, contentValues);

                getContentResolver().notifyChange(newMusicInserted, null);
            }

        }else{
            Toast.makeText(this,"couldn't insert music, fill artist and song",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.save:
                //do the insert in the database ans finish activity
                insertMusic();
                finish();
                return true;
            case R.id.delete_detail:
                //do delete entry from database
                if (uri != null) {
                    deleteDialog();
                    return true;
                }
                return true;
            case android.R.id.home:
                //checks if any field was changed and opens dialog in case is not saved
                if (!musicHasBeenTouched) {
                    NavUtils.navigateUpFromSameTask(DetailActivity.this);
                    return true;
                }

                DialogInterface.OnClickListener discardDialog = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        NavUtils.navigateUpFromSameTask(DetailActivity.this);
                    }
                };
                notSavedDialog(discardDialog);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {
        if(!musicHasBeenTouched){
        super.onBackPressed();
        return;
        }
        DialogInterface.OnClickListener discardDialog = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        };
        notSavedDialog(discardDialog);
    }

    private void notSavedDialog(DialogInterface.OnClickListener discardDialog) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Quit without save changes?");
        builder.setNegativeButton("Keep editing", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface!=null){
                    dialogInterface.dismiss();
                }
            }
        });
        builder.setPositiveButton("Quit", discardDialog);

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void deleteDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Do you wanna delete it?");
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface!=null){
                    dialogInterface.dismiss();
                }
            }
        });
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(dialogInterface!=null){
                    deleteMusic();
                    finish();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    public void deleteMusic(){
        int rowsDeleted = getContentResolver().delete(uri,null,null);
        Toast.makeText(this,"Music selection deleted",Toast.LENGTH_SHORT).show();
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        CursorLoader cursorLoader= new CursorLoader(getBaseContext(),uri,projection,null,null,null);
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

        if (cursor.moveToFirst() == false){
            //cursor is empty
            return;
        }
        artistEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(MusicContract.MusicEntry.ARTIST)));
        songEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(MusicContract.MusicEntry.SONG)));
        albumEditText.setText(cursor.getString(cursor.getColumnIndexOrThrow(MusicContract.MusicEntry.ALBUM)));

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        artistEditText.setText(null);
        songEditText.setText(null);
        albumEditText.setText(null);
    }
}
