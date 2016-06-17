package musicplayer.party;

import android.content.Intent;
import android.os.Bundle;
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

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: SpotifyRetrievePreferences
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 */


public class SpotifyRetrieveArtists extends ActionBarActivity implements Response.Listener,
        Response.ErrorListener {

    /**
     * Set the variables for different UI components declared in activity_spotify_retrieve_artists file.
     */

    public static final String REQUEST_TAG = "SpotifyRetrieveArtists";
    private TextView mTextView;
    private Button mButton;
    private CheckBox c1;
    private CheckBox c2;
    private CheckBox c3;
    private CheckBox c4;
    private CheckBox c5;
    private RequestQueue mQueue;
    private String[] top_artistsname = new String[5]; // array to store top artists retrieved from Spotify web aPI
    private String[] top_artistsID = new String[5];
    private int i=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spotify_retrieve_artists);

        /**
         * Set text to display that the activity will show guest_artists_preferences of user.
         * Set checkboxes that will be used to display top guest_artists_preferences of user.
         * Set button that will be clicke dby user when he has done selecting guest_artists_preferences.
         */
        mTextView = (TextView) findViewById(R.id.preference_text);
        mButton = (Button) findViewById(R.id.done);
        c1 = (CheckBox) findViewById(R.id.checkBox);
        c2 = (CheckBox) findViewById(R.id.checkBox2);
        c3 = (CheckBox) findViewById(R.id.checkBox3);
        c4 = (CheckBox) findViewById(R.id.checkBox4);
        c5 = (CheckBox) findViewById(R.id.checkBox5);

        /**
         * Save the preference of user corresponding to the checkbox he selected
         */
        c1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c1.isChecked())
                {
                    Variable.guest_artists_preferences[i]= top_artistsID[1];
                    i++;
                }
            }
        });


        /**
         * Save the preference of user corresponding to the checkbox he selected
         */
        c2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c2.isChecked())
                {
                    Variable.guest_artists_preferences[i]= top_artistsID[1];
                    i++;
                }
            }
        });

        /**
         * Save the preference of user corresponding to the checkbox he selected
         */
        c3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c3.isChecked())
                {
                    Variable.guest_artists_preferences[i]= top_artistsID[2];
                    i++;
                }
            }
        });

        /**
         * Save the preference of user corresponding to the checkbox he selected
         */
        c4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c4.isChecked())
                {
                    Variable.guest_artists_preferences[i]= top_artistsID[3];
                    i++;
                }
            }
        });

        /**
         * Save the preference of user corresponding to the checkbox user selected
         */
        c5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(c5.isChecked())
                {
                    Variable.guest_artists_preferences[i]= top_artistsID[4];
                    i++;
                }
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        mQueue = CustomVolleyRequestQueue.getInstance(this.getApplicationContext())
                .getRequestQueue();
        String url = "https://api.spotify.com/v1/me/top/artists"; // Spotify web API url to be called to retrieve guest_artists_preferences

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

        /**
         * Traverse the JSON object to retrieve the name of user's prefered artists
         */
        try {
            JSONArray items = jsonresponse.getJSONArray("items");
            if (items.length()==0)
                y=1;
            else
            {
                for (int i = 0; i < 5; i++) {
                    top_artistsname[i] = items.getJSONObject(i).getString("name");
                    top_artistsID[i] = items.getJSONObject(i).getString("id");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /**
         * If user has no guest_artists_preferences, set flag to 1 and display it on text field in UI
         */
        if (y == 1){
            mTextView.setVisibility(View.VISIBLE);
            mTextView.setText("You have no prefernces yet");
        }

        /**
         * If user has guest_artists_preferences, then display each preference against a checkbox in UI
         */
        else
        {
            for (int i = 0; i< top_artistsname.length ; i++)
            {

                if (i==0) {
                    c1.setVisibility(View.VISIBLE);
                    c1.setText(top_artistsname[i]);
                }else if (i==1){
                    c2.setVisibility(View.VISIBLE);
                    c2.setText(top_artistsname[i]);
                }
                else if (i==2) {
                    c3.setVisibility(View.VISIBLE);
                    c3.setText(top_artistsname[i]);
                }
                else if (i==3) {
                    c4.setVisibility(View.VISIBLE);
                    c4.setText(top_artistsname[i]);
                }
                else if (i==4) {
                    c5.setVisibility(View.VISIBLE);
                    c5.setText(top_artistsname[i]);
                }
                mTextView.setVisibility(View.VISIBLE);
                mTextView.setText("Your prefernces are: " );
            }
        }
    }

    /**
     * When the user is done selecting guest_artists_preferences, go to Home screen of app
     */
    public void home(View view) {

        Intent intent = new Intent(this, SpotifyRetrieveTracks.class);
        startActivity(intent);
    }

}