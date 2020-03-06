package com.android.music.Adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class IRPopHistoryMusicRecyclerAdapter extends RecyclerView.Adapter<IRPopHistoryMusicRecyclerAdapter.PopMusicViewHolder> {

    @NonNull
    @Override
    public PopMusicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull PopMusicViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    static class PopMusicViewHolder extends RecyclerView.ViewHolder {
        public PopMusicViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
