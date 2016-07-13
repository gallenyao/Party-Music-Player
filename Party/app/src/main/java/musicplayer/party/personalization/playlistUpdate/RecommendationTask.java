package musicplayer.party.personalization.playlistUpdate;

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
import musicplayer.party.helper.PartyConstant;
import musicplayer.party.helper.PersonalizationConstant;

/**
 * Created by YLTL on 7/12/16.
 */
public class RecommendationTask extends AsyncTask<Void, Integer, Void> implements Response.ErrorListener, Response.Listener<JSONObject> {

    public static final String REQUEST_TAG = "RecommendationService";
    private RequestQueue mQueue; //Request queue that will be used to send request to Spotify.
    private Context mContext;

    @Override
    protected void onPreExecute() {


    }

    @Override
    protected Void doInBackground(Void... params) {

        mQueue = CustomVolleyRequestQueue.getInstance(this.mContext).getRequestQueue();

        /**
         * Spotify web API url to be called to Spotify's recommendation API
         */
        String url = "https://api.spotify.com/v1/recommendations?min_popularity=" + PersonalizationConstant.popularity + "&min_energy=" + PersonalizationConstant.energy +
                "&market=US&seed_artists=";

        /**
         * Adding artists to recommendation url
         */
        for(int i = 0; i< PersonalizationConstant.artistIDs.size(); i++)
            url = url + PersonalizationConstant.artistIDs.get(i)+ ",";

        url = url.substring(0,url.length()-1);
        url = url + "&seed_tracks=";

        /**
         * Adding tracks to recommendation url.
         */
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
         * Store the JSONresponse retrieved from Spotify web API
         */
        JSONObject jsonresponse = (JSONObject)response;

        try {
            JSONArray items = jsonresponse.getJSONArray("tracks");

            /**
             * If no tracks are recommended then do nothing else traverse the response and add the tracks to partyPlaylistTracks array
             */
            if (items.length() == 0)
                Log.e("recom tracks","0");
            else
            {
                int len = PartyConstant.partyPlaylistTracks.size();
                for (int i = 0; i < items.length(); i++) {
                    Log.e("print item",items.getJSONObject(i).getString("uri"));
                    Log.e("print track array",PartyConstant.partyPlaylistTracks.size()+"i");
                    PartyConstant.partyPlaylistTracks.add(len+ i,items.getJSONObject(i).getString("uri"));
                }

                new AddTrackTask().execute();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
