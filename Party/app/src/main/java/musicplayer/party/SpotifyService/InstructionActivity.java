package musicplayer.party.SpotifyService;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;
import com.spotify.sdk.android.player.Spotify;

import musicplayer.party.R;

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: InstructionActivity
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 */

public class InstructionActivity extends Activity implements
        PlayerNotificationCallback, ConnectionStateCallback {

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);

    }

    /**
     * Button to go to the next activity.
     * @param view view
     */
    public void getPreference(View view){
        Intent intent = new Intent(this, SpotifyRetrieveArtists.class);
        startActivity(intent);
    }

    @Override
    public void onLoggedIn() {
        Log.d("InstructionActivity", "User logged in");
    }

    @Override
    public void onLoggedOut() {
        Log.d("InstructionActivity", "User logged out");
    }

    @Override
    public void onLoginFailed(Throwable error) {
        Log.d("InstructionActivity", "Login failed");
    }

    @Override
    public void onTemporaryError() {
        Log.d("InstructionActivity", "Temporary error occurred");
    }

    @Override
    public void onConnectionMessage(String message) {
        Log.d("InstructionActivity", "Received connection message: " + message);
    }

    @Override
    public void onPlaybackEvent(PlayerNotificationCallback.EventType eventType, PlayerState playerState) {
        Log.d("InstructionActivity", "Playback event received: " + eventType.name());
    }

    @Override
    public void onPlaybackError(PlayerNotificationCallback.ErrorType errorType, String errorDetails) {
        Log.d("InstructionActivity", "Playback error received: " + errorType.name());
    }

    @Override
    protected void onDestroy() {
        Spotify.destroyPlayer(this);
        super.onDestroy();
    }



}
