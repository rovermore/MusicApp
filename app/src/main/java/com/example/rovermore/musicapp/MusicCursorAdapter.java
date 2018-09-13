package com.example.rovermore.musicapp;

import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MusicCursorAdapter extends CursorAdapter {

    public MusicCursorAdapter(Context context, Cursor c) {
        super(context, c,0);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View row = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

        return row;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        final TextView artistView = view.findViewById(R.id.artist_name);
        final TextView songView = view.findViewById(R.id.song_name);
        TextView albumView = view.findViewById(R.id.album_name);

        String artistText = cursor.getString(cursor.getColumnIndexOrThrow(MusicContract.MusicEntry.ARTIST));
        String songText = cursor.getString(cursor.getColumnIndexOrThrow(MusicContract.MusicEntry.SONG));
        String albumText = cursor.getString(cursor.getColumnIndexOrThrow(MusicContract.MusicEntry.ALBUM));

        artistView.setText(artistText);
        songView.setText(songText);
        albumView.setText(albumText);

        int position = cursor.getPosition();
        final long id = getItemId(position);

        Button play = (Button) view.findViewById(R.id.play_button_main);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String search = searchBuilder(artistView, songView);

                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, search);
                context.startActivity(intent);
            }
        });

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set an intent to the item clicked in the list
                Uri uri = ContentUris.withAppendedId(MusicContract.MusicEntry.CONTENT_URI,id);
                Intent intent = new Intent(context,DetailActivity.class);
                intent.setData(uri);
                context.startActivity(intent);
            }
        });
    }

    private String searchBuilder(TextView artist, TextView song) {

        StringBuilder searchBuilder = new StringBuilder();
        String mArtist = String.valueOf(artist.getText());
        String mSong = String.valueOf(song.getText());
        searchBuilder.append(mArtist);
        searchBuilder.append(" - ");
        searchBuilder.append(mSong);

        String search = searchBuilder.toString();

        return search;

    }
}
