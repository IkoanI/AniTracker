package com.example.anitracker.vnObjects;

import android.util.Log;

import com.example.anitracker.mediaObjects.CharacterDetails;
import com.example.anitracker.mediaObjects.Date;
import com.example.anitracker.mediaObjects.MediaDetails;
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
    private Integer height;
    @SerializedName("weight")
    private Integer weight;
    @SerializedName("bust")
    private Integer bust;
    @SerializedName("waist")
    private Integer waist;
    @SerializedName("hips")
    private Integer hips;
    @SerializedName("cup")
    private String cup;
    @SerializedName("age")
    private Integer age;
    @SerializedName("birthday")
    private List<Integer> birthday;
    @SerializedName("sex")
    private List<String> sex;
    @SerializedName("traits")
    private List<VNTrait> traits;

    public CharacterDetails convertToCharacterDetails(String vndbID) {
        CharacterDetails characterDetails = new CharacterDetails();
        Name name = new Name(this.getName());
        name.setNativeName(this.original);
        name.setAlternatives(this.aliases);
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
        characterDetails.setBloodtype(this.getBloodType());
        characterDetails.setHeight(this.height);
        characterDetails.setWeight(this.weight);
        characterDetails.setBust(this.bust);
        characterDetails.setWaist(this.waist);
        characterDetails.setHips(this.hips);
        characterDetails.setCup(this.cup);
        characterDetails.setAge(String.valueOf(this.age));
        characterDetails.setDateOfbirth(this.getBirthday());
        characterDetails.setGender(this.getSex());
        characterDetails.setTraits(this.traits);
        characterDetails.setVnRoles(this.vns);
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
        if (this.bloodType != null) {
            return StringUtils.capitalize(bloodType);
        }
        return null;
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
        return new Date(-1, month, day);
    }

    public int getAge() {
        return age;
    }

    public String getSex() {
        if (this.sex == null) {
            return null;
        }
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
