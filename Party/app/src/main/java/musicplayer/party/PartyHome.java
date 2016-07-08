package musicplayer.party;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import musicplayer.party.personalization.host.HostPersonalizationService;

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


    /*
     * Set boolean parameters in protocol so that below personalization service runs only in Host device
     */

        Intent hostPersonalizationIntent = new Intent(this, HostPersonalizationService.class);
        startService(hostPersonalizationIntent);

    }
}
