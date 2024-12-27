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
import java.util.Objects;

public class DetailsViewModel extends ViewModel {
    private final ApiRepository repository;
    private String id;
    private MediaType mediaType;
    private String entityType;

    // error message
    private final MutableLiveData<String> liveErrorMsg;

    // used by overview fragment
    private MutableLiveData<MediaDetails> mediaDetails;
    private MutableLiveData<CharacterDetails> liveCharDetails;
    private MutableLiveData<StaffDetails> liveStaffDetails;
    private MediaDetails lastFetchedMediaDetail;
    private CharacterDetails lastFetchedCharDetail;
    private StaffDetails lastFetchedStaffDetail;

    // used by character fragment
    private final MutableLiveData<List<CharacterDetails>> liveCharPage;
    private int currCharPage = 1;
    private StaffLanguage lastSelectedLanguage = StaffLanguage.JAPANESE;
    private int lastSelectedLanguagePos = 0;
    private Map<String, StaffDetails> vnKnownVAs;

    // used by staff fragment
    private MutableLiveData<List<StaffDetails>> liveStaffPage;
    private int currStaffPage = 1;
    private List<StaffDetails> vnStaffsList;

    // used by relations and roles fragment
    private final MutableLiveData<List<MediaDetails>> liveRelationsPage;
    private List<MediaDetails> vnRelationsList;
    private int currRolePage = 1;

    public DetailsViewModel() {
        this.repository = new ApiRepository();
        this.liveErrorMsg = repository.getMutableErrorMsg();
        this.liveCharPage = repository.getMutableCharPage();
        this.liveRelationsPage = repository.getMutableRelationsPage();
    }

    public LiveData<String> observeErrorMsg() { return liveErrorMsg; }

    // overview fragment
    public void getDetails() {
        repository.fetchData(this.mediaType, this.id);
    }

    public LiveData<MediaDetails> observeMediaDetails(){
        return mediaDetails;
    }

    public void getCharDetail() {
        if (this.mediaType == MediaType.VISUAL_NOVEL) {
            this.repository.fetchVNCharDetails(this.id);
        } else {
            this.repository.fetchCharDetails(Integer.parseInt(this.id));
        }
    }

    public LiveData<CharacterDetails> observeCharDetail() {
        return this.liveCharDetails;
    }

    public void getStaffDetail() {
        if (this.mediaType == MediaType.VISUAL_NOVEL) {
            this.repository.fetchVNStaffDetail(this.id);
        } else {
            this.repository.fetchStaffDetail(Integer.parseInt(this.id));
        }
    }

    public LiveData<StaffDetails> observeStaffDetail() {
        return this.liveStaffDetails;
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

    public void setCurrCharPage(int pageNo) {
        this.currCharPage = pageNo;
    }

    public void setKnownVAs(Map<String, StaffDetails> knownVAs) {
        this.vnKnownVAs = knownVAs;
    }

    public Map<String, StaffDetails> getKnownVAs() {
        return this.vnKnownVAs;
    }

    public void getStaffChars() {
        if (this.mediaType != MediaType.VISUAL_NOVEL) {
            this.repository.fetchStaffChars(Integer.parseInt(this.id), this.currCharPage);
            this.currCharPage++;
        }
    }

    public LiveData<List<CharacterDetails>> observeCharPage() {
        return liveCharPage;
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

    public void setVnRelationsList(List<MediaDetails> vnRelationsList) {
        this.vnRelationsList = vnRelationsList;
    }

    public List<MediaDetails> getVnRelationsList() {
        return this.vnRelationsList;
    }

    public void getCharRoles() {
        if (this.mediaType != MediaType.VISUAL_NOVEL) {
            this.repository.fetchCharRoles(Integer.parseInt(this.id), this.currRolePage);
            this.currRolePage++;
        }
    }

    public void getStaffRoles() {
        if (this.mediaType != MediaType.VISUAL_NOVEL) {
            this.repository.fetchStaffRoles(Integer.parseInt(this.id), this.currRolePage);
            this.currRolePage++;
        }
    }

    public LiveData<List<MediaDetails>> observeRelationsPage(){
        return liveRelationsPage;
    }

    // general getter and setters

    public void setLastSelectedLanguage(StaffLanguage selectedLanguage) {this.lastSelectedLanguage = selectedLanguage;}

    public StaffLanguage getLastSelectedLanguage() {return this.lastSelectedLanguage;}

    public void setLastSelectedLanguagePos(int pos) {this.lastSelectedLanguagePos = pos;}

    public int getLastSelectedLanguagePos() {return this.lastSelectedLanguagePos;}

    public void setId(String id) {this.id = id;}

    public String getId() {return this.id;}

    public void setMediaType(MediaType type) {
        this.mediaType = type;
        this.mediaDetails = repository.getMutableLiveData();
        this.liveStaffPage = repository.getMutableStaffPage();
    }

    public MediaType getMediaType() {return this.mediaType;}

    public String getEntityType() {
        return entityType;
    }

    public void setEntityType(String entityType) {
        this.entityType = entityType;
        if (Objects.equals(entityType, "Char")) {
            this.liveCharDetails = repository.getMutableCharacterDetail();
        } else if (Objects.equals(entityType, "Staff")) {
            this.liveStaffDetails = repository.getMutableStaffDetail();
        }
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

    // clear requests
    public void clearComposite(){
        repository.clearComposite();
    }
}