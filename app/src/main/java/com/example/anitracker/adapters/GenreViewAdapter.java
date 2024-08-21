package com.example.anitracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;

import java.util.List;

public class GenreViewAdapter extends RecyclerView.Adapter<GenreViewAdapter.MyViewHolder> {
    Context context;
    List<String> genreList;

    public GenreViewAdapter(List<String> genreList, Context context) {
        this.genreList = genreList;
        this.context = context;
    }

    @NonNull
    @Override
    public GenreViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.genre_view_layout, parent, false);
        return new GenreViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GenreViewAdapter.MyViewHolder holder, int position) {
        holder.genre.setText(genreList.get(position));
    }

    @Override
    public int getItemCount() {
        return genreList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView genre;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            genre = itemView.findViewById(R.id.genreName);
        }
    }
}
