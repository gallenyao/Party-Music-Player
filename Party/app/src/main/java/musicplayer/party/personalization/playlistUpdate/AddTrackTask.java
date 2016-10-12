package musicplayer.party.personalization.playlistUpdate;

import android.content.Context;
import android.content.Intent;
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
import musicplayer.party.helper.PersonalizationConstant;
import musicplayer.party.mediaPlayer.PlayTracksActivity;
import musicplayer.party.spotifyService.InstructionActivity;
import musicplayer.party.spotifyService.UserProfile;

/**
 * Created by YLTL on 7/12/16.
 */
public class AddTrackTask extends AsyncTask<Void, Integer, Void> implements Response.ErrorListener, Response.Listener<JSONObject> {

    /**
     * Request queue that will be used to send request to Spotify.
     */
    public static final String REQUEST_TAG = "AddTracksService";
    private RequestQueue mQueue;
    private Context mContext;

    public AddTrackTask() {
        this.mContext = PersonalizationConstant.context;
    }


    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Void doInBackground(Void... params) {
        mQueue = CustomVolleyRequestQueue.getInstance(this.mContext).getRequestQueue();
        String url = "https://api.spotify.com/v1/users/"+ UserProfile.userID +"/playlists"+ "/" + PartyConstant.partyPlaylistID + "/tracks?uris=" ; // Spotify web API url to be called to add tracks

        //String url = "https://api.spotify.com/v1/users/"+ " ad " +"/playlists"+ "/" + PartyConstant.partyPlaylistID + "/tracks?uris=" ;
        /**
         * Parsing the partyPlaylistTracks array to add tracks to url
         */
        for (int i = 0; i < PartyConstant.partyPlaylistTracks.size(); i++)
            url = url + PartyConstant.partyPlaylistTracks.get(i) + ",";
        url = url.substring(0, url.length()-1);

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

        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
    }

    @Override
    protected void onPostExecute(Void result) {

        //Log.e("start time: ", String.valueOf(PersonalizationConstant.startTime));
        PersonalizationConstant.endTime = System.currentTimeMillis();
        Log.e("Personalization end: ", String.valueOf(PersonalizationConstant.endTime));

        long duration = PersonalizationConstant.endTime - PersonalizationConstant.startTime;
        Log.e("duration", String.valueOf(duration) + "ms");

        Intent intent_name = new Intent(mContext, PlayTracksActivity.class);
        intent_name.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent_name);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(JSONObject response) {


        JSONObject jsonresponse = (JSONObject)response; // store the JSONresponse retrieved from Spotify web API

        try {
            String id = jsonresponse.getString("snapshot_id"); // Check if the tracks were successfully added
            Log.e("addTrack_ID", id);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
