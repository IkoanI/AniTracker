package com.example.anitracker.uiObjects;

import com.example.anitracker.type.StaffLanguage;

public class LanguageDropdown {

    StaffLanguage[] staffLanguages = {StaffLanguage.JAPANESE,
            StaffLanguage.ENGLISH,
            StaffLanguage.KOREAN,
            StaffLanguage.ITALIAN,
            StaffLanguage.SPANISH,
            StaffLanguage.PORTUGUESE,
            StaffLanguage.FRENCH,
            StaffLanguage.GERMAN,
            StaffLanguage.HEBREW,
            StaffLanguage.HUNGARIAN};

    public StaffLanguage[] getStaffLanguages() {
        return staffLanguages;
    }
}
