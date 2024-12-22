package com.example.anitracker.vnObjects;

import com.example.anitracker.mediaObjects.CharacterDetails;
import com.example.anitracker.mediaObjects.Name;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.StringUtils;

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

    public CharacterDetails convertToCharacterDetails(String vndbID) {
        CharacterDetails characterDetails = new CharacterDetails();
        characterDetails.setName(new Name(this.getName()));
        characterDetails.setId(this.id);
        characterDetails.setImage(this.getImage().getUrl());
        for (VNResponse vn : this.getVns()) {
            if (Objects.equals(vn.getId(), vndbID)) {
                characterDetails.setRole(StringUtils.capitalize(vn.getRole()));
                break;
            }
        }

        return characterDetails;
    }
}
