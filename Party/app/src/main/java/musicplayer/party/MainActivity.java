package musicplayer.party;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.Config;
import com.spotify.sdk.android.player.Player;
import com.spotify.sdk.android.player.Spotify;

import musicplayer.party.bezirk.BezirkActivity;
import musicplayer.party.helper.PartyConstant;
import musicplayer.party.spotifyService.InstructionActivity;
import musicplayer.party.spotifyService.SpotifyAssembly;
import musicplayer.party.spotifyService.UserProfile;


/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: MainActivity
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 */
public class MainActivity extends AppCompatActivity {

    private Button bezirkLaunchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bezirkLaunchButton = (Button) findViewById(R.id.bezirkLaunchButton);
        bezirkLaunchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BezirkActivity.class);
                startActivity(intent);
            }
        });
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

                    /*
                        Initializing Spotify android player to play Spotify tracks in the app
                     */
                    Config playerConfig = new Config(this, response.getAccessToken(), PartyConstant.CLIENT_ID);
                    PartyConstant.mPlayer = Spotify.getPlayer(playerConfig, this, new Player.InitializationObserver() {
                        @Override
                        public void onInitialized(Player player) {
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            Log.e("MainActivity", "Could not initialize player: " + throwable.getMessage());
                        }
                    });

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
