package musicplayer.party.personalization.playlistUpdate;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import musicplayer.party.helper.CustomJSONObjectRequest;
import musicplayer.party.helper.CustomVolleyRequestQueue;
import musicplayer.party.helper.PartyConstant;
import musicplayer.party.spotifyService.UserProfile;

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: CreatePlaylistService
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 * The java class is for creating a playlist for party.
 */

public class CreatePlaylistService extends Service implements Response.ErrorListener, Response.Listener<JSONObject> {


    public static final String REQUEST_TAG = "CreatePlaylistService";
    private RequestQueue mQueue; //Request queue that will be used to send request to Spotify.

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = "https://api.spotify.com/v1/users/" + UserProfile.userID + "/playlists" ; // Spotify web API url to be called create playlist

        JSONObject params = new JSONObject();
        try {
            params.put("name","Party Playlist");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JSON Object",params.toString());
        /**
         * Create a JSON Request using CustomJSONObject function that takes 4 parameters:-
         * 1. Request method type i.e. a GEt ot a POST request type
         * 2. url to be called
         * 3. create new JSON object for retrieving data
         * 4. some event listeneres
         */
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .POST, url,
                params, this, this);

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

        try {
            PartyConstant.partyPlaylistID = jsonresponse.getString("id"); // store the playlist ID

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
