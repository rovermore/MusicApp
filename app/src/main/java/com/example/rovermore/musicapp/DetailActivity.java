package com.example.rovermore.musicapp;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class DetailActivity extends AppCompatActivity{

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_activity);
        setViews();
    }

    public void setViews(){
        artistEditText = findViewById(R.id.artist_detail);
        songEditText = findViewById(R.id.song_detail);
        albumEditText = findViewById(R.id.album_detail);
        playButton = findViewById(R.id.play_button);
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

            Uri newMusicInserted = getContentResolver().insert(MusicContract.MusicEntry.CONTENT_URI,contentValues);

            getContentResolver().notifyChange(newMusicInserted,null);

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
        switch (itemId){
            case R.id.save:
                //do the insert in the database ans finish activity
                insertMusic();
                finish();
            case R.id.delete_detail:
                //do delete entry from database
        }
        return super.onOptionsItemSelected(item);
    }
}
