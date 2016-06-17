package musicplayer.party;

import android.util.Log;

/**
 * Created by Nikita Jain on 6/11/2016.
 */
public class Variable {

    /**
     * Set clientID, redirectURL and requestCode according to Spotify developer account.
     */

    public static final String CLIENT_ID = "90aed54790a74dee92a61da9424b3ca3";
    public static final String REDIRECT_URI = "test1-musicplayer-login://callback/";
    public static final int REQUEST_CODE = 1337;
    public static String Access_Token;
    public static String[] guest_artists_preferences = new String[4]; // array to store guest_artists_preferences chosen by user for personalization
    public static String[] guest_tracks_preferences = new String[4]; // array to store guest_tracks_preferences chosen by user for personalization
    public static String[] recommendation_artist_ids = new String[5];
    public static String[] recommendation_track_ids = new String[5];
    public static String party_playlist_id;
    public static  String[] party_playlist_tracks = new String[40];
    public static int playlist_length ;
    public static int flag_post =0;






}
