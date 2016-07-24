package musicplayer.party.personalization.host;

import android.os.AsyncTask;
import android.util.Log;

import musicplayer.party.spotifyService.RetrieveUserInfoTask;
import musicplayer.party.spotifyService.UserProfile;

/**
 * Created by YLTL on 7/13/16.
 */
public class HostPersonalizationTask extends AsyncTask<Void, Integer, Void> {

    @Override
    protected void onPreExecute() {

    }

    @Override
    protected Void doInBackground(Void... params) {
        return null;
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
    }

    @Override
    protected void onPostExecute(Void result) {
        /**
         * create playlist_id on Spotify if not created
         */
        if(UserProfile.userID == null) {
            new RetrieveUserInfoTask().execute();
            Log.e("start RetrieveUserInfo","Host -> RetrieveUserInfo");
        } else {
            /**
             * Call the UpdateTrackParameters service to update personalization parameters
             */
            new UpdateTrackParaTask().execute();
            Log.e("start UpdateTrackPara","Host -> UpdateTrackParaTask");
        }

    }

}
