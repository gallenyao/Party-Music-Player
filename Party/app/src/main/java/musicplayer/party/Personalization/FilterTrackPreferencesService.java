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

import musicplayer.party.CustomJSONObjectRequest;
import musicplayer.party.Helper.CustomVolleyRequestQueue;
import musicplayer.party.Helper.PersonalizationConstant;
import musicplayer.party.SpotifyService.UserProfile;

/**
 * Created by YLTL on 6/18/16.
 */
public class FilterTrackPreferencesService extends Service implements Response.ErrorListener, Response.Listener<JSONObject> {
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

        String url = "https://api.spotify.com/v1/audio-features/?ids="; // Spotify web API url to be called to retrieve guestTracksPreferences
        for(int i=0; i<UserProfile.guestTracksPreferences.length;i++){
            if(UserProfile.guestTracksPreferences[i]!=null){
                url = url + UserProfile.guestTracksPreferences[i]+",";
                numberOfTracks++;
            }

        }
        url = url.substring(0,url.length()-1);

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
        double energy, danceability, valence, instrumentalness;
        int count=0;
        JSONObject jsonresponse = (JSONObject)response;
        try {
            JSONArray audio_features = jsonresponse.getJSONArray("audio_features");

            for(int i=0;i<numberOfTracks;i++){
                energy = Double.parseDouble(audio_features.getJSONObject(i).getString("energy"));
                danceability = Double.parseDouble(audio_features.getJSONObject(i).getString("danceability"));
                valence = Double.parseDouble(audio_features.getJSONObject(i).getString("valence"));
                instrumentalness = Double.parseDouble(audio_features.getJSONObject(i).getString("instrumentalness"));

                if (energy >= PersonalizationConstant.energy && danceability >= PersonalizationConstant.danceability && valence >= PersonalizationConstant.valence && instrumentalness >= PersonalizationConstant.instrumentalness){
                    PersonalizationConstant.userPreferredTracks[count]= audio_features.getJSONObject(i).getString("uri");
                    count++ ;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
