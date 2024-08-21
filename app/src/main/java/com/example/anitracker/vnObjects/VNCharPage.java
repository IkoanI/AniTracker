package com.example.anitracker.vnObjects;

import com.example.anitracker.mediaObjects.CharacterDetails;
import com.example.anitracker.mediaObjects.Name;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VNCharPage {
    @SerializedName("more")
    boolean more;
    @SerializedName("results")
    List<VNCharacter> vnCharacterList;


    public boolean hasMore() {
        return more;
    }

    public List<CharacterDetails> getVNCharList(String vndbID) {
        List<CharacterDetails> characterDetails = new ArrayList<>();
        for (VNCharacter character : vnCharacterList){
            CharacterDetails newCharacterDetail = new CharacterDetails();
            newCharacterDetail.setName(new Name(character.getName()));
            newCharacterDetail.setImage(character.getImage().getUrl());
            for (VNResponse vn : character.getVns()){
                if(Objects.equals(vn.getId(), vndbID)){
                    newCharacterDetail.setRole(vn.getRole());
                    break;
                }
            }
            characterDetails.add(newCharacterDetail);
        }
        return characterDetails;
    }
}
