package com.example.anitracker.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.anitracker.mediaObjects.CharacterDetails;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.mediaObjects.StaffDetails;
import com.example.anitracker.repository.ApiRepository;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.type.StaffLanguage;

import java.util.List;
import java.util.Map;

public class DetailsViewModel extends ViewModel {
    private final ApiRepository repository;
    private String id;
    private MediaType mediaType;

    // error message
    private final MutableLiveData<String> liveErrorMsg;

    // used by overview fragment
    private final MutableLiveData<MediaDetails> mediaDetails;

    // used by character fragment
    private final MutableLiveData<List<CharacterDetails>> liveCharPage;
    private int currCharPage = 1;
    private StaffLanguage lastSelectedLanguage = StaffLanguage.JAPANESE;
    private int lastSelectedLanguagePos = 0;
    private Map<String, StaffDetails> vnKnownVAs;

    // used by staff fragment
    private final MutableLiveData<List<StaffDetails>> liveStaffPage;
    private int currStaffPage = 1;
    private List<StaffDetails> vnStaffsList;

    // used by relations fragment
    private final MutableLiveData<List<MediaDetails>> liveRelationsPage;
    private List<MediaDetails> vnRelationsList;

    public DetailsViewModel() {
        this.repository = new ApiRepository();
        this.liveCharPage = repository.getMutableCharPage();
        this.liveErrorMsg = repository.getMutableErrorMsg();
        this.mediaDetails = repository.getMutableLiveData();
        this.liveStaffPage = repository.getMutableStaffPage();
        this.liveRelationsPage = repository.getMutableRelationsPage();
    }

    public LiveData<String> observeErrorMsg() { return liveErrorMsg; }

    public void setType(MediaType type) { this.mediaType = type;}

    public MediaType getType() { return this.mediaType;}

    // overview fragment
    public void getDetails() {
        repository.fetchData(this.mediaType, this.id);
    }

    public LiveData<MediaDetails> observeMediaDetails(){
        return mediaDetails;
    }

    // character fragment

    public void getCharPage() {
        if (this.mediaType == MediaType.VISUAL_NOVEL) {
            repository.fetchVNChars(this.id, this.currCharPage);
        } else {
            repository.fetchCharPage(Integer.parseInt(this.id), this.currCharPage, this.lastSelectedLanguage);
        }

        this.currCharPage++;
    }

    public LiveData<List<CharacterDetails>> observeCharPage() {
        return liveCharPage;
    }

    public void setCurrCharPage(int pageNo) {
        this.currCharPage = pageNo;
    }

    public void setKnownVAs(Map<String, StaffDetails> knownVAs) {
        this.vnKnownVAs = knownVAs;
    }

    public Map<String, StaffDetails> getKnownVAs() {
        return this.vnKnownVAs;
    }

    // staff fragment

    public void getStaffPage() {
        repository.fetchStaffPage(Integer.parseInt(this.id), this.currStaffPage);
        this.currStaffPage++;
    }

    public LiveData<List<StaffDetails>> observeStaffPage(){
        return this.liveStaffPage;
    }

    public void setVnStaffsList(List<StaffDetails> vnStaffsList) {
        this.vnStaffsList = vnStaffsList;
    }

    public List<StaffDetails> getVnStaffsList() {
        return this.vnStaffsList;
    }

    // relation fragment

    public void getRelationsPage() {
        repository.fetchRelationsPage(Integer.parseInt(this.id));
    }

    public LiveData<List<MediaDetails>> observeRelationsPage(){
        return liveRelationsPage;
    }

    public void setVnRelationsList(List<MediaDetails> vnRelationsList) {
        this.vnRelationsList = vnRelationsList;
    }

    public List<MediaDetails> getVnRelationsList() {
        return this.vnRelationsList;
    }

    // general getter and setters

    public void setLastSelectedLanguage(StaffLanguage selectedLanguage) {
        this.lastSelectedLanguage = selectedLanguage;
    }

    public StaffLanguage getLastSelectedLanguage() {
        return this.lastSelectedLanguage;
    }

    public void setLastSelectedLanguagePos(int pos) {
        this.lastSelectedLanguagePos = pos;
    }

    public int getLastSelectedLanguagePos() {
        return this.lastSelectedLanguagePos;
    }

    public void setId(String id) { this.id = id;}

    public String getId() {
        return this.id;
    }

    // clear requests
    public void clearComposite(){
        repository.clearComposite();
    }
}