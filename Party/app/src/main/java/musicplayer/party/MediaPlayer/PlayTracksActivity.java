package musicplayer.party.mediaPlayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;

import musicplayer.party.MainActivity;
import musicplayer.party.PartyHome;
import musicplayer.party.R;
import musicplayer.party.helper.PartyConstant;

public class PlayTracksActivity extends AppCompatActivity implements ConnectionStateCallback, PlayerNotificationCallback {

    private Button resumeButton;
    private Button pauseButton;
    private Button playButton;
    private Button skipButton;
    private ListView playlist;
    private Boolean isGuest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_tracks);
        resumeButton = (Button) findViewById(R.id.resume);
        pauseButton = (Button) findViewById(R.id.pause);
        playButton = (Button) findViewById(R.id.play);
        skipButton = (Button) findViewById(R.id.next);
        playlist = (ListView) findViewById(R.id.playlist);

        Intent intent = getIntent();
        this.isGuest = intent.getBooleanExtra(PartyHome.EXTRA_MESSAGE, false);

        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, PartyConstant.partyPlaylistTracksName );
        // Set The Adapter
        playlist.setAdapter(arrayAdapter);

        if (isGuest) {
            resumeButton.setVisibility(View.INVISIBLE);
            pauseButton.setVisibility(View.INVISIBLE);
            playButton.setVisibility(View.INVISIBLE);
            skipButton.setVisibility(View.INVISIBLE);

            resumeButton.setClickable(false);
            pauseButton.setClickable(false);
            playButton.setClickable(false);
            skipButton.setClickable(false);
        }

    }

    public void play(View view){

        PartyConstant.mPlayer.play(PartyConstant.partyPlaylistTracks);
        playButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.VISIBLE);
    }

    public void pause(View view){
        PartyConstant.mPlayer.pause();
        pauseButton.setVisibility(View.INVISIBLE);
        resumeButton.setVisibility(View.VISIBLE);

    }

    public void resume(View view){
        PartyConstant.mPlayer.resume();
        resumeButton.setVisibility(View.INVISIBLE);
        pauseButton.setVisibility(View.VISIBLE);
    }

    public void next(View view){
        PartyConstant.mPlayer.skipToNext();
    }

    @Override
    public void onLoggedIn() {
    }

    @Override
    public void onLoggedOut() {

    }

    @Override
    public void onLoginFailed(Throwable throwable) {

    }

    @Override
    public void onTemporaryError() {

    }

    @Override
    public void onConnectionMessage(String s) {

    }

    @Override
    public void onPlaybackEvent(EventType eventType, PlayerState playerState) {

    }

    @Override
    public void onPlaybackError(ErrorType errorType, String s) {

    }
}
