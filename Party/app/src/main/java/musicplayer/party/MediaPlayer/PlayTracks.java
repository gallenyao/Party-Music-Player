package musicplayer.party.mediaPlayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.spotify.sdk.android.player.ConnectionStateCallback;
import com.spotify.sdk.android.player.PlayerNotificationCallback;
import com.spotify.sdk.android.player.PlayerState;

import musicplayer.party.helper.PartyConstant;
import musicplayer.party.R;

public class PlayTracks extends AppCompatActivity implements ConnectionStateCallback, PlayerNotificationCallback {

    private Button resumeButton;
    private int currentTrack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_tracks);
        resumeButton = (Button) findViewById(R.id.resume);

    }

    public void play(View view){
//        mPlayer.addConnectionStateCallback(PlayTracks.this);
//        mPlayer.addPlayerNotificationCallback(PlayTracks.this);

        for(int i=0;i< PartyConstant.partyPlaylistTracks.size();i++) {
            PartyConstant.mPlayer.play(PartyConstant.partyPlaylistTracks.get(i));
            currentTrack =i;
        }
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
//        if(PartyConstant.partyPlaylistTracks.get(currentTrack+1)==null)
//            currentTrack =0;
//
//        for(int i=currentTrack;i< PartyConstant.partyPlaylistTracks.size();i++) {
//            PartyConstant.mPlayer.play(PartyConstant.partyPlaylistTracks.get(i));
//            currentTrack =i;
//        }
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
