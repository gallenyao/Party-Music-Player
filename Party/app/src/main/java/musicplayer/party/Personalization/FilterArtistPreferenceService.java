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
public class FilterArtistPreferenceService extends Service implements Response.ErrorListener, Response.Listener<JSONObject> {
    private RequestQueue mQueue;
    private int numberOfArtists;
    private static final String REQUEST_TAG = "FilterArtistPreferencesService";

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

        String url = "https://api.spotify.com/v1/artists/?ids="; // Spotify web API url to be called to retrieve guestTracksPreferences
        for(int i=0; i<UserProfile.guestArtistsPreferences.length;i++){
            if(UserProfile.guestArtistsPreferences[i]!=null){
                url = url + UserProfile.guestArtistsPreferences[i]+",";
                numberOfArtists++;
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

        int count=0,popularity;
        JSONObject jsonresponse = (JSONObject)response;
        try {
            JSONArray artists = jsonresponse.getJSONArray("artists");

            for(int i=0;i<numberOfArtists;i++){
                popularity = Integer.parseInt(artists.getJSONObject(i).getString("popularity"));
                if (popularity >= PersonalizationConstant.popularity) {
                    PersonalizationConstant.userPreferredArtists[count]= artists.getJSONObject(i).getString("uri");
                    count++ ;
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}