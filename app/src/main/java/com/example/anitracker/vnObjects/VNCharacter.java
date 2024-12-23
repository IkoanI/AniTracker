package com.example.anitracker.vnObjects;

import com.example.anitracker.mediaObjects.CharacterDetails;
import com.example.anitracker.mediaObjects.Date;
import com.example.anitracker.mediaObjects.Name;
import com.example.anitracker.mediaObjects.Tag;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VNCharacter {
    @SerializedName("id")
    private String id;
    @SerializedName("vns")
    private List<VNResponse> vns;
    @SerializedName("name")
    private String name;
    @SerializedName("image")
    private VNImage image;
    @SerializedName("original")
    private String original;
    @SerializedName("aliases")
    private List<String> aliases;
    @SerializedName("description")
    private String description;
    @SerializedName("blood_type")
    private String bloodType;
    @SerializedName("height")
    private int height;
    @SerializedName("weight")
    private int weight;
    @SerializedName("bust")
    private int bust;
    @SerializedName("waist")
    private int waist;
    @SerializedName("hips")
    private int hips;
    @SerializedName("cup")
    private String cup;
    @SerializedName("age")
    private int age;
    @SerializedName("birthday")
    private List<Integer> birthday;
    @SerializedName("sex")
    private List<String> sex;
    @SerializedName("traits")
    private List<VNTrait> traits;

    public CharacterDetails convertToCharacterDetails(String vndbID) {
        CharacterDetails characterDetails = new CharacterDetails();
        Name name = new Name(this.getName());
        if (this.original != null) {
            name.setNativeName(this.original);
        }

        if (this.aliases != null && !this.aliases.isEmpty()) {
            name.setAlternatives(this.aliases);
        }

        characterDetails.setName(name);
        characterDetails.setId(this.id);
        characterDetails.setImage(this.getImage().getUrl());
        for (VNResponse vn : this.getVns()) {
            if (Objects.equals(vn.getId(), vndbID)) {
                characterDetails.setRole(StringUtils.capitalize(vn.getRole()));
                break;
            }
        }

        characterDetails.setDescription(this.description);
        if (this.bloodType != null) {characterDetails.setBloodtype(this.getBloodType());}
        if (this.height != 0) {characterDetails.setHeight(this.height);}
        if (this.weight != 0) {characterDetails.setWeight(this.weight);}
        if (this.bust != 0) {characterDetails.setBust(this.bust);}
        if (this.waist != 0) {characterDetails.setWaist(this.waist);}
        if (this.hips != 0) {characterDetails.setHips(this.hips);}
        if (this.cup != null) {characterDetails.setCup(this.cup);}
        if (this.age != 0) {characterDetails.setAge(String.valueOf(this.age));}
        if (this.birthday != null) {characterDetails.setDateOfbirth(this.getBirthday());}
        if (this.sex != null) {characterDetails.setGender(this.getSex());}
        if (this.traits != null) {
            List<Tag> allTraits = new ArrayList<>();
            List<Tag> noSpoilerTraits = new ArrayList<>();
            for (VNTrait trait : this.traits) {
                Tag tag = trait.convertToMediaTag();
                if (!tag.getSpoiler()) {
                    noSpoilerTraits.add(tag);
                }
                allTraits.add(tag);
            }
            characterDetails.setAllTraits(allTraits);
            characterDetails.setNoSpoilerTraits(noSpoilerTraits);
        }
        return characterDetails;
    }

    public List<VNResponse> getVns() {
        return this.vns;
    }

    public String getId() {
        return this.id;
    }

    public String getName() {
        return this.name;
    }

    public VNImage getImage() {
        if (this.image == null) {
            return new VNImage();
        }
        return this.image;
    }

    public String getOriginal() {
        return original;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public String getDescription() {
        return description;
    }

    public String getBloodType() {
        return bloodType.toUpperCase();
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    public int getBust() {
        return bust;
    }

    public int getWaist() {
        return waist;
    }

    public int getHips() {
        return hips;
    }

    public String getCup() {
        return cup;
    }

    public Date getBirthday() {
        int month = -1, day = -1;
        if (this.birthday != null && this.birthday.size() == 2) {
            month = this.birthday.get(0);
            day = this.birthday.get(1);
        }
        return new Date(-1, month, day, true);
    }

    public int getAge() {
        return age;
    }

    public String getSex() {
        switch (this.sex.get(0)) {
            case "m":
                return "Male";

            case "f":
                return "Female";

            case "b":
                return "Both Male and Female";

            case "n":
                return "Sexless";

            default:
                return "";
        }
    }

    public List<VNTrait> getTraits() {
        return traits;
    }
}
