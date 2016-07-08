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
import musicplayer.party.personalization.playlistUpdate.RecommedationService;
import musicplayer.party.spotifyService.UserProfile;

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: FilterArtistPerefrencesService
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 * The java class is for filtering the user's artists preferences based on personalization parameter.
 */
public class FilterArtistPreferenceService extends Service implements Response.ErrorListener, Response.Listener<JSONObject> {

    private RequestQueue mQueue; //Request queue that will be used to send request to Spotify.
    private int numberOfArtists;
    private static final String REQUEST_TAG = "FilterArtistPreferencesService";

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext()).getRequestQueue();

        String url = "https://api.spotify.com/v1/artists/?ids="; // Spotify web API url to be called to retrieve metadata about user preferred artist

        /**
         * Traversing the guestArtistsPreferences array and append the artist IDs at the end of url to retrieve their metadata
         */
        for(int i=0; i<UserProfile.guestArtistsPreferences.length;i++){
            if(UserProfile.guestArtistsPreferences[i]!=null){
                url = url + UserProfile.guestArtistsPreferences[i]+",";
                numberOfArtists++;
                Log.e("numofart","i"+numberOfArtists);
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
        Log.e("DestroyService","Destroy FilterArtistsService");
    }

    @Override
    public void onResponse(JSONObject response) {

        int count =0,popularity; // stores the popularity metadata about each artist retrieved from Spotify response
        JSONObject jsonresponse = (JSONObject)response; //Store the JSON response retrieved from Spotify web API

        try {

            JSONArray artists = jsonresponse.getJSONArray("artists");
            /**
             * Traverse the JSONArray artists to retrieve the metadata about artists
             */
            for(int i=0;i<numberOfArtists;i++){
                popularity = Integer.parseInt(artists.getJSONObject(i).getString("popularity"));
                /**
                 * Determine if the artist meets specified popularity in PersonalizationConstant class
                 * If an artist meets the criteria, add the artist to the artistIds array that will be used for personalization
                 */
                if (popularity >= PersonalizationConstant.popularity) {
                    PersonalizationConstant.artistIDs.add(artists.getJSONObject(i).getString("id"));
                    //Log.e("artist id size", PersonalizationConstant.artistIDs.size()+"i");
                 }
            }
            //
            if(PersonalizationConstant.artistIDs.size()==0){
                PersonalizationConstant.artistIDs.add(UserProfile.guestArtistsPreferences[0]);
                //Log.e("artist id size", PersonalizationConstant.artistIDs.size()+"i");
            }

            //If #artists>2, automatically remove the extra artists from artistIDs array
             if(PersonalizationConstant.artistIDs.size()>2){
                for(int i= PersonalizationConstant.artistIDs.size()-1; i>1; i--)
                    PersonalizationConstant.artistIDs.remove(i);
            }

            // Start RecommendationService
            Intent recommnedationIntent = new Intent(this, RecommedationService.class);
            startService(recommnedationIntent);
            Log.e("start recommendation ", "FilterArtists -> Recommendation");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        stopSelf();
        Log.e("StopService","Stop FilterArtistsService");
    }
}
