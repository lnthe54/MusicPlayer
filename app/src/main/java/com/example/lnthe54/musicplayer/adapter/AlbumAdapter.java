package com.example.lnthe54.musicplayer.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lnthe54.musicplayer.R;
import com.example.lnthe54.musicplayer.model.Albums;

import java.util.List;

/**
 * @author lnthe54 on 8/20/2018
 * @project MusicPlayer
 */
public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.ViewHolder> {
    private List<Albums> listAlbums;
    private onCallBack clickAlbum;

    public AlbumAdapter(onCallBack clickAlbum, List<Albums> listAlbums) {
        this.clickAlbum = clickAlbum;
        this.listAlbums = listAlbums;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_album, parent, false);
        return new ViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Albums albums = listAlbums.get(position);
        holder.tvNameAlbum.setText(albums.getNameAlbum());
        holder.tvAuthor.setText(albums.getAuthor());
        holder.ivAlbum.setImageResource(albums.getImage());

    }

    @Override
    public int getItemCount() {
        if (listAlbums != null) {
            return listAlbums.size();
        }
        return 0;
    }

    public interface onCallBack {
        void onClickAlbum(int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvNameAlbum, tvAuthor;
        ImageView ivAlbum;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNameAlbum = itemView.findViewById(R.id.tv_album);
            tvAuthor = itemView.findViewById(R.id.tv_authorAlbum);
            ivAlbum = itemView.findViewById(R.id.iv_album);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clickAlbum.onClickAlbum(getAdapterPosition());
                }
            });
        }
    }
}
