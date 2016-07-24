package musicplayer.party.spotifyService;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import musicplayer.party.R;

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: SpotifyRetrieveArtists
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 * The java class is for retrieving user's top 5 artists from Spotify.
 */
public class SpotifyRetrieveArtists extends ActionBarActivity implements Response.Listener,
        Response.ErrorListener {
    /**
     * Set the variables for different UI components declared in activity_spotify_retrieve_artists file.
     */
    private static final String REQUEST_TAG = "SpotifyRetrieveArtists";

    /**
     * The number of checkbox that will be used.
     */
    private static final int CHECKBOX_NUMBER = 5;

    /**
     * Set textview for the activity.
     */
    private TextView mTextView;

    /**
     * Set button for the activity.
     */
    private Button mButton;

    /**
     * A checkbox array that provides checkbox for user interaction.
     */
    private CheckBox[] checkBoxes = new CheckBox[CHECKBOX_NUMBER];

    /**
     * Request queue that will be used to send request to Spotify.
     */
    private RequestQueue mQueue;

    /**
     * A string array to store top artists' names for the user, which are retrieved from Spotify API.
     */
    private String[] topArtistsName = new String[UserProfile.PREFERENCE_LENGTH];

    /**
     * A string array to store top artists' IDs for the user, which are retrieved from Spotify API.
     */
    private String[] topArtistsID = new String[UserProfile.PREFERENCE_LENGTH];

    /**
     * A counter to count the number of selected preference of the user.
     */
    private int selectedPrefCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_retrieve_artists);
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

        /**
         * Set onclicklistener to every checkbox.
         */
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
        /**
         * ---------------------------------------------+++++++++++++++++++++++
         */
        SpotifyAssembly sa = new SpotifyAssembly();
        sa.SpotifyRequestArtists(this.getApplicationContext(), this, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mQueue != null) {
            mQueue.cancelAll(REQUEST_TAG);
        }
    }

    /**
     * If there is an error while retrieving response from Spotify API, display it on screen
     */
    @Override
    public void onErrorResponse(VolleyError error) {
        mTextView.setText(error.getMessage());
    }

    @Override
    public void onResponse(Object response) {
        /**
         * Store the JSON response retrieved from Spotify web API
         */
        JSONObject jsonresponse = (JSONObject)response;
        int count=0;

        /**
         * Traverse the JSON object to retrieve the name of user's prefered artists
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
                        topArtistsName[i] = items.getJSONObject(i).getString("name");
                        topArtistsID[i] = items.getJSONObject(i).getString("id");
                        count++;
                        Log.d("Counter11111", count + "j");
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
                checkBoxes[i].setText(topArtistsName[i]);
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setText("Your preferences are: " );
            }
        }
    }

    /**
     * The button that user clicks on when finishing selecting preferred artists.
     * @param view view
     */
    public void submitArtists(View view) {
        /**
         * Store user's selected preference to the guestArtistsPreference array.
         */
        //String[] artistPreStr = new String[UserProfile.PREFERENCE_LENGTH];
        for (int i = 0; i < 5; i++) {
            if(checkBoxes[i].isChecked())
            {
                //artistPreStr[selectedPrefCount] = topArtistsID[i];
                UserProfile.selfArtistsPreferences[selectedPrefCount]= topArtistsID[i];
                selectedPrefCount++;
                Log.e("selectednumart","i"+selectedPrefCount);
            }
        }
        //UserProfile.addArtistList(artistPreStr);



        /**
         * Go to next activity.
         */
        Intent intent = new Intent(this, SpotifyRetrieveTracks.class);
        startActivity(intent);
    }

}