package musicplayer.party.Personalization;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONException;
import org.json.JSONObject;

import musicplayer.party.Helper.CustomJSONObjectRequest;
import musicplayer.party.Helper.CustomVolleyRequestQueue;
import musicplayer.party.Helper.PartyConstant;
import musicplayer.party.R;

public class CreatePlaylist extends ActionBarActivity implements Response.Listener,
        Response.ErrorListener {


    public static final String REQUEST_TAG = "CreatePlaylist";
    private RequestQueue mQueue;
    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_playlist);
        mTextView = (TextView) findViewById(R.id.error);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
        String url = "https://api.spotify.com/v1/users/anuragkanungo/playlists" ; // Spotify web API url to be called to retrieve guestTracksPreferences

        JSONObject params = new JSONObject();
        try {
            params.put("name","Party Playlist");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("JSON Object",params.toString());
        /**
         * Create a JSON Request using CustomJSONObject function that takes 4 parameters:-
         * 1. Request method type i.e. a GEt ot a POST request type
         * 2. url to be called
         * 3. create new JSON object for retrieving data
         * 4. some event listeneres
         */
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method
                .POST, url,
                params, this, this);

        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest); // add JSON request to the queue
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    /**
     * If there is an error while retrieving response from Spotify API, diaply it on screen
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        mTextView.setText(error.getMessage());
    }

    @Override
    public void onResponse(Object response) {

        JSONObject jsonresponse = (JSONObject)response; // store the JSONresponse retrieved from Spotify web API
        int y=0; // flag that is set if the user has some guestTracksPreferences

        try {
            String  id = jsonresponse.getString("id");
            PartyConstant.partyPlaylistID = id;
            mTextView.setText(PartyConstant.partyPlaylistID);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void next(View view) {
        Intent intent = new Intent(this, AddTracks.class);
        startActivity(intent);
    }

}
