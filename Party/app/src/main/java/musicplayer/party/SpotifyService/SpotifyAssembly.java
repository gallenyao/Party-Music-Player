package musicplayer.party.SpotifyService;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Response;

import org.json.JSONObject;

/**
 * Created by YLTL on 6/24/16.
 */
public class SpotifyAssembly {
    SpotifyMethodsImpl spotifyMethods = new SpotifyMethodsImpl();

    public void SpotifyLogin(Activity activity) {
        spotifyMethods.login(activity);
    }

    public void SpotifyRequestArtists(Context c, Response.Listener<JSONObject> listener,
                                      Response.ErrorListener errorListener) {
        spotifyMethods.requestArtists(c, listener, errorListener);
    }

    public void SpotifyRequestTracks(Context c, Response.Listener<JSONObject> listener,
                                     Response.ErrorListener errorListener) {
        spotifyMethods.requestTracks(c, listener, errorListener);
    }

}
