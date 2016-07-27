package musicplayer.party.spotifyService;


import android.util.Log;

import java.util.ArrayList;

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: UserProfile
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 */
public class UserProfile {
    /**
     * Variable to state the length of the guest preference array.
     */
    public static final int PREFERENCE_LENGTH = 5;
    /**
     * A string array to store user preferred top artists that will be used in personalization.
     */
    public static String[] selfArtistsPreferences = new String[PREFERENCE_LENGTH];
    /**
     * A string array to store user preferred top tracks that will be used in personalization.
     */
    public static String[] selfTracksPreferences = new String[PREFERENCE_LENGTH];

    public static ArrayList<String[]> artistsPreferences = new ArrayList<>();
    public static ArrayList<String[]> tracksPreferences = new ArrayList<>();
    public static int userCounter = 0;

    public static void testingIncrementUserCounter () {
        userCounter++;
    }

    public static void addArtistList (String[] artistPrefStr) {
        artistsPreferences.add(artistPrefStr);
        Log.e("add artists to host","add artists to host");
    }

    public static void addTracksList (String[] trackPrefStr) {
        tracksPreferences.add(trackPrefStr);
        Log.e("add tracks to host","add tracks to host");
    }


    /**
     * A flag that used to identify login status.
     */
    public static boolean DEFAULT_LOGIN_STATUS = false;

    /**
     * String to store the userID.
     */
    public static String userID;


}
