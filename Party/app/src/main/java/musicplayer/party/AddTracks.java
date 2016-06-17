package musicplayer.party;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class AddTracks extends ActionBarActivity implements Response.Listener,
        Response.ErrorListener {


    public static final String REQUEST_TAG = "CreatePlaylist";
    private RequestQueue mQueue;
    private TextView mTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tracks);
        mTextView = (TextView) findViewById(R.id.error);

    }

    @Override
    protected void onStart() {
        super.onStart();

        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
        String url = "https://api.spotify.com/v1/users/anuragkanungo/playlists"+ "/" + Variable.party_playlist_id + "/tracks?uris=" ; // Spotify web API url to be called to retrieve guest_artists_preferences

        for (int i=0;i<10;i++)
            url = url + Variable.party_playlist_tracks[i] + ",";
        url = url.substring(0,url.length()-1);

   //     String url = "https://api.spotify.com/v1/users/anuragkanungo/playlists/6ZP3uENS7GfvTzb7vR9xFs/tracks?uris=spotify%3Atrack%3A1ruNslbgoiZk8e1vP0lY6j,spotify%3Atrack%3A3KgIZWuC7JJOgkcGeAWbZg,spotify%3Atrack%3A487OPlneJNni3NWC8SYqhW,spotify%3Atrack%3A0TkwJ7TLLnnckRL3PkdPsU,spotify%3Atrack%3A3u1Er1rkjn1oSz1xdZH3ZD,spotify%3Atrack%3A7i9763l5SSfOnqZ35VOcfy,spotify%3Atrack%3A6yuDA4JVS99KXSgjfniAnR,spotify%3Atrack%3A2Q4OrJV7rGtUpPsLGbchgr,spotify%3Atrack%3A3Oiauiojgokw9vWQvFmEoI,spotify%3Atrack%3A2U8g9wVcUu9wsg6i7sFSv8";

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
            String  id = jsonresponse.getString("snapshot_id");
            Log.e("hello","test");
            Log.e("last123", id);
            mTextView.setText(id);

            Log.e("last123", id);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

}
