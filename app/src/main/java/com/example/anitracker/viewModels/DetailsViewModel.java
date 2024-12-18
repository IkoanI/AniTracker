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
import com.example.anitracker.vnObjects.VNCharPage;

import java.util.List;

public class DetailsViewModel extends ViewModel {
    private final ApiRepository repository;
    private String id;
    private MediaType type;

    // error message
    private final MutableLiveData<String> liveErrorMsg;

    // used by overview fragment
    private final MutableLiveData<MediaDetails> mediaDetails;

    // used by character fragment
    private final MutableLiveData<List<CharacterDetails>> liveCharPage;
    private final MutableLiveData<VNCharPage> liveVNCharPage;
    private int currCharPage = 1;

    // used by staff fragment
    private final MutableLiveData<List<StaffDetails>> liveStaffPage;
    private int currStaffPage = 1;

    // used by relations fragment
    private final MutableLiveData<List<MediaDetails>> liveRelationsPage;
    public List<MediaDetails> vnRelationsList;

    public DetailsViewModel() {
        this.repository = new ApiRepository();
        this.liveCharPage = repository.getMutableCharPage();
        this.liveVNCharPage = repository.getMutableVNCharPage();
        this.liveErrorMsg = repository.getMutableErrorMsg();
        this.mediaDetails = repository.getMutableLiveData();
        this.liveStaffPage = repository.getMutableStaffPage();
        this.liveRelationsPage = repository.getMutableRelationsPage();
    }

    public LiveData<String> observeErrorMsg() { return liveErrorMsg; }

    public void setId(String id) { this.id = id;}

    public String getId() {
        return this.id;
    }

    public void setType(MediaType type) { this.type = type;}

    public MediaType getType() { return this.type;}

    // overview fragment
    public void getDetails() {
        repository.fetchData(this.type, this.id);
    }

    public LiveData<MediaDetails> observeMediaDetails(){
        return mediaDetails;
    }

    // character fragment

    public void getCharPage(StaffLanguage language) {
        repository.fetchCharPage(Integer.parseInt(this.id), this.currCharPage, language);
        this.currCharPage++;
    }

    public void getVNCharPage() {
        repository.fetchVNChars(this.id, this.currCharPage);
        this.currCharPage++;
    }

    public LiveData<List<CharacterDetails>> observeCharPage(){
        return liveCharPage;
    }

    public LiveData<VNCharPage> observeVNCharPage(){
        return liveVNCharPage;
    }

    public void setCurrCharPage(int pageNo) {
        this.currCharPage = pageNo;
    }

    // staff fragment

    public void getStaffPage(){
        repository.fetchStaffPage(Integer.parseInt(this.id), this.currStaffPage);
        this.currStaffPage++;
    }

    public LiveData<List<StaffDetails>> observeStaffPage(){
        return this.liveStaffPage;
    }

    // relation fragment
    public void getRelationsPage(){
        repository.fetchRelationsPage(Integer.parseInt(this.id));
    }

    public LiveData<List<MediaDetails>> observeRelationsPage(){
        return liveRelationsPage;
    }

    // clear requests
    public void clearComposite(){
        repository.clearComposite();
    }
}