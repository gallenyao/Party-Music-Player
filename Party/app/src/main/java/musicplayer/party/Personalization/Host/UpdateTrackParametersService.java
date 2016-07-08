package musicplayer.party.personalization.host;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import musicplayer.party.helper.CustomJSONObjectRequest;
import musicplayer.party.helper.CustomVolleyRequestQueue;
import musicplayer.party.helper.PersonalizationConstant;
import musicplayer.party.spotifyService.UserProfile;

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: UpdateTrackParamteresService
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 * The java class is for updating the track's personalization parameter based on mean algorithm.
 */
public class UpdateTrackParametersService extends Service implements Response.ErrorListener, Response.Listener<JSONObject> {

    private RequestQueue mQueue; //Request queue that will be used to send request to Spotify.
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

         for (int i = 0; i < UserProfile.guestTracksPreferences.length; i++) {
             if (UserProfile.guestTracksPreferences[i] != null) {
                 url = url + UserProfile.guestTracksPreferences[i] + ",";
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
        Log.e("DestroyService","Destroy UpdateTrackService");
    }


    @Override
    public void onResponse(JSONObject response) {

        JSONObject jsonresponse = (JSONObject)response;//Store the JSON response retrieved from Spotify web API
        try {
            JSONArray audio_features = jsonresponse.getJSONArray("audio_features");
            /**
             * Traverse the JSONArray audio_features to retrieve the metadata about tracks
             */
            for(int i=0;i<numberOfTracks;i++){
                PersonalizationConstant.energy += Double.parseDouble(audio_features.getJSONObject(i).getString("energy"));
                PersonalizationConstant.danceability += Double.parseDouble(audio_features.getJSONObject(i).getString("danceability"));
                PersonalizationConstant.valence += Double.parseDouble(audio_features.getJSONObject(i).getString("valence"));
                PersonalizationConstant.instrumentalness += Double.parseDouble(audio_features.getJSONObject(i).getString("instrumentalness"));
            }

            /*
               Calculating mean of the track personalization parameters
             */
            PersonalizationConstant.danceability= PersonalizationConstant.danceability/numberOfTracks;
            PersonalizationConstant.energy = PersonalizationConstant.energy/numberOfTracks;
            PersonalizationConstant.instrumentalness= PersonalizationConstant.instrumentalness/numberOfTracks;
            PersonalizationConstant.valence = PersonalizationConstant.valence/numberOfTracks;

            /*
                If #tracks>3 starting the FilterTrackService based on new track parameters calculated above
                else add the tracks to PersonalizationConstant.trackIDs array and
                call UpdateArtistParametersService to update artists personalization parameters as well.
             */
            if(numberOfTracks>3) {
                Intent filterTrackIntent = new Intent(this, FilterTrackPreferencesService.class);
                startService(filterTrackIntent);
                Log.e("start filter track", "UpdateTrack - filterTrack");
            }
            else{

                for(int i=0;i<numberOfTracks;i++)
                    PersonalizationConstant.trackIDs.add(i,UserProfile.guestTracksPreferences[i]);

                Intent updateArtistParametersIntent = new Intent(this, UpdateArtistParametersService.class);
                startService(updateArtistParametersIntent);
                Log.e("start updateArtists", "UpdateTrack -> UpdateArtists");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        stopSelf();
        Log.e("StopService","Stop UpdateTrackService");
    }
}
