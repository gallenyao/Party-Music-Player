package musicplayer.party;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import musicplayer.party.Personalization.Guest.FilterArtistPreferenceService;
import musicplayer.party.Personalization.Guest.FilterTrackPreferencesService;
import musicplayer.party.Personalization.Host.HostPersonalizationService;

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

        Intent trackFilterIntent = new Intent(this, FilterTrackPreferencesService.class);
        startService(trackFilterIntent);

        Intent artistFilterIntent = new Intent(this, FilterArtistPreferenceService.class);
        startService(artistFilterIntent);

        Intent hostPersonalizationIntent = new Intent(this, HostPersonalizationService.class);
        startService(hostPersonalizationIntent);
    }
}
