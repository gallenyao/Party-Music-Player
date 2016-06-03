package musicplayer.party;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: MainActivity
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 */
public class MainActivity extends AppCompatActivity {
    /**
     * String that used to identify login status flag.
     */
    public final static String MESSAGE_LOGIN_DECISION = "user_login_decision";

    /**
     * Flag that used to identify login status.
     */
    // public static boolean DEFAULT_LOGIN_DECISION = false;
    private boolean DEFAULT_LOGIN_DECISION = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Method to get access token from Spotify when click on "Login to Spotify" button.
     *
     * @param view view
     */
    public void loginSpotify(View view) {
        /**
         * Set new intent and send login status to SpotifyLoginActivity.
         */
        Intent intent = new Intent(this, SpotifyLoginActivity.class);
        DEFAULT_LOGIN_DECISION = true;
        intent.putExtra(MESSAGE_LOGIN_DECISION, true);
        startActivity(intent);
    }

    /**
     * Method to skip logging in Spotify account and to enter the next steps when click on "Go without login" button.
     *
     * @param view view
     */
    public void loginSkip(View view) {
        /**
         * Set new intent and do not send any status.
         */
        Intent intent = new Intent(this, SpotifyLoginActivity.class);
        startActivity(intent);
    }
}
