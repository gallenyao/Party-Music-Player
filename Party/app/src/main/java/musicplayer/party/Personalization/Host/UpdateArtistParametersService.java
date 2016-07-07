package musicplayer.party.Personalization.Host;

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
import musicplayer.party.Helper.PersonalizationConstant;
import musicplayer.party.Personalization.PlaylistUpdate.RecommedationService;
import musicplayer.party.SpotifyService.UserProfile;

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: UpdateArtistParameterService
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 * The java class is for updating the tracks personalization parameter based on mean algorithm approach.
 */
public class UpdateArtistParametersService extends Service implements Response.ErrorListener, Response.Listener<JSONObject> {

    private RequestQueue mQueue; //Request queue that will be used to send request to Spotify.
    private int numberOfArtists;
    private static final String REQUEST_TAG = "UpdateArtistParametersService";

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

        String url = "https://api.spotify.com/v1/artists/?ids="; // Spotify web API url to be called to retrieve metadata about user preferred artist

        /**
         * Traversing the guestArtistsPreferences array and append the artist IDs at the end of url to retrieve their metadata
         */

         for(int i=0; i<UserProfile.guestArtistsPreferences.length;i++) {
             if (UserProfile.guestArtistsPreferences[i] != null) {
                 url = url + UserProfile.guestArtistsPreferences[i] + ",";
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

        JSONObject jsonresponse = (JSONObject)response; //Store the JSON response retrieved from Spotify web API
        try {

            JSONArray artists = jsonresponse.getJSONArray("artists");
            /**
             * Traverse the JSONArray artists to retrieve the metadata about artists
             */
            for(int i=0;i<numberOfArtists;i++){
                PersonalizationConstant.popularity += Integer.parseInt(artists.getJSONObject(i).getString("popularity"));
            }
            PersonalizationConstant.popularity = PersonalizationConstant.popularity/numberOfArtists;

            /*
                If #artists>2 start FilterArtistService to reduce #artists based on new popularity parameter calculated above
                Else add artists to artistsIDs array for personalization and start RecommendationService
             */

            if(numberOfArtists>2){
                Intent filterArtistIntent = new Intent(this, FilterArtistPreferenceService.class);
                startService(filterArtistIntent);
                Log.e("Starting filter","track service");
            }
            else{
                    for(int i=0;i<numberOfArtists;i++)
                      PersonalizationConstant.artistIDs.add(i,UserProfile.guestArtistsPreferences[i]);

                    Intent recommnedationIntent = new Intent(this, RecommedationService.class);
                    startService(recommnedationIntent);
                    Log.e("starting recom ", "service");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
