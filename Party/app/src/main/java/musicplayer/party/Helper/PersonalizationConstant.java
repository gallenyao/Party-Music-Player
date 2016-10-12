package musicplayer.party.helper;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by YLTL on 6/18/16.
 */
public class PersonalizationConstant {

    /**
     * Variable to state the danceability personalization parameter for track.
     */
    public static double danceability;
    /**
     * Variable to state the energy personalization parameter for track.
     */
    public static double energy;
    /**
     * Variable to state the instrumentalness personalization parameter for track.
     */
    public static double instrumentalness;
    /**
     * Variable to state the valence personalization parameter for track.
     */
    public static double valence;
    /**
     * Variable to state the popularity personalization parameter for artist.
     */
    public static int popularity;

    /**
     * String array to store track IDs for Recommendation API.
     */
    public static List<String> trackIDs = new ArrayList<String>();
    /**
     * String array to store artist IDs for Recommendation API.
     */
    public static List<String> artistIDs = new ArrayList<String>();

    public static long startTime;
    public static long endTime;



    public static Context context = null;



}
