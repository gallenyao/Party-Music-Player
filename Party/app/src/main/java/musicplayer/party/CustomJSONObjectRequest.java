package musicplayer.party;

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: customJSONObjectRequest
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 */
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CustomJSONObjectRequest extends JsonObjectRequest {

    /**
     * Create a JSON Request using CustomJSONObject function that takes 4 parameters:-
     * 1. Request method type i.e. a GEt ot a POST request type
     * 2. url to be called
     * 3. create new JSON object for retrieving data
     * 4. some event listeners
     */
    public CustomJSONObjectRequest(int method, String url, JSONObject jsonRequest,
                                   Response.Listener<JSONObject> listener,
                                   Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        // Create a Hashmap for headers that would store the name and the corresponding values to be appended in the url
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("Content-Type", "application/json; charset=utf-8");
        headers.put("Authorization", "Bearer "+ SpotifyLoginActivity.Access_Token); // append the url to Spotify web API by adding this
        return headers;
    }

    @Override
    public RetryPolicy getRetryPolicy() {
        // here you can write a custom retry policy
        return super.getRetryPolicy();
    }
}