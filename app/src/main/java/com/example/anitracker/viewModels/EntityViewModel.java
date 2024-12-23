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
import java.util.Map;

public class EntityViewModel extends ViewModel {
    private final ApiRepository repository;
    private String id;
    private final MutableLiveData<CharacterDetails> liveCharDetail;

    private final MutableLiveData<VNCharPage> liveVNCharDetail;
    private MediaType mediaType;

    public EntityViewModel() {
        this.repository = new ApiRepository();
        this.liveErrorMsg = repository.getMutableErrorMsg();
        this.liveCharDetail = repository.getMutableCharacterDetail();
        this.liveVNCharDetail = repository.getMutableVNCharPage();
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

    public void getCharacterDetail() {
        if (this.mediaType == MediaType.VISUAL_NOVEL) {
            this.repository.fetchVNCharDetails(this.id);
        } else {
            this.repository.fetchCharDetails(Integer.parseInt(this.id));
        }

    }

    public LiveData<CharacterDetails> observeCharacterDetail() {
        return this.liveCharDetail;
    }

    public LiveData<VNCharPage> observeVNCharDetail() {
        return this.liveVNCharDetail;
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