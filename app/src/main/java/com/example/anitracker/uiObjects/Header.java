package com.example.anitracker.uiObjects;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.anitracker.R;

public class Header {
    private final String header;

    public Header(String header){
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public static class HeaderView extends RecyclerView.ViewHolder {
        public TextView headerText;
        public HeaderView(@NonNull View itemView) {
            super(itemView);
            headerText = itemView.findViewById(R.id.header);
        }
    }
}
