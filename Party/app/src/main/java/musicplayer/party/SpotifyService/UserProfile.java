package musicplayer.party.SpotifyService;


/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: UserProfile
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 */
public class UserProfile {
    /**
     * Variable to state the length of the guest preference array.
     */
    static final int PREFERENCE_LENGTH = 5;
    /**
     * A string array to store user preferred top artists that will be used in personalization.
     */
    public static String[] guestArtistsPreferences = new String[PREFERENCE_LENGTH];
    /**
     * A string array to store user preferred top tracks that will be used in personalization.
     */
    public static String[] guestTracksPreferences = new String[PREFERENCE_LENGTH];
    /**
     * A flag that used to identify login status.
     */
    public static boolean DEFAULT_LOGIN_STATUS = false;
    /**
     * A string array to store user filtered preferred top artists that will be used in personalization.
     */
    public static String[] userFilteredPreferredArtists = new String[PREFERENCE_LENGTH];
    /**
     * A string array to store user filtered preferred top artists that will be used in personalization.
     */
    public static String[] userFilteredPreferredTracks = new String[PREFERENCE_LENGTH];
    /**
     * String to store the userID.
     */
    public String userID;
}
