package com.example.anitracker.viewModels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.anitracker.mediaObjects.CharacterDetails;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.repository.ApiRepository;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.vnObjects.VNCharPage;

import java.util.List;

public class EntityViewModel extends ViewModel {
    private final ApiRepository repository;
    private String id;
    private MediaType mediaType;

    // overview fragment
    private final MutableLiveData<CharacterDetails> liveEntityDetail;
    private final MutableLiveData<VNCharPage> liveVNEntityDetail;

    // roles fragment
    private final MutableLiveData<List<MediaDetails>> liveEntityRoles;
    private int currRolePage = 1;

    public EntityViewModel() {
        this.repository = new ApiRepository();
        this.liveErrorMsg = repository.getMutableErrorMsg();
        this.liveEntityDetail = repository.getMutableCharacterDetail();
        this.liveVNEntityDetail = repository.getMutableVNCharPage();
        this.liveEntityRoles = repository.getMutableRelationsPage();
    }

    public MediaType getMediaType() {
        return mediaType;
    }

    public void setMediaType(MediaType mediaType) {
        this.mediaType = mediaType;
    }

    public CharacterDetails getLastFetchedDetail() {
        return lastFetchedDetail;
    }

    public void setLastFetchedDetail(CharacterDetails lastFetchedDetail) {
        this.lastFetchedDetail = lastFetchedDetail;
    }

    private CharacterDetails lastFetchedDetail;

    // error message
    private final MutableLiveData<String> liveErrorMsg;

    public void getEntityDetail() {
        if (this.mediaType == MediaType.VISUAL_NOVEL) {
            this.repository.fetchVNCharDetails(this.id);
        } else {
            this.repository.fetchCharDetails(Integer.parseInt(this.id));
        }
    }

    public void getEntityRoles() {
        this.repository.fetchCharRoles(Integer.parseInt(this.id), this.currRolePage);
        this.currRolePage++;
    }

    public LiveData<CharacterDetails> observeEntityDetail() {
        return this.liveEntityDetail;
    }

    public LiveData<VNCharPage> observeVNEntityDetail() {
        return this.liveVNEntityDetail;
    }

    public LiveData<List<MediaDetails>> observeEntityRoles() {
        return this.liveEntityRoles;
    }

    public LiveData<String> observeErrorMsg() { return liveErrorMsg; }

    public void setId(String id) { this.id = id;}

    public String getId() {
        return this.id;
    }

    // clear requests
    public void clearComposite(){
        repository.clearComposite();
    }
}