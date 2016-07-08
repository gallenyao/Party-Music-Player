package musicplayer.party.spotifyService;

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
import musicplayer.party.personalization.playlistUpdate.CreatePlaylistService;

/**
 * Created by Nikita Jain on 7/2/2016.
 */
public class SpotifyRetrieveUserInfoService extends Service implements Response.ErrorListener, Response.Listener<JSONObject> {

    private RequestQueue mQueue;
    private static final String REQUEST_TAG = "RetrieveUserInfoService";

    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();

        String url = "https://api.spotify.com/v1/me"; // Spotify web API url to be called to retrieve metadata about user preferred artist


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

        JSONObject jsonresponse = (JSONObject)response;
        try {

            String  id = jsonresponse.getString("id");
            UserProfile.userID = id;
            Log.e("userid1234",id);

            if(PartyConstant.partyPlaylistID == null) {
                Intent createPlaylistIntent = new Intent(this, CreatePlaylistService.class);
                startService(createPlaylistIntent);
            }


        } catch (JSONException e) {
        e.printStackTrace();
    }

    }
}
