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
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: RecommendationService
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 * The java class is for retrieving personalizaed tracks.
 */
public class RecommedationService extends Service implements Response.ErrorListener, Response.Listener<JSONObject>{

    public static final String REQUEST_TAG = "RecommendationService";
   private RequestQueue mQueue; //Request queue that will be used to send request to Spotify.

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        String url = "https://api.spotify.com/v1/recommendations?min_popularity=" + PersonalizationConstant.popularity + "&min_energy=" + PersonalizationConstant.energy +
                "&market=US&seed_artists="; // Spotify web API url to be called to Spotify's recommendation API

        // Adding artists to recommendation url
        for(int i = 0; i< PersonalizationConstant.artistIDs.size(); i++)
            url = url + PersonalizationConstant.artistIDs.get(i)+ ",";

        url = url.substring(0,url.length()-1);
        url = url + "&seed_tracks=";

        // Adding tracks to recommendation url
        for(int i=0;i<PersonalizationConstant.trackIDs.size();i++)
            url = url + PersonalizationConstant.trackIDs.get(i) + ",";

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
        int y=0;

        try {
            JSONArray items = jsonresponse.getJSONArray("tracks");

            /*
                If no tracks are recommended then do nothing else traverse the response and add the tracks to partyPlaylistTracks array
             */
            if (items.length()==0)
                y=1;
            else
            {
                int len = PartyConstant.partyPlaylistTracks.size();
                for (int i = 0; i < items.length(); i++) {
                    Log.e("print item",items.getJSONObject(i).getString("uri"));
                    Log.e("print track array",PartyConstant.partyPlaylistTracks.size()+"i");
                    PartyConstant.partyPlaylistTracks.add(len+ i,items.getJSONObject(i).getString("uri"));
                }

                //Calling add tracks service to add tracks to spotify playlist
                Intent addTracksIntent = new Intent(this, AddTracksService.class);
                startService(addTracksIntent);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
