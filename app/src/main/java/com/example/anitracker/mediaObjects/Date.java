package com.example.anitracker.mediaObjects;

import androidx.annotation.NonNull;


public class Date {
    private final int year, month, day;
    private boolean isBirthday;

    private final String[] months = {"Jan", "Feb", "Mar", "Apr",
            "May", "Jun", "Jul", "Aug",
            "Sep", "Oct", "Nov", "Dec"};

    public Date(int year, int month, int day, boolean isBirthday) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.isBirthday = isBirthday;
    }

    public Date(int year, int month, int day) {
        this(year, month, day, false);
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
        if (isBirthday) {
            return String.format("%s%s%s",
                    this.year == -1 ? "" : this.year + " ",
                    this.month == -1 ? "" : this.months[this.month - 1] + " ",
                    this.day == -1 ? "" : this.day);
        }

        return String.format("%s%s%s",
                this.year == -1 ? "TBA" : this.year + " ",
                this.month == -1 ? "" : this.months[this.month - 1] + " ",
                this.day == -1 ? "" : this.day);
    }
}
