package musicplayer.party.SpotifyService;

/**
 * Created by YLTL on 6/18/16.
 */
public class UserProfile {
    static final int PREFERENCE_LENGTH = 5;
    public static String[] guestArtistsPreferences = new String[PREFERENCE_LENGTH]; // array to store guestTracksPreferences chosen by user for personalization
    public static String[] guestTracksPreferences = new String[PREFERENCE_LENGTH]; // array to store guestTracksPreferences chosen by user for personalization
    /**
     * Flag that used to identify login status.
     */
    public static boolean DEFAULT_LOGIN_STATUS = false;
    public String userID;
}
