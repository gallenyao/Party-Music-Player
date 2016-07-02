package musicplayer.party.Personalization.Host;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import musicplayer.party.SpotifyService.UserProfile;

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: HostPersonalizationService
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 * The java class is for start personalization on Host device for party.
 */
public class HostPersonalizationService extends Service  {

    private int numberOfArtists;
    private int numberOfTracks;


    @Override
    public void onCreate() {


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /*
     * Change UserProfile array to merged array in Host device
     */
        for(int i = 0; i< UserProfile.guestTracksPreferences.length; i++){
            if(UserProfile.guestTracksPreferences[i]!=null){
                numberOfTracks++; // counting the number of tracks in guestTracksPreferences array
            }
        }

        Log.e("#tracks", "i"+numberOfTracks);

         /*
     * Change UserProfile array to merged array in Host device
     */

        for(int i = 0; i< UserProfile.guestArtistsPreferences.length; i++){
            if(UserProfile.guestArtistsPreferences[i]!=null){
                numberOfArtists++; // counting the number of artists in guestTracksPreferences array
            }
        }
        Log.e("#artists", "i"+numberOfArtists);


        /**
         * If number of tracks is more than 5 then filter based on mean algorithm.
         */
        if(numberOfTracks>4){
            Intent updateTrackParametersIntent = new Intent(this, UpdateTrackParametersService.class);
            startService(updateTrackParametersIntent);
            Log.e("starting track ", "host service");
        }

        /**
         * If number of artists is more than 5 then filter based on mean algorithm.
         */

        if(numberOfArtists>4){
            Intent updateArtistParametersIntent = new Intent(this, UpdateArtistParametersService.class);
            startService(updateArtistParametersIntent);
            Log.e("starting artist ", "host service");
        }


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
