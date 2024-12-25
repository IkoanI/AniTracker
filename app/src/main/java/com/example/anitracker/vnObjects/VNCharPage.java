package com.example.anitracker.vnObjects;

import com.example.anitracker.mediaObjects.CharacterDetails;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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
        for (VNCharacter character : vnCharacterList) {
            characterDetails.add(character.convertToCharacterDetails(vndbID));
        }
        return characterDetails;
    }

    public int getSize() {
        return this.vnCharacterList.size();
    }
}
