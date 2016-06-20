package musicplayer.party;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import musicplayer.party.Personalization.FilterPreferencesService;

/*
 *Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: PartyHome
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 */

/*
 * A simple activity that shows home screen of Party application
 */
public class PartyHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = new Intent(this, FilterPreferencesService.class);
        startService(intent);
    }
}
