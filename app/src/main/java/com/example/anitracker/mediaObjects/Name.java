package com.example.anitracker.mediaObjects;

import java.util.List;

public class Name {
    String first, middle, last, full, nativeName, userPref;
    List<String> alternatives, alternativeSpoilers;

    public Name(String userPref){
        this.userPref = userPref;
    }

    public String getFirst() {
        return first;
    }

    public void setFirst(String first) {
        this.first = first;
    }

    public String getMiddle() {
        return middle;
    }

    public void setMiddle(String middle) {
        this.middle = middle;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String last) {
        this.last = last;
    }

    public String getFull() {
        return full;
    }

    public void setFull(String full) {
        this.full = full;
    }

    public String getNativeName() {
        return nativeName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public String getUserPref() {
        return userPref;
    }

    public void setUserPref(String userPref) {
        this.userPref = userPref;
    }

    public List<String> getAlternatives() {
        return alternatives;
    }

    public void setAlternatives(List<String> alternatives) {
        this.alternatives = alternatives;
    }

    public List<String> getAlternativeSpoilers() {
        return alternativeSpoilers;
    }

    public void setAlternativeSpoilers(List<String> alternativeSpoilers) {
        this.alternativeSpoilers = alternativeSpoilers;
    }
}
