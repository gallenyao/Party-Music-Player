package musicplayer.party.SpotifyService;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import android.support.v7.app.ActionBarActivity;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import musicplayer.party.CustomJSONObjectRequest;
import musicplayer.party.Helper.CustomVolleyRequestQueue;
import musicplayer.party.PartyHome;
import musicplayer.party.R;

public class SpotifyRetrieveTracks extends ActionBarActivity implements Response.Listener,
        Response.ErrorListener {
    /**
     * Set the variables for different UI components declared in activity_spotify_retrieve_artists file.
     */
    private static final String REQUEST_TAG = "SpotifyRetrieveTracks";
    private static final int PREFERENCE_LENGTH = 5;
    private static final int CHECKBOX_NUMBER = 5;
    public static String[] guestTracksPreferences = new String[PREFERENCE_LENGTH]; // array to store guestTracksPreferences chosen by user for personalization
    private TextView mTextView;
    private Button mButton;
    private CheckBox[] checkBoxes = new CheckBox[CHECKBOX_NUMBER];
    private RequestQueue mQueue;
    private String[] topTracksName = new String[PREFERENCE_LENGTH]; // array to store top artists retrieved from Spotify web aPI
    private String[] topTracksID = new String[PREFERENCE_LENGTH];
    private int selectedPrefCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_retrieve_tracks);

        /**
         * Set text to display that the activity will show guestTracksPreferences of user.
         * Set checkboxes that will be used to display top guestTracksPreferences of user.
         * Set button that will be clicked by user when he has done selecting guestTracksPreferences.
         */
        mTextView = (TextView) findViewById(R.id.preference_text);
        mButton = (Button) findViewById(R.id.done);

        checkBoxes[0] = (CheckBox) findViewById(R.id.checkBox);
        checkBoxes[1] = (CheckBox) findViewById(R.id.checkBox2);
        checkBoxes[2] = (CheckBox) findViewById(R.id.checkBox3);
        checkBoxes[3] = (CheckBox) findViewById(R.id.checkBox4);
        checkBoxes[4] = (CheckBox) findViewById(R.id.checkBox5);

        for (int i = 0; i < 5; i++) {
            checkBoxes[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }

    }


    @Override
    protected void onStart() {
        super.onStart();
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
        String url = "https://api.spotify.com/v1/me/top/tracks"; // Spotify web API url to be called to retrieve guestTracksPreferences

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
        int count=0;
        /**
         * Traverse the JSON object to retrieve the name of user's prefered tracks
         */
        try {
            JSONArray items = jsonresponse.getJSONArray("items");

            if (items.length()==0){
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setText("You have no preferences yet");
            }

            else
            {
                for (int i = 0; i < 5; i++) {
                    if(items.getJSONObject(i).getString("name") != null) {
                        topTracksName[i] = items.getJSONObject(i).getString("name");
                        topTracksID[i] = items.getJSONObject(i).getString("id");
                        count++;
                        Log.e("Counter11111", count+"j");
                    }

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        /**
         * If user has guestTracksPreferences, then display each preference against a checkbox in UI
         */
        if(count > 0)
        {
            for (int i = 0; i < count ; i++)
            {
                checkBoxes[i].setVisibility(View.VISIBLE);
                checkBoxes[i].setText(topTracksName[i]);
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setText("Your preferences are: " );
            }
        }
    }

    /**
     * When the user is done selecting guestTracksPreferences, go to PartyHome screen of app
     */
    public void home(View view) {
        for (int i = 0; i < 5; i++) {
            if(checkBoxes[i].isChecked())
            {
                guestTracksPreferences[selectedPrefCount]= topTracksID[i];
                selectedPrefCount++;
            }
        }
        Intent intent = new Intent(this, PartyHome.class);
        startActivity(intent);
    }

}