package com.example.lnthe54.musicplayer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.model.Artists;

import java.util.List;

/**
 * @author lnthe54 on 8/21/2018
 * @project MusicPlayer
 */
public class ArtistsAdapter extends RecyclerView.Adapter<ArtistsAdapter.ViewHolder> {

    private List<Artists> listArtist;

    public ArtistsAdapter(List<Artists> listArtist) {
        this.listArtist = listArtist;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_artists, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Artists artists = listArtist.get(position);
        holder.tvNameArtist.setText(artists.getNameArtist());
        holder.tvInforArtist.setText(artists.getInforArtist());
    }

    @Override
    public int getItemCount() {
        if (listArtist != null) {
            return listArtist.size();
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameArtist, tvInforArtist;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tvNameArtist = itemView.findViewById(R.id.tv_artists_name);
            tvInforArtist = itemView.findViewById(R.id.tv_infor_artists);
        }
    }
}
