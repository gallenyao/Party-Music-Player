package musicplayer.party.Personalization;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import musicplayer.party.CustomJSONObjectRequest;
import musicplayer.party.Helper.CustomVolleyRequestQueue;
import musicplayer.party.PartyHome;
import musicplayer.party.SpotifyService.UserProfile;

/**
 * Created by YLTL on 6/18/16.
 */
public class FilterPreferencesService extends Service implements Response.ErrorListener, Response.Listener<JSONObject> {
    private RequestQueue mQueue;
    private int numberOfTracks;
    private static final String REQUEST_TAG = "FilterPreferencesService";

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("999","akjdlawjnklvaw");
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
        JSONObject jsonresponse = (JSONObject)response;
        try {
            JSONArray audio_features = jsonresponse.getJSONArray("audio_features");
            for(int i=0;i<numberOfTracks;i++){
                String energy = audio_features.getJSONObject(i).getString("energy");
                Log.e("backg11122333",energy);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
