package musicplayer.party;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import musicplayer.party.Helper.PartyConstant;
import musicplayer.party.SpotifyService.InstructionActivity;
import musicplayer.party.SpotifyService.SpotifyAssembly;
import musicplayer.party.SpotifyService.UserProfile;

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: MainActivity
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Method to get access token from Spotify when click on "Login to Spotify" button.
     * @param view view
     */
    public void loginSpotify(View view){
        SpotifyAssembly sa = new SpotifyAssembly();
        sa.SpotifyLogin(this);
    }

    /**
     *  Method to skip logging in Spotify account and to enter the next steps when click on "Go without login" button.
     * @param view view
     */
    public void skipLogin(View view){
        /**
         * Set new intent and do not send any status.
         */
        if (!UserProfile.DEFAULT_LOGIN_STATUS) {
            Intent intent = new Intent(this, PartyHome.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Already logged in", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        try {
            super.onActivityResult(requestCode, resultCode, intent);
            /**
             * Check if result comes from the correct activity.
             */
            if (requestCode == PartyConstant.REQUEST_CODE) {
                AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
                if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                    PartyConstant.Access_Token = response.getAccessToken();
                    UserProfile.DEFAULT_LOGIN_STATUS = true;
                    Toast.makeText(getApplicationContext(), "Log In Successful", Toast.LENGTH_LONG).show();
                    /**
                     * Set new intent and send login status to InstructionActivity.
                     */
                    Intent intentIns = new Intent(this, InstructionActivity.class);
                    startActivity(intentIns);
                }
            }
        } catch(Throwable e){
            e.printStackTrace();
        }
    }
}
