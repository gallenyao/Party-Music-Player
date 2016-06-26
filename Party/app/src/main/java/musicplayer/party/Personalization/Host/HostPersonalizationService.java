package musicplayer.party.Personalization.Host;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

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

        for(int i = 0; i< UserProfile.guestTracksPreferences.length; i++){
            if(UserProfile.guestTracksPreferences[i]!=null){
                numberOfTracks++; // counting the number of tracks in guestTracksPreferences array
            }
        }


        for(int i = 0; i< UserProfile.guestArtistsPreferences.length; i++){
            if(UserProfile.guestArtistsPreferences[i]!=null){
                numberOfArtists++; // counting the number of artists in guestTracksPreferences array
            }
        }

        /**
         * If number of tracks is more than 5 then filter based on mean algorithm.
         */
        if(numberOfTracks>5){
            Intent updateTrackParametersIntent = new Intent(this, UpdateTrackParametersService.class);
            startService(updateTrackParametersIntent);
        }

        /**
         * If number of artists is more than 5 then filter based on mean algorithm.
         */
        if(numberOfArtists>5){
            Intent updateArtistParametersIntent = new Intent(this, UpdateArtistParametersService.class);
            startService(updateArtistParametersIntent);
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
