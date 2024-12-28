package com.example.anitracker.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.apollographql.apollo3.ApolloCall;
import com.apollographql.apollo3.ApolloClient;
import com.apollographql.apollo3.api.Optional;
import com.apollographql.apollo3.rx3.Rx3Apollo;
import com.example.anitracker.AnimeMoreDetailsQuery;
import com.example.anitracker.AnimeSearchPageQuery;
import com.example.anitracker.CharacterDetailQuery;
import com.example.anitracker.CharacterPageQuery;
import com.example.anitracker.CharacterRolesQuery;
import com.example.anitracker.MangaMoreDetailsQuery;
import com.example.anitracker.MangaSearchPageQuery;
import com.example.anitracker.RelationsPageQuery;
import com.example.anitracker.StaffCharsQuery;
import com.example.anitracker.StaffDetailQuery;
import com.example.anitracker.StaffPageQuery;
import com.example.anitracker.StaffRolesQuery;
import com.example.anitracker.animeObjects.AnimeDetails;
import com.example.anitracker.animeObjects.Studios;
import com.example.anitracker.clients.AniClient;
import com.example.anitracker.clients.VNDBClient;
import com.example.anitracker.fragment.AnimeDetail;
import com.example.anitracker.fragment.AnimeShortDetail;
import com.example.anitracker.fragment.CharDetail;
import com.example.anitracker.fragment.Detail;
import com.example.anitracker.fragment.MangaDetail;
import com.example.anitracker.fragment.MangaShortDetail;
import com.example.anitracker.fragment.MediumDetail;
import com.example.anitracker.fragment.ShortCharDetail;
import com.example.anitracker.fragment.ShortDetail;
import com.example.anitracker.fragment.ShortStaffDetail;
import com.example.anitracker.fragment.StaffDetail;
import com.example.anitracker.type.CharacterRole;
import com.example.anitracker.type.MediaSort;
import com.example.anitracker.vnObjects.VNCharPage;
import com.example.anitracker.vnObjects.VNDetails;
import com.example.anitracker.vnObjects.VNRequestBody;
import com.example.anitracker.interfaces.VNDBApi;
import com.example.anitracker.mangaObjects.MangaDetails;
import com.example.anitracker.mediaObjects.CharacterDetails;
import com.example.anitracker.mediaObjects.Date;
import com.example.anitracker.mediaObjects.MediaDetails;
import com.example.anitracker.mediaObjects.Name;
import com.example.anitracker.mediaObjects.StaffDetails;
import com.example.anitracker.mediaObjects.Titles;
import com.example.anitracker.type.MediaType;
import com.example.anitracker.type.StaffLanguage;
import com.example.anitracker.vnObjects.VNPage;
import com.example.anitracker.vnObjects.VNStaffPage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiRepository {
    private final ApolloClient aniClient = AniClient.INSTANCE.getClient();
    private final VNDBApi VNClient = VNDBClient.INSTANCE.getClient().create(VNDBApi.class);
    // search fragment
    private final MutableLiveData<List<AnimeDetails>> mutableAnimeSearch = new MutableLiveData<>();
    private final MutableLiveData<List<MangaDetails>> mutableMangaSearch = new MutableLiveData<>();
    private final MutableLiveData<List<VNDetails>> mutableVNSearch = new MutableLiveData<>();
    // overview fragment
    private final MutableLiveData<MediaDetails> mutableLiveData = new MutableLiveData<>();
    // character fragment
    private final MutableLiveData<List<CharacterDetails>> mutableCharPage = new MutableLiveData<>();
    // staff fragment
    private final MutableLiveData<List<StaffDetails>> mutableStaffPage = new MutableLiveData<>();
    // relations fragment
    private final MutableLiveData<List<? extends MediaDetails>> mutableRelationPage = new MutableLiveData<>();
    // character overview
    private final MutableLiveData<CharacterDetails> mutableCharacterDetail = new MutableLiveData<>();
    // staff overview
    private final MutableLiveData<StaffDetails> mutableStaffDetail = new MutableLiveData<>();
    // error message
    private final MutableLiveData<String> mutableErrorMsg = new MutableLiveData<>();
    // help clear requests upon activity death
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();
    public void clearComposite(){
        compositeDisposable.dispose();
    }

    // getters
    public MutableLiveData<List<AnimeDetails>> getMutableAnimeSearch() {return mutableAnimeSearch;}
    public MutableLiveData<List<MangaDetails>> getMutableMangaSearch() {return mutableMangaSearch;}
    public MutableLiveData<List<VNDetails>> getMutableVNSearch() {return mutableVNSearch;}

    public MutableLiveData<MediaDetails>  getMutableLiveData() { return mutableLiveData; }

    public MutableLiveData<List<CharacterDetails>> getMutableCharPage() {
        return mutableCharPage;
    }

    public MutableLiveData<List<StaffDetails>> getMutableStaffPage() {
        return mutableStaffPage;
    }

    public MutableLiveData<List<? extends MediaDetails>> getMutableMediaPage() {return mutableRelationPage;}

    public MutableLiveData<CharacterDetails> getMutableCharacterDetail() {return this.mutableCharacterDetail;}

    public MutableLiveData<StaffDetails> getMutableStaffDetail() {return this.mutableStaffDetail;}

    public MutableLiveData<String> getMutableErrorMsg() { return mutableErrorMsg; }

    // fetch data functions
    public void fetchSearchResults(MediaType mediaType, int page, String userSearch, List<MediaSort> sort) {
        Optional<String> opUserSearch = Optional.present(userSearch);
        Optional<List<MediaSort>> opSort = Optional.present(sort);

        if (mediaType == MediaType.ANIME) {
            ApolloCall<AnimeSearchPageQuery.Data> animeQueryCall = aniClient.query(new AnimeSearchPageQuery(page, opUserSearch, opSort));
            compositeDisposable.add(Rx3Apollo.single(animeQueryCall)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(res -> {
                                assert res.data != null;
                                List<AnimeDetails> animeList = new ArrayList<>();
                                List<AnimeSearchPageQuery.Medium> results = res.data.Page.media;
                                for (AnimeSearchPageQuery.Medium result : results) {
                                    AnimeDetails animeDetails = new AnimeDetails();
                                    this.setMediumDetail(animeDetails, result.animeShortDetail.mediumDetail);
                                    this.setAnimeShortDetail(animeDetails, result.animeShortDetail);
                                    if (!result.studios.nodes.isEmpty()) {
                                        Studios studios = new Studios();
                                        for(AnimeSearchPageQuery.Node studio : result.studios.nodes){
                                            studios.addAnimationStudio(studio.name);
                                        }
                                        animeDetails.setStudios(studios);
                                    }
                                    animeList.add(animeDetails);
                                }
                                mutableAnimeSearch.setValue(animeList);
                            },
                            error -> mutableErrorMsg.setValue(error.getMessage()))
            );
        } else if (mediaType == MediaType.MANGA) {
            ApolloCall<MangaSearchPageQuery.Data> mangaQueryCall = aniClient.query(new MangaSearchPageQuery(page, opUserSearch, opSort));
            compositeDisposable.add(Rx3Apollo.single(mangaQueryCall)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(res -> {
                                assert res.data != null;
                                List<MangaDetails> mangaList = new ArrayList<>();
                                List<MangaSearchPageQuery.Medium> results = res.data.Page.media;
                                for (MangaSearchPageQuery.Medium result : results) {
                                    MangaDetails mangaDetails = new MangaDetails();
                                    this.setMediumDetail(mangaDetails, result.mangaShortDetail.mediumDetail);
                                    this.setMangaShortDetail(mangaDetails, result.mangaShortDetail);
                                    mangaList.add(mangaDetails);
                                }
                                mutableMangaSearch.setValue(mangaList);
                            },
                            error -> mutableErrorMsg.setValue(error.getMessage()))
            );
        } else if (mediaType == MediaType.VISUAL_NOVEL) {
            String fields = "title, image{thumbnail}, developers{name}, released, length, length_minutes, rating, id";
            if (userSearch == null) {
                this.fetchDefaultVNPage("rating", fields, page);
            } else {
                this.fetchVNSearchPage(userSearch, "searchrank", fields, page);
            }
        }
    }

    private void fetchAnimeData (int id){
        ApolloCall<AnimeMoreDetailsQuery.Data> queryCall;
        queryCall = aniClient.query(new AnimeMoreDetailsQuery(id));
        AnimeDetails animeDetails = new AnimeDetails();
        compositeDisposable.add(
                Rx3Apollo.single(queryCall)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(res -> {
                            assert res.data != null;
                            this.setCommonDetails(animeDetails, res.data.Media.animeDetail.detail);
                            this.setAnimeDetail(animeDetails, res.data.Media.animeDetail);
                            mutableLiveData.setValue(animeDetails);
                        },
                                error -> mutableErrorMsg.setValue(error.getMessage()))
        );
    }

    private void fetchMangaData (int id) {
        ApolloCall<MangaMoreDetailsQuery.Data> queryCall;
        queryCall = aniClient.query(new MangaMoreDetailsQuery(id));
        MangaDetails mangaDetails = new MangaDetails();
        compositeDisposable.add(
                Rx3Apollo.single(queryCall)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(res -> {
                                    assert res.data != null;
                                    this.setCommonDetails(mangaDetails, res.data.Media.mangaDetail.detail);
                                    this.setMangaDetail(mangaDetails, res.data.Media.mangaDetail);
                                    mutableLiveData.setValue(mangaDetails);
                                },
                                error -> mutableErrorMsg.setValue(error.getMessage()))
        );
    }

    public void fetchData(MediaType mediaType, String id) {
        if (mediaType == MediaType.ANIME) {
            this.fetchAnimeData(Integer.parseInt(id));
        } else if (mediaType == MediaType.MANGA) {
            this.fetchMangaData(Integer.parseInt(id));
        } else if (mediaType == MediaType.VISUAL_NOVEL) {
            this.fetchVNData(id);
        }
    }

    public void fetchCharPage(int mediaId, int pageNo, StaffLanguage language) {
        ApolloCall<CharacterPageQuery.Data> charPageCall = aniClient.query(new CharacterPageQuery(mediaId, pageNo, language));
        compositeDisposable.add(Rx3Apollo.single(charPageCall)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    assert res.data != null;
                    List<CharacterDetails> charPage = new ArrayList<>();
                    List<CharacterPageQuery.Edge> characters = res.data.Media.characters.edges;
                    for (CharacterPageQuery.Edge character : characters) {
                        ShortCharDetail charDetail = character.node.shortCharDetail;
                        List<CharacterPageQuery.VoiceActorRole> voiceActors = character.voiceActorRoles;
                        if (!voiceActors.isEmpty()) {
                            for (CharacterPageQuery.VoiceActorRole voiceActorRole : voiceActors) {
                                CharacterDetails characterDetails = new CharacterDetails();
                                this.setShortCharDetails(charDetail, characterDetails);
                                characterDetails.setRole(AnilistObjectMappings.characterRoleToString.get(character.role));

                                StaffDetails voiceActor = new StaffDetails();
                                ShortStaffDetail staffDetail = voiceActorRole.voiceActor.shortStaffDetail;
                                this.setShortStaffDetails(staffDetail, voiceActor);

                                characterDetails.setVoiceActor(voiceActor);
                                characterDetails.setNotes(voiceActorRole.roleNotes);

                                charPage.add(characterDetails);
                            }
                        } else {
                            CharacterDetails characterDetails = new CharacterDetails();
                            this.setShortCharDetails(charDetail, characterDetails);
                            characterDetails.setRole(AnilistObjectMappings.characterRoleToString.get(character.role));

                            charPage.add(characterDetails);
                        }
                    }
                    mutableCharPage.setValue(charPage);
                },

                        error -> mutableErrorMsg.setValue(error.getMessage()))
        );
    }

    public void fetchStaffPage(int mediaId, int pageNo){
        ApolloCall<StaffPageQuery.Data> staffPageCall = aniClient.query(new StaffPageQuery(mediaId, pageNo));
        compositeDisposable.add(Rx3Apollo.single(staffPageCall)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(res -> {
                    assert res.data != null;
                    List<StaffDetails> staffPage = new ArrayList<>();
                    for(StaffPageQuery.Edge edge : res.data.Media.staff.edges){
                        StaffDetails staffDetails = new StaffDetails();
                        staffDetails.setRole(edge.role);
                        ShortStaffDetail staffDetail = edge.node.shortStaffDetail;
                        this.setShortStaffDetails(staffDetail, staffDetails);
                        staffPage.add(staffDetails);
                    }
                    mutableStaffPage.setValue(staffPage);
                },
                        error -> mutableErrorMsg.setValue(error.getMessage())
                ));
    }

    public void fetchRelationsPage(int mediaId){
        ApolloCall<RelationsPageQuery.Data> relationsPageCall = aniClient.query(new RelationsPageQuery(mediaId));
        compositeDisposable.add(Rx3Apollo.single(relationsPageCall)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(res -> {
                            assert res.data != null;
                            List<MediaDetails> relationsPage = new ArrayList<>();
                            for(RelationsPageQuery.Edge edge : res.data.Media.relations.edges){
                                MediaDetails mediaDetails = new MediaDetails();
                                ShortDetail detail = edge.node.shortDetail;
                                this.setShortDetail(mediaDetails, detail);
                                mediaDetails.setRelation(edge.relationType);
                                relationsPage.add(mediaDetails);
                            }
                            mutableRelationPage.setValue(relationsPage);
                        },
                        error -> mutableErrorMsg.setValue(error.getMessage())
                ));
    }

    public void fetchCharDetails(int id) {
        ApolloCall<CharacterDetailQuery.Data> charDetailCall = aniClient.query(new CharacterDetailQuery(id));
        CharacterDetails characterDetails = new CharacterDetails();
        compositeDisposable.add(Rx3Apollo.single(charDetailCall)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(res -> {
                    assert res.data != null;
                    this.setCharDetails(res.data.Character.charDetail, characterDetails);
                    mutableCharacterDetail.setValue(characterDetails);
                },
                        error -> mutableErrorMsg.setValue(error.getMessage()))
        );
    }

    public void fetchCharRoles(int id, int page) {
        ApolloCall<CharacterRolesQuery.Data> charRolesCall = aniClient.query(new CharacterRolesQuery(id, page));
        List<MediaDetails> roles = new ArrayList<>();
        compositeDisposable.add(Rx3Apollo.single(charRolesCall)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(res -> {
                            assert res.data != null;
                            for (CharacterRolesQuery.Edge media : res.data.Character.media.edges) {
                                MediaDetails mediaDetails = new MediaDetails();
                                this.setShortDetail(mediaDetails, media.node.shortDetail);
                                if (media.characterRole != null) {mediaDetails.setRelation(media.characterRole);}
                                roles.add(mediaDetails);
                            }

                            mutableRelationPage.setValue(roles);
                        },
                        error -> mutableErrorMsg.setValue(error.getMessage()))
        );
    }

    public void fetchStaffDetail(int id) {
        ApolloCall<StaffDetailQuery.Data> staffDetailCall = aniClient.query(new StaffDetailQuery(id));
        StaffDetails staffDetails = new StaffDetails();
        compositeDisposable.add(Rx3Apollo.single(staffDetailCall)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(res -> {
                            assert res.data != null;
                            this.setStaffDetails(res.data.Staff.staffDetail, staffDetails);
                            mutableStaffDetail.setValue(staffDetails);
                        },
                        error -> mutableErrorMsg.setValue(error.getMessage()))
        );
    }

    public void fetchStaffChars(int id, int page) {
        ApolloCall<StaffCharsQuery.Data> staffCharsCall = aniClient.query(new StaffCharsQuery(id, page));
        List<CharacterDetails> staffChars = new ArrayList<>();
        compositeDisposable.add(Rx3Apollo.single(staffCharsCall)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(res -> {
                            assert res.data != null;
                            List<StaffCharsQuery.Edge> edges = res.data.Staff.characterMedia.edges;
                            for (StaffCharsQuery.Edge edge : edges) {
                                CharacterRole role = edge.characterRole;
                                MediaDetails charMedia = new MediaDetails();
                                this.setShortDetail(charMedia, edge.node.shortDetail);
                                for (StaffCharsQuery.Character character : edge.characters) {
                                    CharacterDetails characterDetails = new CharacterDetails();
                                    this.setShortCharDetails(character.shortCharDetail, characterDetails);
                                    characterDetails.setCharMedia(charMedia);
                                    characterDetails.setRole(AnilistObjectMappings.characterRoleToString.get(role));
                                    staffChars.add(characterDetails);
                                }
                            }
                            mutableCharPage.setValue(staffChars);
                        },
                        error -> mutableErrorMsg.setValue(error.getMessage()))
        );
    }

    public void fetchStaffRoles(int id, int page) {
        ApolloCall<StaffRolesQuery.Data> staffRolesCall = aniClient.query(new StaffRolesQuery(id, page));
        List<MediaDetails> roles = new ArrayList<>();
        compositeDisposable.add(Rx3Apollo.single(staffRolesCall)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(res -> {
                            assert res.data != null;
                            for (StaffRolesQuery.Edge media : res.data.Staff.staffMedia.edges) {
                                MediaDetails mediaDetails = new MediaDetails();
                                this.setShortDetail(mediaDetails, media.node.shortDetail);
                                mediaDetails.setRelation(media.staffRole);
                                roles.add(mediaDetails);
                            }

                            mutableRelationPage.setValue(roles);
                        },
                        error -> mutableErrorMsg.setValue(error.getMessage()))
        );
    }

    // VNDB API calls

    private void fetchVNPage(VNRequestBody body, String pageType){
        Call<VNPage> call = VNClient.fetchVNPage(body);
            call.enqueue(new Callback<VNPage>() {
                @Override
                public void onResponse(@NonNull Call<VNPage> call, @NonNull Response<VNPage> response) {
                    assert response.body() != null;
                    if (!Objects.equals(pageType, "Overview")) {
                        if (Objects.equals(pageType, "Search")) {
                            mutableVNSearch.setValue(response.body().getVnDetailsList());
                        } else {
                            mutableRelationPage.setValue(response.body().getVnDetailsList());
                        }
                    } else {
                        mutableLiveData.setValue(response.body().getVnDetailsList().get(0));
                    }
                }

                @Override
                public void onFailure(@NonNull Call<VNPage> call, @NonNull Throwable throwable) {
                    mutableErrorMsg.setValue(throwable.getMessage());
                }
            });
    }

    public void fetchDefaultVNPage(String sort, String fields, int page) {
        VNRequestBody body = new VNRequestBody(sort, true, 50, page, fields, null);
        this.fetchVNPage(body, "Search");
    }

    public void fetchVNSearchPage(String search, String sort, String fields, int page){
        List<Object> filters = Arrays.asList("search", "=", search);
        VNRequestBody body = new VNRequestBody(sort, false, 50, page, fields, filters);
        this.fetchVNPage(body, "Search");
    }

    private void fetchVNData(String vndbID) {
        String fields = "title, titles{lang, title, latin, official, main}, " +
                "image{thumbnail}, released, length, length_minutes, length_votes, rating, " +
                "average, developers{name}, description, devstatus, extlinks{label, url}, " +
                "tags{name, rating, spoiler}, aliases, screenshots{url}, " +
                "relations{title, relation, image{thumbnail}, devstatus}, languages, platforms, " +
                "editions{name}, staff{role, name, eid, note}, va{note, staff{name}, character{id}}";

        List<Object> filters = Arrays.asList("id", "=", vndbID);
        VNRequestBody body = new VNRequestBody(null, false, 1, 1, fields, filters);
        this.fetchVNPage(body, "Overview");
    }

    private void fetchVNCharPage(String vndbID, VNRequestBody body) {
        Call<VNCharPage> call = VNClient.fetchVNChars(body);
        call.enqueue(new Callback<VNCharPage>() {
            @Override
            public void onResponse(@NonNull Call<VNCharPage> call, @NonNull Response<VNCharPage> response) {
                assert response.body() != null;
                if (response.body().getSize() == 1) {
                    mutableCharacterDetail.setValue(response.body().getVNCharList(vndbID).get(0));
                } else {
                    mutableCharPage.setValue(response.body().getVNCharList(vndbID));
                }
            }

            @Override
            public void onFailure(@NonNull Call<VNCharPage> call, @NonNull Throwable throwable) {
                mutableErrorMsg.setValue(throwable.getMessage());
            }
        });
    }

    public void fetchVNChars(String vndbID, int page) {
        String fields = "name, image{url}, vns{role}";
        List<Object> filters = Arrays.asList("vn","=", new String[]{"id","=",vndbID});
        VNRequestBody body = new VNRequestBody("name", false, 50, page, fields, filters);
        this.fetchVNCharPage(vndbID,  body);
    }

    public void fetchVNCharDetails(String vndbID) {
        String fields = "name, original, aliases, description, image{url}, blood_type, " +
                "height, weight, bust, waist, hips, cup, age, birthday, sex, vns{role, title, image{thumbnail}, devstatus}, " +
                "traits{name, spoiler, group_name}";

        List<Object> filters = Arrays.asList("id", "=", vndbID);
        VNRequestBody body = new VNRequestBody(null, false, 1, 1, fields, filters);
        this.fetchVNCharPage(vndbID, body);
    }

    private void fetchVNStaffPage(VNRequestBody body) {
        Call<VNStaffPage> call = VNClient.fetchVNStaffs(body);
        call.enqueue(new Callback<VNStaffPage>() {
            @Override
            public void onResponse(@NonNull Call<VNStaffPage> call, @NonNull Response<VNStaffPage> response) {
                assert response.body() != null;
                if (response.body().getSize() > 1) {
                    mutableStaffPage.setValue(response.body().getVnStaffList());
                } else if (response.body().getSize() == 1){
                    mutableStaffDetail.setValue(response.body().getVnStaffList().get(0));
                }
            }

            @Override
            public void onFailure(@NonNull Call<VNStaffPage> call, @NonNull Throwable throwable) {
                mutableErrorMsg.setValue(throwable.getMessage());
            }
        });
    }

    public void fetchVNStaffDetail(String vndbID) {
        String fields = "name, original, lang, gender, description, aliases{name}, extlinks{label, url}";
        List<Object> filters = Arrays.asList("id", "=", vndbID);
        VNRequestBody body = new VNRequestBody(null, false, 1, 1, fields, filters);
        this.fetchVNStaffPage(body);
    }

    public void fetchVNStaffChars(String vndbID, int page) {
        String fields = "name, image{url}, vns{title, role, release{title}, image{thumbnail}}";
        List<Object> filters = Arrays.asList("seiyuu","=", new String[]{"id","=",vndbID});
        VNRequestBody body = new VNRequestBody(null, false, 50, page, fields, filters);
        this.fetchVNCharPage(vndbID, body);
    }

    public void fetchVNStaffRoles(String vndbID, int page) {
        String fields = "title, image{thumbnail}, staff{role, note}, devstatus";
        List<Object> filters = Arrays.asList("staff","=", new String[]{"id","=",vndbID});
        VNRequestBody body = new VNRequestBody(null, false, 50, page, fields, filters);
        this.fetchVNPage(body, "Relation");
    }

    // setter helper functions

    private void setShortDetail(MediaDetails mediaDetails, ShortDetail commonDetails) {
        mediaDetails.setTitles(new Titles(null, null, null, commonDetails.title.userPreferred));
        mediaDetails.setCoverImg(commonDetails.coverImage.large);
        mediaDetails.setFormat(AnilistObjectMappings.mediaFormatToString.get(commonDetails.format));
        mediaDetails.setId(String.valueOf(commonDetails.id));
        mediaDetails.setStatus(commonDetails.status);
        mediaDetails.setType(commonDetails.type);
    }

    private void setMediumDetail(MediaDetails mediaDetails, MediumDetail commonDetails) {
        this.setShortDetail(mediaDetails, commonDetails.shortDetail);
        mediaDetails.setAvgScore(commonDetails.averageScore);
        mediaDetails.setGenres(commonDetails.genres);
        mediaDetails.setFavorites(commonDetails.favourites);
        mediaDetails.setStartDate(new Date(commonDetails.startDate.year, -1, -1));
    }

    private void setCommonDetails(MediaDetails mediaDetails, Detail commonDetails) {
        this.setMediumDetail(mediaDetails, commonDetails.mediumDetail);
        mediaDetails.setTitles(new Titles(commonDetails.title.english, commonDetails.title.native_, commonDetails.title.romaji, commonDetails.title.userPreferred));
        mediaDetails.setStartDate(new Date(commonDetails.startDate.year, commonDetails.startDate.month, commonDetails.startDate.day));
        mediaDetails.setEndDate(new Date(commonDetails.endDate.year, commonDetails.endDate.month, commonDetails.endDate.day));
        mediaDetails.setDesc(commonDetails.description);
        mediaDetails.setBanner(commonDetails.bannerImage);
        mediaDetails.setMeanScore(commonDetails.meanScore);
        mediaDetails.setPopularity(commonDetails.popularity);
        mediaDetails.setSource(AnilistObjectMappings.mediaSourceToString.get(commonDetails.source));
        mediaDetails.setHashtags(commonDetails.hashtag);
        mediaDetails.setTrailer(commonDetails.trailer);
        mediaDetails.setSynonyms(commonDetails.synonyms);
        mediaDetails.setTags(commonDetails.tags);
    }

    private void setAnimeShortDetail(AnimeDetails animeDetails, AnimeShortDetail animeShortDetail) {
        animeDetails.setSeason(AnilistObjectMappings.mediaSeasonToString.get(animeShortDetail.season));
        animeDetails.setDuration(animeShortDetail.duration);
        animeDetails.setEpisodes(animeShortDetail.episodes, animeShortDetail.nextAiringEpisode);
        animeDetails.setType(MediaType.ANIME);
    }

    private void setAnimeDetail(AnimeDetails animeDetails, AnimeDetail animeDetail) {
        this.setAnimeShortDetail(animeDetails, animeDetail.animeShortDetail);
        if (!animeDetail.studios.edges.isEmpty()) {
            Studios studios = new Studios();
            for (AnimeDetail.Edge studio : animeDetail.studios.edges) {
                if (studio.isMain) {
                    studios.addAnimationStudio(studio.node.name);
                } else {
                    studios.addProducer(studio.node.name);
                }
            }
            animeDetails.setStudios(studios);
        }
    }

    private void setMangaShortDetail(MangaDetails mangaDetails, MangaShortDetail mangaShortDetail) {
        mangaDetails.setEndDate(new Date(mangaShortDetail.endDate.year, -1, -1));
        mangaDetails.setType(MediaType.MANGA);
        mangaDetails.setVolumes(mangaShortDetail.volumes);
    }

    private void setMangaDetail(MangaDetails mangaDetails, MangaDetail mangaDetail) {
        this.setMangaShortDetail(mangaDetails, mangaDetail.mangaShortDetail);
        mangaDetails.setChapters(mangaDetail.chapters);
    }

    private void setShortCharDetails(ShortCharDetail detail, CharacterDetails characterDetails) {
        characterDetails.setName(new Name(detail.name.userPreferred));
        characterDetails.setImage(detail.image.large);
        characterDetails.setId(String.valueOf(detail.id));
    }

    private void setCharDetails (CharDetail detail, CharacterDetails characterDetails) {
        this.setShortCharDetails(detail.shortCharDetail, characterDetails);
        Name name = new Name(detail.name.userPreferred);
        name.setFirst(detail.name.first);
        name.setMiddle(detail.name.middle);
        name.setLast(detail.name.last);
        name.setFull(detail.name.full);
        name.setNativeName(detail.name.native_);
        name.setAlternatives(detail.name.alternative);
        name.setAlternativeSpoilers(detail.name.alternativeSpoiler);
        characterDetails.setName(name);
        characterDetails.setDescription(detail.description);
        characterDetails.setAge(detail.age);
        characterDetails.setGender(detail.gender);
        characterDetails.setDateOfbirth(new Date(detail.dateOfBirth.year,
                detail.dateOfBirth.month,
                detail.dateOfBirth.day));
        characterDetails.setBloodtype(detail.bloodType);
        characterDetails.setFavorites(detail.favourites);
    }

    private void setShortStaffDetails(ShortStaffDetail detail, StaffDetails staffDetails) {
        staffDetails.setName(new Name(detail.name.userPreferred));
        staffDetails.setImage(detail.image.large);
        staffDetails.setId(String.valueOf(detail.id));
    }
    
    private void setStaffDetails(StaffDetail detail, StaffDetails staffDetails) {
        this.setShortStaffDetails(detail.shortStaffDetail, staffDetails);
        Name name = new Name(detail.name.userPreferred);
        name.setFirst(detail.name.first);
        name.setMiddle(detail.name.middle);
        name.setLast(detail.name.last);
        name.setFull(detail.name.full);
        name.setNativeName(detail.name.native_);
        name.setAlternatives(detail.name.alternative);
        staffDetails.setName(name);
        staffDetails.setLang(detail.languageV2);
        staffDetails.setDescription(detail.description);
        staffDetails.setPrimaryOccupations(detail.primaryOccupations);
        staffDetails.setGender(detail.gender);
        staffDetails.setDateOfbirth(new Date(detail.dateOfBirth.year,
                detail.dateOfBirth.month,
                detail.dateOfBirth.day));
        staffDetails.setDateOfDeath(new Date(detail.dateOfDeath.year,
                detail.dateOfDeath.month,
                detail.dateOfDeath.day));
        staffDetails.setAge(detail.age);
        staffDetails.setYearsActive(detail.yearsActive);
        staffDetails.setHomeTown(detail.homeTown);
        staffDetails.setBloodtype(detail.bloodType);
        staffDetails.setFavorites(detail.favourites);
    }
}
