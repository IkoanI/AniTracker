package com.example.anitracker.repository;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.apollographql.apollo3.ApolloCall;
import com.apollographql.apollo3.ApolloClient;
import com.apollographql.apollo3.rx3.Rx3Apollo;
import com.example.anitracker.AnimeMoreDetailsQuery;
import com.example.anitracker.AnimeSearchPageQuery;
import com.example.anitracker.CharacterPageQuery;
import com.example.anitracker.MangaMoreDetailsQuery;
import com.example.anitracker.MangaSearchPageQuery;
import com.example.anitracker.RelationsPageQuery;
import com.example.anitracker.StaffPageQuery;
import com.example.anitracker.TopAnimePageQuery;
import com.example.anitracker.TopMangaPageQuery;
import com.example.anitracker.animeObjects.AiringSchedule;
import com.example.anitracker.animeObjects.AnimeDetails;
import com.example.anitracker.animeObjects.Studios;
import com.example.anitracker.clients.AniClient;
import com.example.anitracker.clients.VNDBClient;
import com.example.anitracker.vnObjects.VNCharPage;
import com.example.anitracker.vnObjects.VNCharacter;
import com.example.anitracker.vnObjects.VNRequestBody;
import com.example.anitracker.fragment.CommonDetails;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiRepository {
    private final ApolloClient aniClient = AniClient.INSTANCE.getClient();
    private final VNDBApi VNClient = VNDBClient.INSTANCE.getClient().create(VNDBApi.class);
    // anime search page
    private final MutableLiveData<List<AnimeDetails>> mutableAnimePage = new MutableLiveData<>();
    // manga search page
    private final MutableLiveData<List<MangaDetails>> mutableMangaPage = new MutableLiveData<>();
    // visual novel search page
    private final MutableLiveData<VNPage> mutableVNPage = new MutableLiveData<>();
    // overview fragment
    private final MutableLiveData<MediaDetails> mutableLiveData = new MutableLiveData<>();
    // character fragment
    private final MutableLiveData<List<CharacterDetails>> mutableCharPage = new MutableLiveData<>();
    private final MutableLiveData<VNCharPage> mutableVNCharPage = new MutableLiveData<>();
    // staff fragment
    private final MutableLiveData<List<StaffDetails>> mutableStaffPage = new MutableLiveData<>();
    // relations fragment
    private final MutableLiveData<List<MediaDetails>> mutableRelationsPage = new MutableLiveData<>();
    // error message
    private final MutableLiveData<String> mutableErrorMsg = new MutableLiveData<>();
    // help clear requests upon activity death
    private final CompositeDisposable compositeDisposable = new CompositeDisposable();

    public ApiRepository() {}

    public void clearComposite(){
        compositeDisposable.dispose();
    }

    public MutableLiveData<MediaDetails>  getMutableLiveData() { return  mutableLiveData; }

    public MutableLiveData<List<AnimeDetails>> getMutableAnimePage() {
        return mutableAnimePage;
    }

    public MutableLiveData<List<MangaDetails>> getMutableMangaPage() {
        return mutableMangaPage;
    }

    public MutableLiveData<VNPage> getMutableVNPage() { return mutableVNPage; }

    public MutableLiveData<List<CharacterDetails>> getMutableCharPage() {
        return mutableCharPage;
    }

    public MutableLiveData<VNCharPage> getMutableVNCharPage() {
        return mutableVNCharPage;
    }

    public MutableLiveData<List<StaffDetails>> getMutableStaffPage() {
        return mutableStaffPage;
    }

    public MutableLiveData<List<MediaDetails>> getMutableRelationsPage() {
        return mutableRelationsPage;
    }

    public MutableLiveData<String> getMutableErrorMsg() { return mutableErrorMsg; }

    private void setCommonDetails(MediaDetails mediaDetails, CommonDetails commonDetails){
        mediaDetails.setTitles(new Titles(commonDetails.title.english, commonDetails.title.native_, commonDetails.title.romaji, commonDetails.title.userPreferred));
        mediaDetails.setCoverImg(commonDetails.coverImage.large);
        if(commonDetails.averageScore != null){mediaDetails.setAvgScore(commonDetails.averageScore);}
        mediaDetails.setFormat(AnilistObjectMappings.mediaFormatToString.get(commonDetails.format));
        if(commonDetails.startDate.year != null){
            int startMonth = -1, startDay = -1;
            if(commonDetails.startDate.month != null){
                startMonth = commonDetails.startDate.month;
            }

            if(commonDetails.startDate.day != null){
                startDay = commonDetails.startDate.day;
            }
            mediaDetails.setStartDate(new Date(commonDetails.startDate.year, startMonth, startDay));
        }
        if(commonDetails.endDate.year != null){
            int endMonth = -1, endDay = -1;
            if(commonDetails.startDate.month != null){
                endMonth = commonDetails.endDate.month;
            }

            if(commonDetails.startDate.day != null){
                endDay = commonDetails.endDate.day;
            }
            mediaDetails.setEndDate(new Date(commonDetails.endDate.year, endMonth, endDay));
        }
        if(!commonDetails.genres.isEmpty()){mediaDetails.setGenres(commonDetails.genres);}
        mediaDetails.setFavorites(commonDetails.favourites);
        mediaDetails.setId(commonDetails.id);
        mediaDetails.setDesc(commonDetails.description);
        if(commonDetails.bannerImage != null){mediaDetails.setBanner(commonDetails.bannerImage);}
        if(commonDetails.meanScore != null){mediaDetails.setMeanScore(commonDetails.meanScore);}
        mediaDetails.setPopularity(commonDetails.popularity);
        if(commonDetails.source != null){mediaDetails.setSource(AnilistObjectMappings.mediaSourceToString.get(commonDetails.source));}
        if(commonDetails.hashtag != null){mediaDetails.setHashtags(commonDetails.hashtag);}
        mediaDetails.setStatus(AnilistObjectMappings.mediaStatusToString.get(commonDetails.status));
        if(commonDetails.trailer != null){mediaDetails.setTrailer(commonDetails.trailer);}
        mediaDetails.setSynonyms(commonDetails.synonyms);
        if(commonDetails.tags != null){mediaDetails.setTags(commonDetails.tags);}
    }

    public void fetchAnimeData (int id){
        ApolloCall<AnimeMoreDetailsQuery.Data> queryCall;
        queryCall = aniClient.query(new AnimeMoreDetailsQuery(id));
        AnimeDetails animeDetails = new AnimeDetails();
        compositeDisposable.add(
                Rx3Apollo.single(queryCall)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(res -> {
                            assert res.data != null;
                            CommonDetails commonDetails = res.data.Media.commonDetails;
                            setCommonDetails(animeDetails, commonDetails);
                            AnimeMoreDetailsQuery.Media anime = res.data.Media;
                            if(anime.duration != null){animeDetails.setDuration(anime.duration);}
                            if(anime.episodes != null){animeDetails.setEpisodes(anime.episodes);}
                            if(anime.season != null){animeDetails.setSeason(AnilistObjectMappings.mediaSeasonToString.get(anime.season));}
                            if(anime.nextAiringEpisode != null){animeDetails.setAiringSchedule(new AiringSchedule(anime.nextAiringEpisode.episode, anime.nextAiringEpisode.timeUntilAiring));}
                            if(!anime.studios.edges.isEmpty()){
                                Studios studios = new Studios();
                                for(AnimeMoreDetailsQuery.Edge studio : anime.studios.edges){
                                    if (studio.isMain){
                                        studios.addAnimationStudio(studio.node.name);
                                    }
                                    else{
                                        studios.addProducer(studio.node.name);
                                    }
                                }
                                animeDetails.setStudios(studios);
                            }
                            mutableLiveData.setValue(animeDetails);
                        },
                                error -> mutableErrorMsg.setValue(error.getMessage()))
        );
    }

    public void fetchMangaData (int id){
        ApolloCall<MangaMoreDetailsQuery.Data> queryCall;
        queryCall = aniClient.query(new MangaMoreDetailsQuery(id));
        MangaDetails mangaDetails = new MangaDetails();
        compositeDisposable.add(
                Rx3Apollo.single(queryCall)
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(res -> {
                                    assert res.data != null;
                                    CommonDetails commonDetails = res.data.Media.commonDetails;
                                    setCommonDetails(mangaDetails, commonDetails);
                                    MangaMoreDetailsQuery.Media manga = res.data.Media;
                                    if(manga.chapters != null){mangaDetails.setChapters(manga.chapters);}
                                    if(manga.volumes != null){mangaDetails.setVolumes(manga.volumes);}
                                    mutableLiveData.setValue(mangaDetails);
                                },
                                error -> mutableErrorMsg.setValue(error.getMessage()))
        );
    }

    public void fetchTopAnimePage(int page){
        ApolloCall<TopAnimePageQuery.Data> queryCall = aniClient.query(new TopAnimePageQuery(page));
        compositeDisposable.add(Rx3Apollo.single(queryCall)
                                .subscribeOn(Schedulers.newThread())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(res -> {
                                    assert res.data != null;
                                    List<AnimeDetails> animeList = new ArrayList<>();
                                    List<TopAnimePageQuery.Medium> shows = res.data.Page.media;
                                    for (TopAnimePageQuery.Medium show : shows) {
                                        AnimeDetails details = new AnimeDetails();
                                        details.setTitles(new Titles(null, null, null, show.title.userPreferred));
                                        details.setCoverImg(show.coverImage.large);
                                        details.setAvgScore(show.averageScore);
                                        details.setFormat(AnilistObjectMappings.mediaFormatToString.get(show.format));
                                        if (show.season != null) {
                                            details.setSeason(AnilistObjectMappings.mediaSeasonToString.get(show.season));
                                        }
                                        if(show.startDate.year != null){
                                            details.setStartDate(new Date(show.startDate.year, -1, -1));
                                        }
                                        if (show.duration != null) {
                                            details.setDuration(show.duration);
                                        }
                                        if (show.episodes != null) {
                                            details.setEpisodes(show.episodes);
                                        }
                                        if (!show.genres.isEmpty()) {
                                            details.setGenres(show.genres);
                                        }
                                        details.setFavorites(show.favourites);
                                        if (show.nextAiringEpisode != null) {
                                            details.setAiringSchedule(new AiringSchedule(show.nextAiringEpisode.episode, show.nextAiringEpisode.timeUntilAiring));
                                        }
                                        if (!show.studios.nodes.isEmpty()) {
                                            Studios studios = new Studios();
                                            for(TopAnimePageQuery.Node studio : show.studios.nodes){
                                                studios.addAnimationStudio(studio.name);
                                            }
                                            details.setStudios(studios);
                                        }
                                        details.setId(show.id);
                                        details.setType(MediaType.ANIME);
                                        animeList.add(details);
                                    }
                                    mutableAnimePage.setValue(animeList);
                                },
                                        error -> mutableErrorMsg.setValue(error.getMessage()))
        );
    }

    public void fetchUserAnimeSearch(int page, String userSearch){
        ApolloCall<AnimeSearchPageQuery.Data> queryCall = aniClient.query(new AnimeSearchPageQuery(page, userSearch));
        compositeDisposable.add(Rx3Apollo.single(queryCall)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                            assert res.data != null;
                            List<AnimeDetails> animeList = new ArrayList<>();
                            List<AnimeSearchPageQuery.Medium> shows = res.data.Page.media;
                            for (AnimeSearchPageQuery.Medium show : shows) {
                                AnimeDetails details = new AnimeDetails();
                                details.setTitles(new Titles(null, null, null, show.title.userPreferred));
                                details.setCoverImg(show.coverImage.large);
                                if(show.averageScore != null){
                                    details.setAvgScore(show.averageScore);
                                }
                                else{
                                    details.setAvgScore(0);
                                }

                                details.setFormat(AnilistObjectMappings.mediaFormatToString.get(show.format));
                                if (show.season != null) {
                                    details.setSeason(AnilistObjectMappings.mediaSeasonToString.get(show.season));
                                }
                                if(show.startDate.year != null){
                                    details.setStartDate(new Date(show.startDate.year, -1, -1));
                                }
                                if (show.duration != null) {
                                    details.setDuration(show.duration);
                                }
                                if (show.episodes != null) {
                                    details.setEpisodes(show.episodes);
                                }
                                if (!show.genres.isEmpty()) {
                                    details.setGenres(show.genres);
                                }
                                details.setFavorites(show.favourites);
                                if (show.nextAiringEpisode != null) {
                                    details.setAiringSchedule(new AiringSchedule(show.nextAiringEpisode.episode, show.nextAiringEpisode.timeUntilAiring));
                                }
                                if (!show.studios.nodes.isEmpty()) {
                                    Studios studios = new Studios();
                                    for(AnimeSearchPageQuery.Node studio : show.studios.nodes){
                                        studios.addAnimationStudio(studio.name);
                                    }
                                    details.setStudios(studios);
                                }
                                details.setId(show.id);
                                details.setType(MediaType.ANIME);
                                animeList.add(details);
                            }
                            mutableAnimePage.setValue(animeList);
                        },
                        error -> mutableErrorMsg.setValue(error.getMessage()))
        );
    }

    public void fetchTopMangaPage(int page){
        ApolloCall<TopMangaPageQuery.Data> queryCall = aniClient.query(new TopMangaPageQuery(page));
        compositeDisposable.add(Rx3Apollo.single(queryCall)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                            assert res.data != null;
                            List<MangaDetails> mangaList = new ArrayList<>();
                            List<TopMangaPageQuery.Medium> mangas = res.data.Page.media;
                            for (TopMangaPageQuery.Medium manga : mangas) {
                                MangaDetails details = new MangaDetails();
                                details.setTitles(new Titles(null, null, null, manga.title.userPreferred));
                                details.setCoverImg(manga.coverImage.large);
                                if(manga.averageScore != null){
                                    details.setAvgScore(manga.averageScore);
                                }
                                else{
                                    details.setAvgScore(0);
                                }

                                details.setFormat(AnilistObjectMappings.mediaFormatToString.get(manga.format));
                                if(manga.startDate.year != null){
                                    details.setStartDate(new Date(manga.startDate.year, -1, -1));
                                }

                                if(manga.endDate.year != null){
                                    details.setEndDate(new Date(manga.endDate.year, -1, -1));
                                }

                                if (!manga.genres.isEmpty()) {
                                    details.setGenres(manga.genres);
                                }
                                if (manga.volumes != null){
                                    details.setVolumes(manga.volumes);
                                }
                                details.setStatus(AnilistObjectMappings.mediaStatusToString.get(manga.status));
                                details.setFavorites(manga.favourites);
                                details.setId(manga.id);
                                details.setType(MediaType.MANGA);
                                mangaList.add(details);
                            }
                            mutableMangaPage.setValue(mangaList);
                        },
                        error -> mutableErrorMsg.setValue(error.getMessage()))
        );
    }

    public void fetchUserMangaSearch(int page, String userSearch){
        ApolloCall<MangaSearchPageQuery.Data> queryCall = aniClient.query(new MangaSearchPageQuery(page, userSearch));
        compositeDisposable.add(Rx3Apollo.single(queryCall)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                            assert res.data != null;
                            List<MangaDetails> mangaList = new ArrayList<>();
                            List<MangaSearchPageQuery.Medium> mangas = res.data.Page.media;
                            for (MangaSearchPageQuery.Medium manga : mangas) {
                                MangaDetails details = new MangaDetails();
                                details.setTitles(new Titles(null, null, null, manga.title.userPreferred));
                                details.setCoverImg(manga.coverImage.large);
                                if(manga.averageScore != null){
                                    details.setAvgScore(manga.averageScore);
                                }
                                else{
                                    details.setAvgScore(0);
                                }

                                details.setFormat(AnilistObjectMappings.mediaFormatToString.get(manga.format));
                                if(manga.startDate.year != null){
                                    details.setStartDate(new Date(manga.startDate.year, -1, -1));
                                }

                                if (!manga.genres.isEmpty()) {
                                    details.setGenres(manga.genres);
                                }
                                if (manga.volumes != null){
                                    details.setVolumes(manga.volumes);
                                }
                                details.setStatus(AnilistObjectMappings.mediaStatusToString.get(manga.status));
                                details.setFavorites(manga.favourites);
                                details.setId(manga.id);
                                details.setType(MediaType.MANGA);
                                mangaList.add(details);
                            }
                            mutableMangaPage.setValue(mangaList);
                        },
                        error -> mutableErrorMsg.setValue(error.getMessage()))
        );
    }

    public void fetchCharPage(int mediaId, int pageNo, StaffLanguage language){
        ApolloCall<CharacterPageQuery.Data> charPageCall = aniClient.query(new CharacterPageQuery(mediaId, pageNo, language));
        compositeDisposable.add(Rx3Apollo.single(charPageCall)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(res -> {
                    assert res.data != null;
                    List<CharacterDetails> charPage = new ArrayList<>();
                    List<CharacterPageQuery.Edge> characters = res.data.Media.characters.edges;
                    for(CharacterPageQuery.Edge character : characters){
                        CharacterPageQuery.Node charNode = character.node;
                        List<CharacterPageQuery.VoiceActorRole> voiceActors = character.voiceActorRoles;
                        if(!voiceActors.isEmpty()){
                            for(CharacterPageQuery.VoiceActorRole voiceActorRole : voiceActors){
                                CharacterDetails characterDetails = new CharacterDetails();
                                characterDetails.setName(new Name(charNode.name.userPreferred));
                                if(charNode.image.large != null){characterDetails.setImage(charNode.image.large);}
                                characterDetails.setRole(AnilistObjectMappings.characterRoleToString.get(character.role));
                                characterDetails.setId(charNode.id);
                                StaffDetails voiceActor = new StaffDetails();
                                voiceActor.setName(new Name(voiceActorRole.voiceActor.name.userPreferred));
                                voiceActor.setImage(voiceActorRole.voiceActor.image.large);
                                characterDetails.setVoiceActor(voiceActor);
                                characterDetails.setNotes(voiceActorRole.roleNotes);
                                charPage.add(characterDetails);
                            }
                        }
                        else{
                            CharacterDetails characterDetails = new CharacterDetails();
                            characterDetails.setName(new Name(charNode.name.userPreferred));
                            if(charNode.image.large != null){characterDetails.setImage(charNode.image.large);}
                            characterDetails.setRole(AnilistObjectMappings.characterRoleToString.get(character.role));
                            characterDetails.setId(charNode.id);
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
                        if(edge.role != null){ staffDetails.setRole(edge.role);}
                        staffDetails.setName(new Name(edge.node.name.userPreferred));
                        staffDetails.setImage(edge.node.image.large);
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
                                mediaDetails.setCoverImg(edge.node.coverImage.large);
                                mediaDetails.setTitles(new Titles(null, null, null, edge.node.title.userPreferred));
                                mediaDetails.setRelation(AnilistObjectMappings.mediaRelationsToString.get(edge.relationType));
                                mediaDetails.setFormat(AnilistObjectMappings.mediaFormatToString.get(edge.node.format));
                                mediaDetails.setStatus(AnilistObjectMappings.mediaStatusToString.get(edge.node.status));
                                mediaDetails.setType(edge.node.type);
                                mediaDetails.setId(edge.node.id);
                                relationsPage.add(mediaDetails);
                            }
                            mutableRelationsPage.setValue(relationsPage);
                        },
                        error -> mutableErrorMsg.setValue(error.getMessage())
                ));
    }

    public void fetchDefaultVNPage(String sort, String fields, int page){
        VNRequestBody body = new VNRequestBody(sort, true, 50, page, fields, null);
        fetchVNPage(body);
    }

    public void fetchVNSearchPage(String search, String sort, String fields, int page){
        List<Object> filters = Arrays.asList("search", "=", search);
        VNRequestBody body = new VNRequestBody(sort, false, 50, page, fields, filters);
        fetchVNPage(body);
    }

    public void fetchVNPage(VNRequestBody body){
        Call<VNPage> call = VNClient.fetchVNPage(body);
        call.enqueue(new Callback<VNPage>() {
            @Override
            public void onResponse(@NonNull Call<VNPage> call, @NonNull Response<VNPage> response) {
                mutableVNPage.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<VNPage> call, @NonNull Throwable throwable) {
                mutableErrorMsg.setValue(throwable.getMessage());
            }
        });
    }

    public void fetchVNData(String vndbID){
        String fields = "title, titles{lang, title, latin, official, main}, " +
                "image{thumbnail}, released, length, length_minutes, length_votes, rating, " +
                "average, developers{name}, description, devstatus, " +
                "tags{name, rating, spoiler}, aliases, screenshots{url}";

        List<Object> filters = Arrays.asList("id", "=", vndbID);
        VNRequestBody body = new VNRequestBody(null, false, 1, 1, fields, filters);
        Call<VNPage> call = VNClient.fetchVNDetails(body);
        call.enqueue(new Callback<VNPage>() {
            @Override
            public void onResponse(@NonNull Call<VNPage> call, @NonNull Response<VNPage> response) {
                assert response.body() != null;
                mutableLiveData.setValue(response.body().getVnDetailsList().get(0).convertToMediaObject());
            }

            @Override
            public void onFailure(@NonNull Call<VNPage> call, @NonNull Throwable throwable) {
                mutableErrorMsg.setValue(throwable.getMessage());
            }
        });
    }

    public void fetchVNChars(String vndbID, int page){
        String fields = "name, image{url}, vns{role}";
        List<Object> filters = Arrays.asList("vn","=", new String[]{"id","=",vndbID});
        VNRequestBody body = new VNRequestBody("name", false, 50, page, fields, filters);
        Call<VNCharPage> call = VNClient.fetchVNCharsTest(body);
        call.enqueue(new Callback<VNCharPage>() {
            @Override
            public void onResponse(@NonNull Call<VNCharPage> call, @NonNull Response<VNCharPage> response) {
                assert response.body() != null;
                mutableVNCharPage.setValue(response.body());
            }

            @Override
            public void onFailure(@NonNull Call<VNCharPage> call, @NonNull Throwable throwable) {
                mutableErrorMsg.setValue(throwable.getMessage());
            }
        });
    }
}
