package musicplayer.party.mediaPlayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;

import musicplayer.party.R;
import musicplayer.party.helper.PartyConstant;

public class PlayTracksActivity extends AppCompatActivity implements ConnectionStateCallback, PlayerNotificationCallback {

    private Button resumeButton;
    private ListView playlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_tracks);
        resumeButton = (Button) findViewById(R.id.resume);
        playlist = (ListView) findViewById(R.id.playlist);


        // Create The Adapter with passing ArrayList as 3rd parameter
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, PartyConstant.partyPlaylistTracksName );
        // Set The Adapter
        playlist.setAdapter(arrayAdapter);

    }

    public void play(View view){

        PartyConstant.mPlayer.play(PartyConstant.partyPlaylistTracks);
    }

    public void pause(View view){
        PartyConstant.mPlayer.pause();
        resumeButton.setVisibility(View.VISIBLE);
    }

    public void resume(View view){
        PartyConstant.mPlayer.resume();
        resumeButton.setVisibility(View.INVISIBLE);
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
