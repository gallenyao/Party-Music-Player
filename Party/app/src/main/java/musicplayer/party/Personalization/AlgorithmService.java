package musicplayer.party.Personalization;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import musicplayer.party.Helper.CustomJSONObjectRequest;
import musicplayer.party.Helper.CustomVolleyRequestQueue;
import musicplayer.party.Helper.PersonalizationConstant;
import musicplayer.party.SpotifyService.UserProfile;

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: FilterTrackPerefrencesService
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 * The java class is for filtering the user's tracks preferences based on personalization parameter.
 */
public class AlgorithmService extends Service implements Response.ErrorListener, Response.Listener<JSONObject> {
    /**
     * Request queue that will be used to send request to Spotify.
     */
    private RequestQueue mQueue;
    private int numberOfTracks;
    private static final String REQUEST_TAG = "FilterTrackPreferencesService";

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

        String url = "https://api.spotify.com/v1/audio-features/?ids="; // Spotify web API url to be called to retrieve metadata about user preferred tracks

        /**
         * Traversing the guestTracksPreferences array and append the track IDs at the end of url to retrieve their metadata
         */
        for(int i=0; i<UserProfile.guestTracksPreferences.length;i++){
            if(UserProfile.guestTracksPreferences[i]!=null){
                url = url + UserProfile.guestTracksPreferences[i]+",";
                numberOfTracks++; // counting the number of tracks in guestTracksPreferences array
            }

        }
        url = url.substring(0,url.length()-1); // appending the extra ',' at the end of url

        /**
         * Create a JSON Request using CustomJSONObject function that takes 4 parameters:-
         * 1. Request method type selectedPrefCount.e. a GEt ot a POST request type
         * 2. url to be called
         * 3. create new JSON object for retrieving data
         * 4. some event listeners
         */
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .GET, url,
                new JSONObject(), this, this);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest); // add JSON request to the queue

        return START_STICKY;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onResponse(JSONObject response) {
        /**
         * Store the  energy, danceability, valence, instrumentalness metadata about each track retrieved from Spotify response
         */
        double energy, danceability, valence, instrumentalness;
        int count=0;
        /**
         * Store the JSON response retrieved from Spotify web API
         */
        JSONObject jsonresponse = (JSONObject)response;
        try {
            JSONArray audio_features = jsonresponse.getJSONArray("audio_features");
            /**
             * Traverse the JSONArray audio_features to retrieve the metadata about tracks
             */
            for(int i=0;i<numberOfTracks;i++){
                energy = Double.parseDouble(audio_features.getJSONObject(i).getString("energy"));
                danceability = Double.parseDouble(audio_features.getJSONObject(i).getString("danceability"));
                valence = Double.parseDouble(audio_features.getJSONObject(i).getString("valence"));
                instrumentalness = Double.parseDouble(audio_features.getJSONObject(i).getString("instrumentalness"));

                /**
                 * Determine if the track meets the track parameters specified in PersonalizationConstant class
                 * If a track meets the criteria, add the track to the userFilteredPreferredTracks which will be sent to Host and used for personalization
                 */
                if (energy >= PersonalizationConstant.energy && danceability >= PersonalizationConstant.danceability && valence >= PersonalizationConstant.valence && instrumentalness >= PersonalizationConstant.instrumentalness){
                    UserProfile.userFilteredPreferredTracks[count]= audio_features.getJSONObject(i).getString("uri");
                    count++ ;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
