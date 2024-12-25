package com.example.anitracker.vnObjects;

import com.example.anitracker.mediaObjects.StaffDetails;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class VNStaffPage {
    @SerializedName("more")
    boolean more;
    @SerializedName("results")
    List<VNStaff> vnStaffList;


    public boolean hasMore() {
        return more;
    }

    public List<StaffDetails> getVnStaffList() {
        List<StaffDetails> staffs = new ArrayList<>();

        for (VNStaff vnStaff :  this.vnStaffList) {
            staffs.add(vnStaff.convertToStaffDetail());
        }
        return staffs;
    }

    public int getSize() {
        return this.vnStaffList.size();
    }
}
