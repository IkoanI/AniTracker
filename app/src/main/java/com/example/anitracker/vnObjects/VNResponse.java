package com.example.anitracker.vnObjects;

import com.example.anitracker.mediaObjects.Date;
import com.example.anitracker.mediaObjects.Name;
import com.example.anitracker.mediaObjects.StaffDetails;
import com.example.anitracker.mediaObjects.Titles;
import com.example.anitracker.type.MediaType;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class VNResponse {
    @SerializedName("id")
    private String id;
    @SerializedName("title")
    private String title;
    @SerializedName("titles")
    private List<VNTitle> titles;
    @SerializedName("image")
    private VNImage image;
    @SerializedName("screenshots")
    private List<VNImage> screenshots;
    @SerializedName("released")
    private String released;
    @SerializedName("length")
    private int length;
    @SerializedName("length_votes")
    private int lengthVotes;
    @SerializedName("length_minutes")
    private int length_minutes;
    @SerializedName("rating")
    private float rating;
    @SerializedName("average")
    private float average;
    @SerializedName("developers")
    private List<Developer> developers;
    @SerializedName("description")
    private String desc;
    @SerializedName("devstatus")
    private int devStatus;
    @SerializedName("tags")
    private List<VNTag> tags;
    @SerializedName("aliases")
    private List<String> aliases;
    @SerializedName("role")
    private String role;
    @SerializedName("languages")
    private List<String> languages;
    @SerializedName("platforms")
    private List<String> platforms;
    @SerializedName("relations")
    private List<VNRelation> relations;
    @SerializedName("staff")
    private List<VNStaff> staffs;
    @SerializedName("editions")
    private List<VNEdition> editions;
    @SerializedName("va")
    private List<VNVoiceActor> knownVoiceActors;
    @SerializedName("extlinks")
    private List<VNLink> links;

    // exclusive to show specific release of visual novel a character is associated with
    @SerializedName("release")
    private VNRelease release;


    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public VNImage getImage() {
        if (image == null) {
            return new VNImage();
        }

        return image;
    }

    public String getLength() {
        switch (this.length) {
            case 1:
                return "Very Short (< 2 hours)";

            case 2:
                return "Short (2- 10 hours)";

            case 3:
                return "Medium (10 - 30 hours)";

            case 4:
                return "Long (30 - 50 hours)";

            case 5:
                return "Very Long (> 50 hours)";

            default:
                return "Unknown";
        }
    }

    public int getLengthMinutes() {
        return length_minutes;
    }

    public List<Developer> getDevelopers() {
        return developers;
    }

    public int getRating() {
        return Math.round(rating);
    }

    public String getReleased() {
        return released;
    }

    public String getStatus() {
        switch (this.devStatus) {
            case 0:
                return "Finished";
            case 1:
                return "In development";
            case 2:
                return "Cancelled";
            default:
                return "Unknown";
        }
    }

    public List<String> getLanguages() {
        if (this.languages != null && !this.languages.isEmpty()) {
            this.languages.replaceAll(VNLanguage.languageMap::get);
            return this.languages;
        }

        return null;
    }

    public List<String> getPlatforms() {
        if (this.platforms != null && !this.platforms.isEmpty()) {
            this.platforms.replaceAll(VNPlatform.platformMap::get);
            return this.platforms;
        }

        return null;
    }

    public String getRole() {
        return StringUtils.capitalize(this.role);
    }

    public List<VNLink> getLinks() {
        return links;
    }

    public String getDesc() {
        return this.desc == null ? null : VNDBParser.parseText(this.desc);
    }

    public VNDetails convertToMediaObject() {
        VNDetails vnDetails = new VNDetails();
        String natTitle = null, romTitle = null, engTitle = null;
        List<String> synonyms = new ArrayList<>();

        if (this.release != null) {
            vnDetails.setRelease(this.release);
            this.title = String.format("%s (%s)", this.title, this.release.getTitle());
        }

        if (this.titles != null) {
            for (VNTitle title : this.titles) {
                if (title.isMain()) {
                    natTitle = title.getTitle();
                    romTitle = title.getLatin();
                } else if (title.isOfficial()) {
                    if (Objects.equals(title.getLang(), "en")) {
                        engTitle = title.getTitle();
                    } else {
                        synonyms.add(title.getTitle());
                    }
                }
            }
        }

        vnDetails.setTitles(new Titles(engTitle, natTitle, romTitle, this.title));
        vnDetails.setSynonyms(synonyms);
        vnDetails.setAliases(this.aliases);
        vnDetails.setCoverImg(this.getImage().getThumbnail());
        vnDetails.setDesc(this.getDesc());
        vnDetails.setStatus(this.getStatus());
        vnDetails.setId(this.id);
        vnDetails.setAvgScore(this.getRating());
        vnDetails.setMeanScore(Math.round(this.average));
        if (this.tags != null) {
            vnDetails.setAllTags(VNTag.getAllTags(this.tags));
            vnDetails.setNoSpoilerTags(VNTag.getNoSpoilerTags(this.tags));
        }
        vnDetails.setDevelopers(this.developers);
        vnDetails.setLengthMinutes(this.length_minutes, this.lengthVotes);
        vnDetails.setLength(this.getLength());
        if (this.released != null) {
            int[] date = {-1, -1, -1};

            String[] prevDate = this.released.split("-");

            for (int i = 0; i < prevDate.length; i++) {
                try {
                    date[i] = Integer.parseInt(prevDate[i]);
                } catch (NumberFormatException e) {
                    date[i] = -1;
                }
            }

            vnDetails.setStartDate(new Date(date[0], date[1], date[2]));
        }

        if (this.screenshots != null && !this.screenshots.isEmpty()) {
            vnDetails.setScreenshots(new Screenshots(this.screenshots));
        }

        vnDetails.setRelations(this.relations);

        if (this.staffs != null && !this.staffs.isEmpty()) {
            List<StaffDetails> staffsList = new ArrayList<>();
            for (VNStaff staff : this.staffs) {
                StaffDetails staffDetails = staff.convertToStaffDetail();
                String nameAndEdition = String.format("%s\n(%s)",
                        staff.getName(), staff.getEdition() == -1 ? "Original Edition" : this.editions.get(staff.getEdition()).getName());

                staffDetails.setName(new Name(nameAndEdition));
                staffsList.add(staffDetails);
            }
            vnDetails.setStaffs(staffsList);
        }

        if (this.knownVoiceActors != null && !this.knownVoiceActors.isEmpty()) {
            Map<String, StaffDetails> knownVAs = new HashMap<>();
            for (VNVoiceActor va : this.knownVoiceActors) {
                StaffDetails vaDetails = va.getStaff().convertToStaffDetail();
                vaDetails.setRole(va.getNote());
                knownVAs.put(va.getCharacter().getId(), vaDetails);
            }

            vnDetails.setKnownVAs(knownVAs);
        }
        vnDetails.setLanguages(this.getLanguages());
        vnDetails.setPlatforms(this.getPlatforms());
        vnDetails.setLinks(this.links);
        vnDetails.setCharRole(this.getRole());
        vnDetails.setFormat("Visual Novel");
        vnDetails.setType(MediaType.VISUAL_NOVEL);

        return vnDetails;
    }


}
