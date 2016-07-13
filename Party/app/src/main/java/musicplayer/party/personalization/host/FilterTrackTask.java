package musicplayer.party.personalization.host;

import android.content.Context;
import android.os.AsyncTask;
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
 * Created by YLTL on 7/12/16.
 */
public class FilterTrackTask extends AsyncTask<Void, Integer, Void> implements Response.ErrorListener, Response.Listener<JSONObject> {

    /**
     * Request queue that will be used to send request to Spotify.
     */
    private RequestQueue mQueue;
    private int numberOfTracks;
    private static final String REQUEST_TAG = "FilterTrackPreferencesService";
    private Context mContext;

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Void doInBackground(Void... params) {
        mQueue = CustomVolleyRequestQueue.getInstance(this.mContext)
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

        url = url.substring(0, url.length()-1); // appending the extra ',' at the end of url

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

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
    }

    @Override
    protected void onPostExecute(Void result) {

    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {

    /**
     * Store the  energy, danceability, valence, instrumentalness metadata about each track retrieved from Spotify response
     */
        double energy, danceability, valence, instrumentalness;
        JSONObject jsonresponse = (JSONObject)response; //Store the JSON response retrieved from Spotify web API

        try {
            JSONArray audio_features = jsonresponse.getJSONArray("audio_features");
            /**
             * Traverse the JSONArray audio_features to retrieve the metadata about tracks
             */
            for(int i = 0; i < numberOfTracks; i++){
                energy = Double.parseDouble(audio_features.getJSONObject(i).getString("energy"));
                danceability = Double.parseDouble(audio_features.getJSONObject(i).getString("danceability"));
                valence = Double.parseDouble(audio_features.getJSONObject(i).getString("valence"));
                instrumentalness = Double.parseDouble(audio_features.getJSONObject(i).getString("instrumentalness"));

                /**
                 * Determine if the track meets the track parameters specified in PersonalizationConstant class
                 * If a track meets the criteria, add the track to the trackIDS array for personalization
                 */
                if (energy >= PersonalizationConstant.energy && danceability >= PersonalizationConstant.danceability && valence >= PersonalizationConstant.valence && instrumentalness >= PersonalizationConstant.instrumentalness){
                    PersonalizationConstant.trackIDs.add(audio_features.getJSONObject(i).getString("id"));
                    //Log.e("track id size",PersonalizationConstant.trackIDs.size()+"i");
                }
            }

            /**
             * If trackIDs array is empty, add any track in it so that it can be used for personalization
             */
            if(PersonalizationConstant.trackIDs.size()==0){
                PersonalizationConstant.trackIDs.add(UserProfile.guestTracksPreferences[0]);
                //Log.e("track id size",PersonalizationConstant.trackIDs.size()+"i");
            }

            /**
             * If #tracks>3 automatically remove the extra tracks from trackIDS array
             */
            if(PersonalizationConstant.trackIDs.size()>3){
                for(int i= PersonalizationConstant.trackIDs.size()-1;i >2;i--)
                    PersonalizationConstant.trackIDs.remove(i);
            }

            new UpdateArtistParaTask().execute();
            Log.e("UpdateArtistParameter","FilterTrack -> UpdateArtistParameter");


        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.e("StopService","Stop FilterTrackService");
    }

}
