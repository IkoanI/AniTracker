package com.example.anitracker.mediaObjects;
import java.util.List;

public class Name {
    private String first, middle, last, full, nativeName, userPref;
    private List<String> alternatives, alternativeSpoilers;

    public Name(String userPref){
        this.userPref = userPref;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        if (first != null) {
            this.first = first;
        }
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        if (middle != null) {
            this.middle = middle;
        }
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        if (last != null){
            this.last = last;
        }
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        if (full != null) {
            this.full = full;
        }
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        if (nativeName != null) {
            this.nativeName = nativeName;
        }
    }

    public String getUserPref() {
        return userPref;
    }

    public void setUserPref(String userPref) {
        if (userPref != null) {
            this.userPref = userPref;
        }
    }

    public List<String> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<String> alternatives) {
        if (alternatives != null && !alternatives.isEmpty()) {
            this.alternatives = alternatives;
        }
    }

    public List<String> getAlternativeSpoilers() {
        return alternativeSpoilers;
    }

    public void setAlternativeSpoilers(List<String> alternativeSpoilers) {
        if (alternativeSpoilers != null && !alternativeSpoilers.isEmpty()) {
            this.alternativeSpoilers = alternativeSpoilers;
        }
    }
}
