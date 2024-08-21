package com.example.anitracker.mediaObjects;

import androidx.annotation.NonNull;

import java.util.Locale;

public class Date {
    private final int year, month, day;

    public Date(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public int getDay() {
        return day;
    }


    @Override
    @NonNull
    public String toString() {
        String[] months = {"Jan", "Feb", "Mar", "Apr",
                "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec"};

        String dateString = String.valueOf(this.year);

        if(this.month != -1){
            dateString += String.format(" %s", months[this.month-1]);
        }

        if(this.day != -1){
            dateString += String.format(Locale.ENGLISH, " %d", this.day);
        }

        return dateString;
    }
}
