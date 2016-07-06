package musicplayer.party.Personalization.Host;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import musicplayer.party.Personalization.PlaylistUpdate.RecommedationService;
import musicplayer.party.SpotifyService.SpotifyRetrieveUserInfoService;
import musicplayer.party.SpotifyService.UserProfile;

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: HostPersonalizationService
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 * The java class is for start personalization on Host device for party.
 */
public class HostPersonalizationService extends Service  {

    private int numberOfArtists=0;
    private int numberOfTracks=0;


    @Override
    public void onCreate() {


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /*
     * Change UserProfile array to merged array in Host device
     */
       for(int i = 0; i< UserProfile.guestTracksPreferences.length; i++) {
          if (UserProfile.guestTracksPreferences[i] != null) {
             numberOfTracks++; // counting the number of tracks in guestTracksPreferences array
          }
       }


        Log.e("#tracks", "i"+numberOfTracks);

         /*
     * Change UserProfile array to merged array in Host device
     */

       for (int i = 0; i < UserProfile.guestArtistsPreferences.length; i++) {
           if (UserProfile.guestArtistsPreferences[i] != null) {
              numberOfArtists++; // counting the number of artists in guestTracksPreferences array
           }
       }

        Log.e("#artists", "i"+numberOfArtists);


        /**
         * create playlist on Spotify if not created
         */

        Log.e("userid1234567",UserProfile.userID+"i");

        if(UserProfile.userID == null) {

            Intent retrieveUserInfoIntent = new Intent(this, SpotifyRetrieveUserInfoService.class);
            startService(retrieveUserInfoIntent);
        }

        /**
         * If number of tracks and artists is less than or equal to 5 then e call recommendation API.
         */

        if(numberOfArtists==2 && numberOfTracks==3){
            Intent recommnedationIntent = new Intent(this, RecommedationService.class);
            startService(recommnedationIntent);
            Log.e("starting recom ", "service");
        }
        else{
                /**
                 * If number of tracks is more than 5 then filter based on mean algorithm
                 */
                if(numberOfTracks>3){
                    Intent updateTrackParametersIntent = new Intent(this, UpdateTrackParametersService.class);
                    startService(updateTrackParametersIntent);
                    Log.e("starting track ", "host service");
                }

                /**
                 * If number of artists is more than 5 then filter based on mean algorithm.
                 */

                else if(numberOfArtists>2){
                    Intent updateArtistParametersIntent = new Intent(this, UpdateArtistParametersService.class);
                    startService(updateArtistParametersIntent);
                    Log.e("starting artist ", "host service");
                }

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
