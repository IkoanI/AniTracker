package com.example.anitracker.mediaObjects;

import com.example.anitracker.vnObjects.VNLink;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class StaffDetails extends Entity {
    private String lang, homeTown, yearsActive;
    private List<String> primaryOccupations;
    private Date dateOfDeath;

    private List<VNLink> links;

    @Override
    public List<Info> getInfo() {
        List<Info> infoList = new ArrayList<>();
        String[] infoOrder = {"Full Name", "Native Name", "First Name", "Middle Name",
                "Last Name", "Alternative Names", "Language", "Birthday", "Age",
                "Gender", "Years Active", "Date of Death", "Hometown", "Blood Type",
                "Primary Occupations", "Favorites", "Links"};

        for (String infoName: infoOrder) {
            if (this.infoMap.containsKey(infoName)) {
                infoList.add(new Info(infoName, infoMap.get(infoName)));
            }
        }

        return infoList;
    }

    public String getLang() {
        return this.lang;
    }

    public void setLang(String lang) {
        if (lang != null) {
            this.lang = lang;
            this.infoMap.put("Language", this.lang);
        }
    }

    public String getHomeTown() {
        return this.homeTown;
    }

    public void setHomeTown(String homeTown) {
        if (homeTown != null) {
            this.homeTown = homeTown;
            this.infoMap.put("Hometown", this.homeTown);
        }
    }

    public List<String> getPrimaryOccupations() {
        return this.primaryOccupations;
    }

    public void setPrimaryOccupations(List<String> primaryOccupations) {
        if (primaryOccupations != null && !primaryOccupations.isEmpty()) {
            this.primaryOccupations = primaryOccupations;
            this.infoMap.put("Primary Occupations", String.join("\n\n", this.primaryOccupations));

        }
    }

    public Date getDateOfDeath() {
        return this.dateOfDeath;
    }

    public void setDateOfDeath(Date dateOfDeath) {
        if (StringUtils.isNotBlank(dateOfDeath.toString())) {
            this.dateOfDeath = dateOfDeath;
            this.infoMap.put("Date of Death", this.dateOfDeath.toString());
        }
    }

    public String getYearsActive() {
        return this.yearsActive;
    }

    public void setYearsActive(List<Integer> yearsActive) {
        if (yearsActive != null && !yearsActive.isEmpty()) {
            this.yearsActive = String.format(Locale.ENGLISH, "%d - ", yearsActive.get(0));
            if (yearsActive.size() == 2) {
                this.yearsActive += String.valueOf(yearsActive.get(1));
            } else {
                this.yearsActive += "Present";
            }

            infoMap.put("Years Active", this.yearsActive);
        }
    }

    public void setLinks(List<VNLink> links) {
        if (links != null && !links.isEmpty()) {
            this.links = links;
        }
    }

    public List<VNLink> getLinks() {
        return this.links;
    }
}
