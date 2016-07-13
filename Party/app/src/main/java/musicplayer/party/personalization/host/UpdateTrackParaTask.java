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
public class UpdateTrackParaTask extends AsyncTask<Void, Integer, Void> implements Response.ErrorListener, Response.Listener<JSONObject> {

    /**
     * Request queue that will be used to send request to Spotify.
     */
    private RequestQueue mQueue;
    private int numberOfTracks;
    private static final String REQUEST_TAG = "UpdateTrackParametersService";
    private Context mContext;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {
            mQueue = CustomVolleyRequestQueue.getInstance(this.mContext)
                    .getRequestQueue();
            /**
             * Spotify web API url to be called to retrieve metadata about user preferred tracks
             */
            String url = "https://api.spotify.com/v1/audio-features/?ids=";

            /**
             * Traversing the guestTracksPreferences array and append the track IDs at the end of url to retrieve their metadata
             */
            for (int i = 0; i < UserProfile.guestTracksPreferences.length; i++) {
                if (UserProfile.guestTracksPreferences[i] != null) {
                    url = url + UserProfile.guestTracksPreferences[i] + ",";
                    /**
                     * Counting the number of tracks in guestTracksPreferences array
                     */
                    numberOfTracks++;
                }
            }
            /**
             * Appending the extra ',' at the end of url
             */
            url = url.substring(0, url.length()-1);

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


        JSONObject jsonresponse = (JSONObject)response;//Store the JSON response retrieved from Spotify web API
        try {
            JSONArray audio_features = jsonresponse.getJSONArray("audio_features");
            /**
             * Traverse the JSONArray audio_features to retrieve the metadata about tracks
             */
            for(int i = 0; i < numberOfTracks; i++){
                PersonalizationConstant.energy += Double.parseDouble(audio_features.getJSONObject(i).getString("energy"));
                PersonalizationConstant.danceability += Double.parseDouble(audio_features.getJSONObject(i).getString("danceability"));
                PersonalizationConstant.valence += Double.parseDouble(audio_features.getJSONObject(i).getString("valence"));
                PersonalizationConstant.instrumentalness += Double.parseDouble(audio_features.getJSONObject(i).getString("instrumentalness"));
            }

            /**
               Calculating mean of the track personalization parameters
             */
            PersonalizationConstant.danceability= PersonalizationConstant.danceability/numberOfTracks;
            PersonalizationConstant.energy = PersonalizationConstant.energy/numberOfTracks;
            PersonalizationConstant.instrumentalness= PersonalizationConstant.instrumentalness/numberOfTracks;
            PersonalizationConstant.valence = PersonalizationConstant.valence/numberOfTracks;

            /**
                If #tracks>3 starting the FilterTrackService based on new track parameters calculated above
                else add the tracks to PersonalizationConstant.trackIDs array and
                call UpdateArtistParametersService to update artists personalization parameters as well.
             */
            if(numberOfTracks > 3) {
                new FilterTrackTask().execute();
                Log.e("start FilterTrack task", "UpdateTrack -> FilterTrack");
            }
            else{

                for(int i = 0; i < numberOfTracks; i++)
                    PersonalizationConstant.trackIDs.add(i,UserProfile.guestTracksPreferences[i]);

                new UpdateArtistParaTask().execute();
                Log.e("start UpdateArtist task", "UpdateTrack -> UpdateArtists");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Log.e("StopService","Stop UpdateTrack AsynTask!!!");
    }

}
