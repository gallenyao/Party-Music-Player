package musicplayer.party.Personalization.PlaylistUpdate;


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

import musicplayer.party.Helper.CustomJSONObjectRequest;
import musicplayer.party.Helper.CustomVolleyRequestQueue;
import musicplayer.party.Helper.PartyConstant;
import musicplayer.party.Helper.PersonalizationConstant;


/**
 * Created by Nikita Jain on 7/2/2016.
 */
public class RecommedationService extends Service implements Response.ErrorListener, Response.Listener<JSONObject>{

    public static final String REQUEST_TAG = "RecommendationService";
    public static int playlist_length;
    private RequestQueue mQueue;


    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
        String url = "https://api.spotify.com/v1/recommendations?min_popularity=" + PersonalizationConstant.popularity + "&min_energy=" + PersonalizationConstant.energy +
                "&market=US&seed_artists="; // Spotify web API url to be called to retrieve guestTracksPreferences

        for(int i = 0; i< PersonalizationConstant.artistIDs.size(); i++)
            url = url + PersonalizationConstant.artistIDs.get(i)+ ",";

        url = url.substring(0,url.length()-1);
        url = url + "&seed_tracks=";

        for(int i=0;i<PersonalizationConstant.trackIDs.size();i++)
            url = url + PersonalizationConstant.trackIDs.get(i)+ ",";

        url = url.substring(0,url.length()-1);

        /**
         * Create a JSON Request using CustomJSONObject function that takes 4 parameters:-
         * 1. Request method type i.e. a GEt ot a POST request type
         * 2. url to be called
         * 3. create new JSON object for retrieving data
         * 4. some event listeneres
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

        JSONObject jsonresponse = (JSONObject)response; // store the JSONresponse retrieved from Spotify web API
        int y=0; // flag that is set if the user has some guestTracksPreferences

        try {
            JSONArray items = jsonresponse.getJSONArray("tracks");
            if (items.length()==0)
                y=1;
            else
            {
                for(int i = 0; i< PartyConstant.partyPlaylistTracks.size(); i++) {
                    if (PartyConstant.partyPlaylistTracks.get(i) != null) {
                        playlist_length++;

                    }
                }
                Log.e("#t", playlist_length + "jfdj");
                for (int i = 0; i < items.length(); i++) {
                    PartyConstant.partyPlaylistTracks.set(i + playlist_length, items.getJSONObject(i).getString("uri"));

                }

                /**
                 * Calling add tracks service to add tracks to spotify playlist
                 */
                Intent addTracksIntent = new Intent(this, AddTracksService.class);
                startService(addTracksIntent);


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
