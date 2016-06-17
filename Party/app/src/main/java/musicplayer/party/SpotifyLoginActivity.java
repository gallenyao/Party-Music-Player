package musicplayer.party;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: SpotifyLoginActivity
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 */
public class SpotifyLoginActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spotify_login);
        /**
         * Get intent from MainActivity.
         */
        Intent intent = getIntent();
        /**
         * Get login status from MainActivity.
         */
        Boolean loginStatus = intent.getBooleanExtra(MainActivity.LOGIN_DECISION, false);
        if (loginStatus) {
            /**
             * Build authentication request and get response through login.
             */
            AuthenticationRequest.Builder builder =
                    new AuthenticationRequest.Builder(Variable.CLIENT_ID, AuthenticationResponse.Type.TOKEN, Variable.REDIRECT_URI);
            builder.setScopes(new String[]{"user-read-private","user-top-read","playlist-modify-public", "playlist-modify-private"});
            AuthenticationRequest request = builder.build();
            AuthenticationClient.openLoginActivity(this, Variable.REQUEST_CODE, request);

            /**
             * Set text to show the status of login as "login".
             */
            TextView textview = new TextView(this);
            textview.setText("Status: Login");
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.rL);
            layout.addView(textview);

        } else {
            /**
             * Skip login and authentication, set text to show the status of login as "Not login".
             */
            TextView textview = new TextView(this);
            textview.setText("Status: Not Login");
            RelativeLayout layout = (RelativeLayout) findViewById(R.id.rL);
            layout.addView(textview);
        }



    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        /**
         * Check if result comes from the correct activity.
         */
        if (requestCode == Variable.REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
            if (response.getType() == AuthenticationResponse.Type.TOKEN) {
                /**
                 * Display received access token on logcat in Android Studio.
                 */
                Log.e("Access token 111", response.getAccessToken());
                Variable.Access_Token = response.getAccessToken();
                /**
                 * Display received access token on the screen.
                 */
                Toast.makeText(getApplicationContext(), response.getAccessToken(),Toast.LENGTH_LONG).show();
            }
        }

    }

    public void getData(View view){
        Intent intent = new Intent(this, SpotifyRetrieveArtists.class);
        startActivity(intent);

    }

    @Override
    public void onLoggedIn() {
        Log.d("SpotifyLoginActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("SpotifyLoginActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("SpotifyLoginActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("SpotifyLoginActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("SpotifyLoginActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(PlayerNotificationCallback.EventType eventType, PlayerState playerState) {
        Log.d("SpotifyLoginActivity", "Playback event received: " + eventType.name());
    }

    @Override
    public void onPlaybackError(PlayerNotificationCallback.ErrorType errorType, String errorDetails) {
        Log.d("SpotifyLoginActivity", "Playback error received: " + errorType.name());
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }

}
