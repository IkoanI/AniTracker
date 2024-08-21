package com.example.anitracker.repository;

import com.example.anitracker.type.CharacterRole;
import com.example.anitracker.type.MediaFormat;
import com.example.anitracker.type.MediaRelation;
import com.example.anitracker.type.MediaSeason;
import com.example.anitracker.type.MediaSource;
import com.example.anitracker.type.MediaStatus;
import com.example.anitracker.type.StaffLanguage;

import java.util.Map;

public class AnilistObjectMappings {
    public static Map<MediaRelation, String> mediaRelationsToString = Map.ofEntries(
            Map.entry(MediaRelation.ADAPTATION, "Adaptation"),
            Map.entry(MediaRelation.PREQUEL, "Prequel"),
            Map.entry(MediaRelation.SEQUEL, "Sequel"),
            Map.entry(MediaRelation.PARENT, "Parent"),
            Map.entry(MediaRelation.SIDE_STORY, "Side Story"),
            Map.entry(MediaRelation.CHARACTER, "Character"),
            Map.entry(MediaRelation.SUMMARY, "Summary"),
            Map.entry(MediaRelation.ALTERNATIVE, "Alternative"),
            Map.entry(MediaRelation.SPIN_OFF, "Spin Off"),
            Map.entry(MediaRelation.OTHER, "Other"),
            Map.entry(MediaRelation.SOURCE, "Source"),
            Map.entry(MediaRelation.COMPILATION, "Compilation"),
            Map.entry(MediaRelation.CONTAINS, "Contains")
    );

    public static Map<MediaSeason, String> mediaSeasonToString = Map.ofEntries(
            Map.entry(MediaSeason.FALL, "Fall"),
            Map.entry(MediaSeason.SPRING, "Spring"),
            Map.entry(MediaSeason.SUMMER, "Summer"),
            Map.entry(MediaSeason.WINTER, "Winter")
    );

    public static Map<MediaFormat, String> mediaFormatToString = Map.ofEntries(
            Map.entry(MediaFormat.TV, "TV"),
            Map.entry(MediaFormat.TV_SHORT, "TV Short"),
            Map.entry(MediaFormat.MOVIE, "Movie"),
            Map.entry(MediaFormat.SPECIAL, "Special"),
            Map.entry(MediaFormat.OVA, "OVA"),
            Map.entry(MediaFormat.ONA, "ONA"),
            Map.entry(MediaFormat.MUSIC, "Music"),
            Map.entry(MediaFormat.MANGA, "Manga"),
            Map.entry(MediaFormat.NOVEL, "Novel"),
            Map.entry(MediaFormat.ONE_SHOT, "One Shot")
    );

    public static Map<MediaStatus, String> mediaStatusToString = Map.ofEntries(
            Map.entry(MediaStatus.FINISHED, "Finished"),
            Map.entry(MediaStatus.RELEASING, "Releasing"),
            Map.entry(MediaStatus.NOT_YET_RELEASED, "Not Yet Released"),
            Map.entry(MediaStatus.CANCELLED, "Cancelled"),
            Map.entry(MediaStatus.HIATUS, "Hiatus")
    );

    public static Map<MediaSource, String> mediaSourceToString = Map.ofEntries(
            Map.entry(MediaSource.ORIGINAL, "Original"),
            Map.entry(MediaSource.MANGA, "Manga"),
            Map.entry(MediaSource.LIGHT_NOVEL, "Light Novel"),
            Map.entry(MediaSource.VISUAL_NOVEL, "Visual Novel"),
            Map.entry(MediaSource.VIDEO_GAME, "Video Game"),
            Map.entry(MediaSource.OTHER, "Other"),
            Map.entry(MediaSource.NOVEL, "Novel"),
            Map.entry(MediaSource.DOUJINSHI, "Doujinshi"),
            Map.entry(MediaSource.ANIME, "Anime"),
            Map.entry(MediaSource.WEB_NOVEL, "Web Novel"),
            Map.entry(MediaSource.LIVE_ACTION, "Live Action"),
            Map.entry(MediaSource.GAME, "Game"),
            Map.entry(MediaSource.COMIC, "Comic"),
            Map.entry(MediaSource.MULTIMEDIA_PROJECT, "Multimedia Project"),
            Map.entry(MediaSource.PICTURE_BOOK, "Picture Book")
    );

    public static Map<CharacterRole, String> characterRoleToString = Map.ofEntries(
            Map.entry(CharacterRole.MAIN, "Main"),
            Map.entry(CharacterRole.SUPPORTING, "Supporting"),
            Map.entry(CharacterRole.BACKGROUND, "Background")
    );

    public static Map<StaffLanguage, String> staffLanguageToString = Map.ofEntries(
            Map.entry(StaffLanguage.JAPANESE, "Japanese"),
            Map.entry(StaffLanguage.ENGLISH, "English"),
            Map.entry(StaffLanguage.KOREAN, "Korean"),
            Map.entry(StaffLanguage.ITALIAN, "Italian"),
            Map.entry(StaffLanguage.SPANISH, "Spanish"),
            Map.entry(StaffLanguage.PORTUGUESE, "Portuguese"),
            Map.entry(StaffLanguage.FRENCH, "French"),
            Map.entry(StaffLanguage.GERMAN, "German"),
            Map.entry(StaffLanguage.HEBREW, "Hebrew"),
            Map.entry(StaffLanguage.HUNGARIAN, "Hungarian")
    );
}
