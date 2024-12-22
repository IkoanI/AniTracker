package com.example.anitracker.uiObjects;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.example.anitracker.type.StaffLanguage;

public class LanguageDropdown {
    private final StaffLanguage[] staffLanguages = {StaffLanguage.JAPANESE,
            StaffLanguage.ENGLISH,
            StaffLanguage.KOREAN,
            StaffLanguage.ITALIAN,
            StaffLanguage.SPANISH,
            StaffLanguage.PORTUGUESE,
            StaffLanguage.FRENCH,
            StaffLanguage.GERMAN,
            StaffLanguage.HEBREW,
            StaffLanguage.HUNGARIAN};

    private final String[] languages = {"Japanese", "English", "Korean", "Italian",
            "Spanish", "Portuguese", "French",
            "German", "Hebrew", "Hungarian"};

    private final ArrayAdapter<String> languageDropdownAdapter;

    public LanguageDropdown(Context context) {
        this.languageDropdownAdapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, languages);
        this.languageDropdownAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    }

    public ArrayAdapter<String> getAdapter() {
        return this.languageDropdownAdapter;
    }

    public StaffLanguage getStaffLanguage(int i) {
        return staffLanguages[i];
    }
}
