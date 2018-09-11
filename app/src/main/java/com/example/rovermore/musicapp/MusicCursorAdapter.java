package com.example.rovermore.musicapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MusicCursorAdapter extends CursorAdapter {

    public MusicCursorAdapter(Context context, Cursor c) {
        super(context, c);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View row = LayoutInflater.from(context).inflate(R.layout.list_item, parent);

        return row;
    }

    @Override
    public void bindView(View view, final Context context, final Cursor cursor) {

        final TextView artistView = view.findViewById(R.id.artist_name);
        final TextView songView = view.findViewById(R.id.song_name);
        TextView albumView = view.findViewById(R.id.album_name);

        artistView.setText(cursor.getColumnIndexOrThrow(MusicContract.MusicEntry.ARTIST));
        songView.setText(cursor.getColumnIndexOrThrow(MusicContract.MusicEntry.SONG));
        albumView.setText(cursor.getColumnIndexOrThrow(MusicContract.MusicEntry.ALBUM));

        int position = cursor.getPosition();
        final long id = getItemId(position);

        Button play = (Button) view.findViewById(R.id.play_button);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String search = searchBuilder(artistView, songView);

                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, search);
                context.startActivity(intent);
            }
        });
    }

    private String searchBuilder(TextView artist, TextView song) {

        StringBuilder searchBuilder = (StringBuilder) artist.getText();
        searchBuilder.append(" - ");
        searchBuilder.append(song.getText());

        String search = searchBuilder.toString();

        return search;

    }
}
