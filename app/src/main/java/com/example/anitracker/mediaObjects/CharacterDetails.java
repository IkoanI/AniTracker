package com.example.anitracker.mediaObjects;

import android.util.Log;

import com.example.anitracker.vnObjects.VNResponse;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class CharacterDetails {
    private String image, role, notes, id, description, gender, bloodtype, age;
    private Name name;
    private StaffDetails voiceActor;
    private Date dateOfbirth;
    private int favorites;
    private final Map<String, String> infoMap = new HashMap<>();

    // VN Exclusive
    private int height, weight, bust, waist, hips;
    private String cup;
    private List<Tag> allTraits, noSpoilerTraits;
    private List<MediaDetails> vnRoles = new ArrayList<>();

    public List<Info> getInfo() {
        List<Info> infoList = new ArrayList<>();
        String[] infoOrder = {"Full Name", "Native Name", "First Name", "Middle Name", "Last Name", "Alternative Names",
                "Birthday", "Age", "Gender", "Blood Type", "Height", "Weight", "Bust", "Waist", "Hips", "Cup", "Favorites"};

        for (String infoName: infoOrder) {
            if (this.infoMap.containsKey(infoName)) {
                infoList.add(new Info(infoName, infoMap.get(infoName)));
            }
        }

        return infoList;
    }

    public Name getName() {
        return this.name;
    }

    public void setName(Name name) {
        this.name = name;
        if (!StringUtils.isEmpty(this.name.getFirst())) {
            infoMap.put("First Name", this.name.getFirst());
        }

        if (!StringUtils.isEmpty(this.name.getMiddle())) {
            infoMap.put("Middle Name", this.name.getMiddle());
        }

        if (!StringUtils.isEmpty(this.name.getLast())) {
            infoMap.put("Last Name", this.name.getLast());
        }

        if (!StringUtils.isEmpty(this.name.getFull())) {
            infoMap.put("Full Name", this.name.getFull());
        }

        if (!StringUtils.isEmpty(this.name.getNativeName())) {
            infoMap.put("Native Name", this.name.getNativeName());
        }

        if (this.name.getAlternatives() != null && !this.name.getAlternatives().isEmpty()) {
            infoMap.put("Alternative Names", String.join("\n\n", this.name.getNativeName()));
        }

    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getRole() {
        return this.role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public StaffDetails getVoiceActor() {
        return this.voiceActor;
    }

    public void setVoiceActor(StaffDetails voiceActor) {
        this.voiceActor = voiceActor;
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        if (this.gender != null) {
            this.infoMap.put("Gender", this.gender);
        }
    }

    public String getBloodtype() {
        return bloodtype;
    }

    public void setBloodtype(String bloodtype) {
        this.bloodtype = bloodtype;
        if (this.bloodtype != null) {
            this.infoMap.put("Blood Type", this.bloodtype);
        }
    }

    public Date getDateOfbirth() {
        return dateOfbirth;
    }

    public void setDateOfbirth(Date dateOfbirth) {
        this.dateOfbirth = dateOfbirth;
        if (!this.dateOfbirth.toString().isBlank()) {
            this.infoMap.put("Birthday", this.dateOfbirth.toString());
        }
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
        if (this.age != null)  {
            this.infoMap.put("Age", this.age);
        }
    }

    public int getFavorites() {
        return this.favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
        this.infoMap.put("Favorites", String.valueOf(this.favorites));
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
        this.infoMap.put("Height", String.format(Locale.ENGLISH,"%d cm", this.height));
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
        this.infoMap.put("Weight", String.format(Locale.ENGLISH,"%d kg", this.weight));
    }

    public int getBust() {
        return bust;
    }

    public void setBust(int bust) {
        this.bust = bust;
        this.infoMap.put("Bust", String.format(Locale.ENGLISH,"%d cm", this.bust));
    }

    public int getWaist() {
        return waist;
    }

    public void setWaist(int waist) {
        this.waist = waist;
        this.infoMap.put("Waist", String.format(Locale.ENGLISH,"%d cm", this.waist));
    }

    public int getHips() {
        return hips;
    }

    public void setHips(int hips) {
        this.hips = hips;
        this.infoMap.put("Hips", String.format(Locale.ENGLISH,"%d cm", this.hips));
    }

    public String getCup() {
        return cup;
    }

    public void setCup(String cup) {
        this.cup = cup;
        this.infoMap.put("Cup", this.cup);
    }

    public List<Tag> getAllTraits() {
        return this.allTraits;
    }

    public void setAllTraits(List<Tag> traits) {
        this.allTraits = traits;
    }

    public List<Tag> getNoSpoilerTraits() {
        return this.noSpoilerTraits;
    }

    public void setNoSpoilerTraits(List<Tag> traits) {
        this.noSpoilerTraits = traits;
    }

    public void setVnRoles(List<MediaDetails> vnRoles) {
        this.vnRoles = vnRoles;
    }

    public List<MediaDetails> getVnRoles() {
        return this.vnRoles;
    }
}
