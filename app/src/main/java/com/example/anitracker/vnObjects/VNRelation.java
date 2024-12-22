package com.example.anitracker.vnObjects;

import com.example.anitracker.type.MediaRelation;
import com.google.gson.annotations.SerializedName;

public class VNRelation extends VNResponse {
    @SerializedName("relation")
    private String relation;

    @Override
    public VNDetails convertToMediaObject() {
        VNDetails vnDetails = super.convertToMediaObject();

        switch (this.relation) {
            case "char":
                vnDetails.setRelation(MediaRelation.CHARACTER);
                break;

            case "seq":
                vnDetails.setRelation(MediaRelation.SEQUEL);
                break;

            case "set":
                vnDetails.setRelation("Same Setting");
                break;

            case "side":
                vnDetails.setRelation(MediaRelation.SIDE_STORY);
                break;

            case "alt":
                vnDetails.setRelation(MediaRelation.ALTERNATIVE);
                break;

            case "fan":
                vnDetails.setRelation("Fandisc");
                break;

            case "ser":
                vnDetails.setRelation("Series");
                break;

            case "preq":
                vnDetails.setRelation(MediaRelation.PREQUEL);
                break;

            case "orig":
                vnDetails.setRelation(MediaRelation.SOURCE);
                break;

            case "par":
                vnDetails.setRelation(MediaRelation.PARENT);
                break;

            default:
                vnDetails.setRelation(this.relation);
                break;

        }

        vnDetails.setStatus(super.getStatus());
        return vnDetails;
    }
}
