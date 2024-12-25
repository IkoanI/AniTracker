package com.example.anitracker.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.anitracker.mediaObjects.CharacterDetails;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.mediaObjects.StaffDetails;
import com.example.anitracker.repository.ApiRepository;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.type.Page;
import com.example.anitracker.vnObjects.VNCharPage;

import java.util.List;

public class EntityViewModel extends ViewModel {
    private final ApiRepository repository;
    private String id;

    private String entityType;
    private MediaType mediaType;
    private CharacterDetails lastFetchedCharDetail;
    private StaffDetails lastFetchedStaffDetail;

    // overview fragment
    private final MutableLiveData<CharacterDetails> liveCharDetail;
    private final MutableLiveData<StaffDetails> liveStaffDetail;

    // roles fragment
    private final MutableLiveData<List<MediaDetails>> liveCharRoles;
    private int currRolePage = 1;

    public EntityViewModel() {
        this.repository = new ApiRepository();
        this.liveErrorMsg = repository.getMutableErrorMsg();
        this.liveCharDetail = repository.getMutableCharacterDetail();
        this.liveStaffDetail = repository.getMutableStaffDetail();
        this.liveCharRoles = repository.getMutableRelationsPage();
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public CharacterDetails getLastFetchedCharDetail() {
        return this.lastFetchedCharDetail;
    }

    public void setLastFetchedCharDetail(CharacterDetails lastFetchedDetail) {
        this.lastFetchedCharDetail = lastFetchedDetail;
    }

    public StaffDetails getLastFetchedStaffDetail() {
        return this.lastFetchedStaffDetail;
    }

    public void setLastFetchedStaffDetail(StaffDetails lastFetchedDetail) {
        this.lastFetchedStaffDetail = lastFetchedDetail;
    }

    // error message
    private final MutableLiveData<String> liveErrorMsg;

    public void getCharDetail() {
        if (this.mediaType == MediaType.VISUAL_NOVEL) {
            this.repository.fetchVNCharDetails(this.id);
        } else {
            this.repository.fetchCharDetails(Integer.parseInt(this.id));
        }
    }

    public void getCharRoles() {
        this.repository.fetchCharRoles(Integer.parseInt(this.id), this.currRolePage);
        this.currRolePage++;
    }

    public void getStaffDetail() {
        if (this.mediaType == MediaType.VISUAL_NOVEL) {
            this.repository.fetchVNStaffDetail(this.id);
        } else {
            this.repository.fetchStaffDetail(Integer.parseInt(this.id));
        }
    }

    public LiveData<CharacterDetails> observeCharDetail() {
        return this.liveCharDetail;
    }

    public LiveData<List<MediaDetails>> observeCharRoles() {
        return this.liveCharRoles;
    }

    public LiveData<StaffDetails> observeStaffDetail() {
        return this.liveStaffDetail;
    }

    public LiveData<String> observeErrorMsg() { return liveErrorMsg; }

    public void setId(String id) { this.id = id;}

    public String getId() {
        return this.id;
    }

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
    }

    // clear requests
    public void clearComposite(){
        repository.clearComposite();
    }
}