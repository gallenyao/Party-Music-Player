package musicplayer.party.Personalization.Host;

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
 * Name: UpdateTrackParamteresService
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 * The java class is for updating the track's personalization parameter.
 */
public class UpdateTrackParametersService extends Service implements Response.ErrorListener, Response.Listener<JSONObject> {
    /**
     * Request queue that will be used to send request to Spotify.
     */
    private RequestQueue mQueue;
    private int numberOfTracks;
    private static final String REQUEST_TAG = "UpdateTrackParametersService";

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
        double sum_energy = 0, sum_danceability = 0, sum_valence = 0, sum_instrumentalness = 0;
        double mean_energy=0, mean_danceability=0, mean_valence=0, mean_instrumentalness=0;

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
                sum_energy += Double.parseDouble(audio_features.getJSONObject(i).getString("energy"));
                sum_danceability += Double.parseDouble(audio_features.getJSONObject(i).getString("danceability"));
                sum_valence += Double.parseDouble(audio_features.getJSONObject(i).getString("valence"));
                sum_instrumentalness += Double.parseDouble(audio_features.getJSONObject(i).getString("instrumentalness"));
            }

            mean_danceability = sum_danceability/numberOfTracks;
            mean_energy = sum_energy/numberOfTracks;
            mean_instrumentalness= sum_instrumentalness/numberOfTracks;
            mean_valence = sum_valence/numberOfTracks;

            PersonalizationConstant.danceability = mean_danceability;
            PersonalizationConstant.energy = mean_energy;
            PersonalizationConstant.instrumentalness = mean_instrumentalness;
            PersonalizationConstant.valence = mean_valence;

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
