package com.example.anitracker.mediaObjects;

import com.example.anitracker.vnObjects.VNResponse;
import com.example.anitracker.vnObjects.VNTrait;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CharacterDetails extends Entity {
    private String notes;
    private StaffDetails voiceActor;

    // VN Exclusive
    private int height, weight, bust, waist, hips;
    private String cup;
    private List<Tag> allTraits, noSpoilerTraits;
    private List<MediaDetails> vnRoles = new ArrayList<>();

    @Override
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

    public StaffDetails getVoiceActor() {
        return this.voiceActor;
    }

    public void setVoiceActor(StaffDetails voiceActor) {
        if (voiceActor != null) {
            this.voiceActor = voiceActor;
        }
    }

    public String getNotes() {
        return this.notes;
    }

    public void setNotes(String notes) {
        if (notes != null) {
            this.notes = notes;
        }
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(Integer height) {
        if (height != null) {
            this.height = height;
            this.infoMap.put("Height", String.format(Locale.ENGLISH,"%d cm", this.height));
        }
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        if (weight != null) {
            this.weight = weight;
            this.infoMap.put("Weight", String.format(Locale.ENGLISH,"%d kg", this.weight));
        }
    }

    public int getBust() {
        return bust;
    }

    public void setBust(Integer bust) {
        if (bust != null) {
            this.bust = bust;
            this.infoMap.put("Bust", String.format(Locale.ENGLISH,"%d cm", this.bust));
        }

    }

    public int getWaist() {
        return waist;
    }

    public void setWaist(Integer waist) {
        if (waist != null) {
            this.waist = waist;
            this.infoMap.put("Waist", String.format(Locale.ENGLISH,"%d cm", this.waist));
        }
    }

    public int getHips() {
        return hips;
    }

    public void setHips(Integer hips) {
        if (hips != null) {
            this.hips = hips;
            this.infoMap.put("Hips", String.format(Locale.ENGLISH,"%d cm", this.hips));
        }
    }

    public String getCup() {
        return cup;
    }

    public void setCup(String cup) {
        if (cup != null) {
            this.cup = cup;
            this.infoMap.put("Cup", this.cup);
        }
    }

    public List<Tag> getAllTraits() {
        return this.allTraits;
    }

    private void setAllTraits(List<Tag> traits) {
        if (traits != null && !traits.isEmpty()) {
            this.allTraits = traits;
        }
    }

    public List<Tag> getNoSpoilerTraits() {
        return this.noSpoilerTraits;
    }

    private void setNoSpoilerTraits(List<Tag> traits) {
        if (traits != null && !traits.isEmpty()) {
            this.noSpoilerTraits = traits;
        }
    }

    public void setTraits(List<VNTrait> traits) {
        if (traits != null && !traits.isEmpty()) {
            List<Tag> allTraits = new ArrayList<>();
            List<Tag> noSpoilerTraits = new ArrayList<>();
            for (VNTrait trait : traits) {
                Tag tag = trait.convertToMediaTag();
                if (!tag.getSpoiler()) {
                    noSpoilerTraits.add(tag);
                }
                allTraits.add(tag);
            }
            this.setAllTraits(allTraits);
            this.setNoSpoilerTraits(noSpoilerTraits);
        }
    }

    public void setVnRoles(List<VNResponse> roles) {
        if (roles != null && !roles.isEmpty()) {
            List<MediaDetails> vnRoles = new ArrayList<>();
            for (VNResponse vn : roles) {
                MediaDetails details = vn.convertToMediaObject();;
                details.setRelation(vn.getRole());
                vnRoles.add(details);
            }
            this.vnRoles = vnRoles;
        }

    }

    public List<MediaDetails> getVnRoles() {
        return this.vnRoles;
    }
}
