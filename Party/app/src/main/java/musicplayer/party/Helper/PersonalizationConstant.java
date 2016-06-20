package musicplayer.party.Helper;

/**
 * Created by YLTL on 6/18/16.
 */
public class PersonalizationConstant {
    /**
     * Variable to state the length of the user filtered preference array.
     */
    private static final int maxPref = 5;
    /**
     * A string array to store user filtered preferred top artists that will be used in personalization.
     */
    public static String[] userPreferredTracks = new String[maxPref];
    /**
     * A string array to store user filtered preferred top artists that will be used in personalization.
     */
    public static String[] userPreferredArtists = new String[maxPref];
    /**
     * Variable to state the danceability personalization parameter for track.
     */
    public static double danceability = 0.8;
    /**
     * Variable to state the energy personalization parameter for track.
     */
    public static double energy = 0.75;
    /**
     * Variable to state the instrumentalness personalization parameter for track.
     */
    public static double instrumentalness = 0.7;
    /**
     * Variable to state the valence personalization parameter for track.
     */
    public static double valence = 0.8;
    /**
     * Variable to state the popularity personalization parameter for artist.
     */
    public static int popularity = 80;

}
