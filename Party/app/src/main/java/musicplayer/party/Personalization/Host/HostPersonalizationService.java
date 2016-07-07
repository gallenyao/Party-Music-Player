package musicplayer.party.Personalization.Host;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import musicplayer.party.SpotifyService.SpotifyRetrieveUserInfoService;
import musicplayer.party.SpotifyService.UserProfile;

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: HostPersonalizationService
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 * The java class is for start personalization on Host device for party.
 */
public class HostPersonalizationService extends Service  {


    @Override
    public void onCreate() {

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /**
         * create playlist_id on Spotify if not created
         */

        if(UserProfile.userID == null) {

            Intent retrieveUserInfoIntent = new Intent(this, SpotifyRetrieveUserInfoService.class);
            startService(retrieveUserInfoIntent);
        }

        /**
         * Call the UpdateTrackParameters service to update personalization parameters
         */

        Intent updateTrackParametersIntent = new Intent(this, UpdateTrackParametersService.class);
        startService(updateTrackParametersIntent);
        Log.e("starting track ", "host service");

        return START_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {

    }

}
