package musicplayer.party;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import musicplayer.party.personalization.host.HostPersonalizationTask;
import musicplayer.party.helper.PersonalizationConstant;

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: PartyHome
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 */

/**
 * A simple activity that shows home screen of Party application
 */
public class PartyHome extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        /**
         * Store activity context so that AddTrackTask can start new activity.
         */
        PersonalizationConstant.context = getApplicationContext();

//        Intent hostPersonalizationIntent = new Intent(this, HostPersonalizationService.class);
//        startService(hostPersonalizationIntent);
        new HostPersonalizationTask().execute();

    }
}
