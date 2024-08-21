package com.example.anitracker.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;
import com.example.anitracker.interfaces.RecyclerViewInterface;
import com.example.anitracker.mediaObjects.Tag;

import java.util.List;
import java.util.Locale;

public class TagsViewAdapter extends RecyclerView.Adapter<TagsViewAdapter.MyViewHolder> {
    public List<Tag> tags;
    Context context;
    RecyclerViewInterface recyclerViewInterface;

    public TagsViewAdapter(List<Tag> tags, Context context, RecyclerViewInterface recyclerViewInterface) {
        this.tags = tags;
        this.context = context;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    public void setTags(List<Tag> tags){
        this.tags = tags;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TagsViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tag_view_layout, parent, false);
        return new TagsViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TagsViewAdapter.MyViewHolder holder, int position) {
        holder.tagName.setText(tags.get(position).getTagName());
        holder.tagRanking.setText(String.format(Locale.ENGLISH,"%d%%", tags.get(position).getTagRanking()));
        if(tags.get(position).getSpoiler()){
            holder.tagName.setTextColor(ContextCompat.getColor(context, R.color.red));
            holder.tagRanking.setTextColor(ContextCompat.getColor(context, R.color.red));
        }
        else {
            holder.tagName.setTextColor(ContextCompat.getColor(context, R.color.white));
            holder.tagRanking.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        ConstraintLayout mainLayout;
        TextView tagName, tagRanking;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tagName = itemView.findViewById(R.id.tagName);
            tagRanking = itemView.findViewById(R.id.tagRanking);
            mainLayout = itemView.findViewById(R.id.tagLayout);
            itemView.setOnClickListener(view -> {
                int pos = getBindingAdapterPosition();
                if(pos != RecyclerView.NO_POSITION){
                    recyclerViewInterface.onItemClick(pos);
                }
            });
        }
    }
}
