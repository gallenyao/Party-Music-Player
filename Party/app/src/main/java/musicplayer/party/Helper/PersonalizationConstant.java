package musicplayer.party.Helper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YLTL on 6/18/16.
 */
public class PersonalizationConstant {

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

    /**
     * String array to store track IDs for Recommendation API.
     */
    public static List<String> trackIDs = new ArrayList<String>();
    /**
     * String array to store artist IDs for Recommendation API.
     */
    public static List<String> artistIDs = new ArrayList<String>();


}
