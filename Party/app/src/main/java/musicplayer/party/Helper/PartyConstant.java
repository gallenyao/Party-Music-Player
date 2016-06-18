package musicplayer.party.Helper;

/**
 * Created by YLTL on 6/18/16.
 */
public class PartyConstant {
    /**
     * Set clientID, redirectURL and requestCode according to Spotify developer account.
     */
    public static final String REDIRECT_URI = "test1-musicplayer-login://callback/";
    public static final String CLIENT_ID = "90aed54790a74dee92a61da9424b3ca3";
    public static final int REQUEST_CODE = 1337;

    public static String Access_Token;
    public static String partyPlaylistID;

    /**
     * ??
     */
    public static String[] partyPlaylistTracks = new String[40];
}
