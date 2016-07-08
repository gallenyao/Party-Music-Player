package musicplayer.party.spotifyService;

import android.app.Activity;
import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONObject;

import musicplayer.party.helper.CustomJSONObjectRequest;
import musicplayer.party.helper.CustomVolleyRequestQueue;
import musicplayer.party.helper.PartyConstant;

/**
 * Created by YLTL on 6/24/16.
 */
public class SpotifyMethodsImpl {

    /**
     * Request queue that will be used to send request to Spotify.
     */
    private RequestQueue mQueue;

    /**
     * Set the variables for different UI components declared in activity_spotify_retrieve_artists file.
     */
    private static final String REQUEST_TAG = "SpotifyMethodsImpl";


    public void requestArtists(Context c, Response.Listener<JSONObject> listener,
                        Response.ErrorListener errorListener) {
        mQueue = CustomVolleyRequestQueue.getInstance(c)
                .getRequestQueue();
        String url = "https://api.spotify.com/v1/me/top/artists"; // Spotify web API url to be called to retrieve guestTracksPreferences

        /**
         * Create a JSON Request using CustomJSONObject function that takes 4 parameters:-
         * 1. Request method type selectedPrefCount.e. a GEt ot a POST request type
         * 2. url to be called
         * 3. create new JSON object for retrieving data
         * 4. some event listeners
         */
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url,new JSONObject(), listener, errorListener);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest); // add JSON request to the queue
    }


    public void requestTracks(Context c, Response.Listener<JSONObject> listener,
                        Response.ErrorListener errorListener) {
        mQueue = CustomVolleyRequestQueue.getInstance(c)
                .getRequestQueue();
        String url = "https://api.spotify.com/v1/me/top/tracks"; // Spotify web API url to be called to retrieve guestTracksPreferences

        /**
         * Create a JSON Request using CustomJSONObject function that takes 4 parameters:-
         * 1. Request method type selectedPrefCount.e. a GEt ot a POST request type
         * 2. url to be called
         * 3. create new JSON object for retrieving data
         * 4. some event listeners
         */
        final CustomJSONObjectRequest jsonRequest = new CustomJSONObjectRequest(Request.Method.GET, url,new JSONObject(), listener, errorListener);
        jsonRequest.setTag(REQUEST_TAG);
        mQueue.add(jsonRequest); // add JSON request to the queue
    }



    public void login(Activity activity) {
        /**
         * Build authentication request and get response through login.
         */
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(PartyConstant.CLIENT_ID, AuthenticationResponse.Type.TOKEN, PartyConstant.REDIRECT_URI);
        builder.setScopes(new String[]{"user-read-private","user-top-read","playlist-modify-public", "playlist-modify-private"} );
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(activity, PartyConstant.REQUEST_CODE, request);
    }

}
