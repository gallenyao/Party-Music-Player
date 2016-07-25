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

import musicplayer.party.personalization.playlistUpdate.RecommendationTask;
import musicplayer.party.helper.CustomJSONObjectRequest;
import musicplayer.party.helper.CustomVolleyRequestQueue;
import musicplayer.party.helper.PersonalizationConstant;
import musicplayer.party.spotifyService.UserProfile;

/**
 * Created by YLTL on 7/12/16.
 */
public class UpdateArtistParaTask extends AsyncTask<Void, Integer, Void> implements Response.ErrorListener, Response.Listener<JSONObject> {

    /**
     * Request queue that will be used to send request to Spotify.
     */
    private RequestQueue mQueue;
    private int numberOfArtists;
    private static final String REQUEST_TAG = "UpdateArtistParametersService";
    private Context mContext;


    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Void doInBackground(Void... params) {
        mQueue = CustomVolleyRequestQueue.getInstance(this.mContext)
                .getRequestQueue();

        /**
         * Spotify web API url to be called to retrieve metadata about user preferred artist
         */
        String url = "https://api.spotify.com/v1/artists/?ids=";

        /**
         * Traversing the guestArtistsPreferences array and append the artist IDs at the end of url to retrieve their metadata
         */
//        for(int i = 0; i < UserProfile.guestArtistsPreferences.length; i++) {
//            if (UserProfile.guestArtistsPreferences[i] != null) {
//                url = url + UserProfile.guestArtistsPreferences[i] + ",";
//                numberOfArtists++;
//            }
//        }




        Log.e("UpdateArtisParaTask-1", String.valueOf(UserProfile.userCounter));
        for (int i = 0; i < UserProfile.artistsPreferences.get(UserProfile.userCounter).length; i++) {
            if (UserProfile.artistsPreferences.get(UserProfile.userCounter)[i] != null) {
                Log.e(" * * * * * ", UserProfile.artistsPreferences.get(UserProfile.userCounter)[i]);
                url = url + UserProfile.artistsPreferences.get(UserProfile.userCounter)[i] + ",";
                numberOfArtists++;
            }
        }
        Log.e("UpdateArtisParaTask-2", String.valueOf(UserProfile.userCounter));
        Log.e("UpdateArtistParaTask", url);





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


        JSONObject jsonresponse = (JSONObject)response; //Store the JSON response retrieved from Spotify web API
        try {

            JSONArray artists = jsonresponse.getJSONArray("artists");
            /**
             * Traverse the JSONArray artists to retrieve the metadata about artists
             */
            for(int i = 0; i < numberOfArtists;i++){
                PersonalizationConstant.popularity += Integer.parseInt(artists.getJSONObject(i).getString("popularity"));
            }
            PersonalizationConstant.popularity = PersonalizationConstant.popularity/numberOfArtists;

            /**
             * If #artists>2 start FilterArtistService to reduce #artists based on new popularity parameter calculated above
             * Else add artists to artistsIDs array for personalization and start RecommendationService
             */

            if(numberOfArtists > 2){

                new FilterArtistTask().execute();
                Log.e("Start FilterArtists","UpdateArtists -> FilterArtists");
            }
            else{
                for(int i = 0; i < numberOfArtists; i++)
                    PersonalizationConstant.artistIDs.add(i, UserProfile.artistsPreferences.get(UserProfile.userCounter)[i]);

                new RecommendationTask().execute();
                Log.e("start Recommendation ", "UpdateArtists -> Recommendation");
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


}
