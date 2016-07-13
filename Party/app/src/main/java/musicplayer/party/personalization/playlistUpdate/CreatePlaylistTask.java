package musicplayer.party.personalization.playlistUpdate;

import android.content.Context;
import android.os.AsyncTask;
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
 * Created by YLTL on 7/13/16.
 */
public class CreatePlaylistTask extends AsyncTask<Void, Integer, Void> implements Response.ErrorListener, Response.Listener<JSONObject> {

    public static final String REQUEST_TAG = "CreatePlaylistService";
    /**
     * Request queue that will be used to send request to Spotify.
     */
    private RequestQueue mQueue;
    private Context mContext;


    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Void doInBackground(Void... params) {

        mQueue = CustomVolleyRequestQueue.getInstance(this.mContext).getRequestQueue();
        /**
         * Spotify web API url to be called create playlist
         */
        String url = "https://api.spotify.com/v1/users/" + UserProfile.userID + "/playlists" ;

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("name","Party Playlist");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JSON Object",jsonObject.toString());
        /**
         * Create a JSON Request using CustomJSONObject function that takes 4 parameters:-
         * 1. Request method type i.e. a GEt ot a POST request type
         * 2. url to be called
         * 3. create new JSON object for retrieving data
         * 4. some event listeneres
         */
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .POST, url,
                jsonObject, this, this);

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
        JSONObject jsonresponse = (JSONObject)response; // store the JSONresponse retrieved from Spotify web API

        try {
            PartyConstant.partyPlaylistID = jsonresponse.getString("id"); // store the playlist ID
            Log.e("playlist created","i");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
