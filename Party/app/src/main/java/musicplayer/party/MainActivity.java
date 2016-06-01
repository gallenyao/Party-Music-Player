package musicplayer.party;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.View;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Method to get access token from Spotify when click on "Login" button.
     * @param view view
     */
    public void loginSpotify(View view){
        Intent intent = new Intent(this, SpotifyLoginActivity.class);
        startActivity(intent);

    }



}
