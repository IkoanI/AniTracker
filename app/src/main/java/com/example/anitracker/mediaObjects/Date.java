package com.example.anitracker.mediaObjects;

import androidx.annotation.NonNull;

import com.example.anitracker.type.FuzzyDate;


public class Date {
    private final int year, month, day;

    private final String[] months = {"Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec"};

    public Date(Integer year, Integer month, Integer day) {
        this.year = year == null ? -1 : year;
        this.month = month == null ? -1 : month;
        this.day = day == null ? -1 : day;
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
        return String.format("%s%s%s",
                this.year == -1 ? "" : this.year + " ",
                this.month == -1 ? "" : this.months[this.month - 1] + " ",
                this.day == -1 ? "" : this.day);
    }
}
