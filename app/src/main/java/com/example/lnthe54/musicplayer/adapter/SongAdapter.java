package com.example.lnthe54.musicplayer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.model.Songs;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lnthe54 on 8/20/2018
 * @project MusicPlayer
 */
public class SongAdapter extends RecyclerView.Adapter<SongAdapter.ViewHolder> {

    private List<Songs> listSong;
    private onCallBack clickSong;

    public SongAdapter(onCallBack clickSong, List<Songs> listSong) {
        this.clickSong = clickSong;
        this.listSong = listSong;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_song, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Songs songs = listSong.get(position);
        holder.tvNameSong.setText(songs.getNameSong());
        holder.tvAuthor.setText(songs.getAuthor());
    }

    @Override
    public int getItemCount() {
        if (listSong != null) {
            return listSong.size();
        }
        return 0;
    }

    public interface onCallBack {
        void onClickSong(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameSong, tvAuthor;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameSong = itemView.findViewById(R.id.tv_song);
            tvAuthor = itemView.findViewById(R.id.tv_author);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickSong.onClickSong(getAdapterPosition());
                }
            });
        }
    }

    public void updateList(List<Songs> newListSong) {
        listSong = new ArrayList<>();
        listSong.addAll(newListSong);
        notifyDataSetChanged();
    }
}
