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
import musicplayer.party.mediaPlayer.PlayTracks;
import musicplayer.party.spotifyService.UserProfile;

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: AddTracksService
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 * The java class is for adding tracks to Spotify playlist.
 */

public class AddTracksService extends Service implements Response.ErrorListener, Response.Listener<JSONObject> {


    public static final String REQUEST_TAG = "AddTracksService"; //Request queue that will be used to send request to Spotify.
    private RequestQueue mQueue;

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();
        String url = "https://api.spotify.com/v1/users/"+ UserProfile.userID +"/playlists"+ "/" + PartyConstant.partyPlaylistID + "/tracks?uris=" ; // Spotify web API url to be called to add tracks

        // Parsing the partyPlaylistTracks array to add tracks to url
        for (int i=0;i<PartyConstant.partyPlaylistTracks.size();i++)
            url = url + PartyConstant.partyPlaylistTracks.get(i) + ",";
        url = url.substring(0,url.length()-1);

        /**
         * Create a JSON Request using CustomJSONObject function that takes 4 parameters:-
         * 1. Request method type i.e. a GEt ot a POST request type
         * 2. url to be called
         * 3. create new JSON object for retrieving data
         * 4. some event listeneres
         */
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .POST, url,
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

        try {
            String  id = jsonresponse.getString("snapshot_id"); // Check if the tracks were successfully added
            Log.e("last123", id);



        } catch (JSONException e) {
            e.printStackTrace();
        }
        Intent intent = new Intent(this, PlayTracks.class);
        intent.setFlags(intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
