package musicplayer.party;

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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Recommendation extends ActionBarActivity implements Response.Listener,
        Response.ErrorListener {


    public static final String REQUEST_TAG = "Recommendation";
    private RequestQueue mQueue;
    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recommendation);
        mTextView = (TextView) findViewById(R.id.error);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
        String url = "https://api.spotify.com/v1/recommendations?min_popularity=50&min_energy=0.4&market=US&seed_artists="; // Spotify web API url to be called to retrieve guest_artists_preferences

        for(int i=0;i<2;i++)
            url = url + Variable.guest_artists_preferences[i]+ ",";

        url = url.substring(0,url.length()-1);
        url = url + "&seed_tracks=";

        for(int i=0;i<2;i++)
            url = url + Variable.guest_tracks_preferences[i]+ ",";

        url = url.substring(0,url.length()-1);
        Log.e("url1234455",url);
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
        int y=0; // flag that is set if the user has some guest_artists_preferences

        try {
            JSONArray items = jsonresponse.getJSONArray("tracks");
            if (items.length()==0)
                y=1;
            else
            {
                for(int i=0;i< Variable.party_playlist_tracks.length;i++) {
                    if (Variable.party_playlist_tracks[i] != null) {
                        Variable.playlist_length++;

                    }
                }
                Log.e("#t",Variable.playlist_length + "jfdj");
                for (int i = 0; i < items.length(); i++) {
                    Variable.party_playlist_tracks[i+Variable.playlist_length]= items.getJSONObject(i).getString("uri");
                    Log.e("12345i", Variable.party_playlist_tracks[i+Variable.playlist_length]);
                    mTextView.setText(Variable.party_playlist_tracks[i+Variable.playlist_length]);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void next(View view) {
        Intent intent = new Intent(this, CreatePlaylist.class);
        startActivity(intent);
    }

}
