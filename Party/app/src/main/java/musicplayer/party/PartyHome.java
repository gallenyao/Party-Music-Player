package musicplayer.party;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.BoolRes;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.bezirk.middleware.Bezirk;
import com.bezirk.middleware.BezirkListener;
import com.bezirk.middleware.addressing.DiscoveredZirk;
import com.bezirk.middleware.addressing.Pipe;
import com.bezirk.middleware.addressing.PipePolicy;
import com.bezirk.middleware.addressing.ZirkEndPoint;
import com.bezirk.middleware.messages.Event;
import com.bezirk.middleware.messages.Stream;
import com.bezirk.middleware.proxy.android.Factory;

import java.io.File;
import java.io.InputStream;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import musicplayer.party.bezirk.BezirkActivity;
import musicplayer.party.bezirk.events.End;
import musicplayer.party.bezirk.events.IdentifyHost;
import musicplayer.party.bezirk.events.Invite;
import musicplayer.party.bezirk.events.Join;
import musicplayer.party.bezirk.events.PlaylistInfo;
import musicplayer.party.bezirk.events.PreferencesAccepted;
import musicplayer.party.bezirk.events.SharePreferences;
import musicplayer.party.bezirk.protocols.GuestRole;
import musicplayer.party.bezirk.protocols.HostRole;
import musicplayer.party.helper.PartyConstant;
import musicplayer.party.mediaPlayer.PlayTracksActivity;
import musicplayer.party.personalization.host.HostPersonalizationTask;
import musicplayer.party.helper.PersonalizationConstant;
import musicplayer.party.spotifyService.UserProfile;

/**
 * Copyright: Team Music Player from MSIT-SE in Carnegie Mellon University.
 * Name: PartyHome
 * Author: Litianlong Yao, Nikita Jain, Zhimin Tang
 */

/**
 * A simple activity that shows home screen of Party application
 */
public class PartyHome extends AppCompatActivity {

    public final static String EXTRA_MESSAGE = "isGuest";

    /**
     * Button for subscribe roles.
     */
    private Button hostButton;
    private Button guestButton;

    private static final String ZIRK_NAME = "Party Zirk";
    private static final String TAG = "Bezirk";
    /**
     * Flag to check the role of a user.
     */
    private boolean isGuest = false;

    private boolean joined = false;

    public boolean guestOrNot() {
        return isGuest;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        hostButton = (Button) findViewById(R.id.host);
        guestButton = (Button) findViewById(R.id.guest);


        final Bezirk bezirk = Factory.registerZirk(PartyHome.this, ZIRK_NAME);

        /**
         * Host button click event-handler.
         */
        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("### @@@ Button Clicked", " Host Button");
                //isGuest = false;
                bezirk.subscribe(new HostRole(), new HostListener(bezirk));
                Log.e("### @@@ Subscribe", " Host");
                /**
                 * Start Host Personalization.
                 */
               new HostPersonalizationTask().execute();
            }
        });

        /**
         * Guest button click event-handler.
         */
        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("### @@@ Button Clicked", " Guest Button");
                bezirk.subscribe(new GuestRole(), new GuestListener(bezirk));
                isGuest = true;
                Log.e("### @@@ Subscribe", " Guest");


            }
        });

        new Timer().scheduleAtFixedRate(
                new TimerTask() {
                    @Override
                    public void run() {
                        //clear the text
                        Log.e("### @@@ Timer Timer "," Timer Timer @@@ ###");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //bezirkMessagesTV.setText("");
                                Log.e("### See Message TV "," TVTV");
                                if (isGuest) {
                                    Log.e("~~@@ Send Event ~~@@","[ IdentifyHost ]");
                                    bezirk.sendEvent(new IdentifyHost());
                                    //Log.e("~~@@ Send Event ~~@@","[ IdentifyHost ]");
                                } else {
                                    Log.e("I am host","");
                                }
                            }
                        });
                    }
                }, 1000, 20000);

        /**
         * Store activity context so that AddTrackTask can start new activity.
         */
        PersonalizationConstant.context = getApplicationContext();

    }


    private class GuestListener implements BezirkListener {
        Bezirk bezirk;

        GuestListener(Bezirk bezirk) {
            this.bezirk = bezirk;
        }

        @Override
        public void receiveEvent(String s, Event event, final ZirkEndPoint zirkEndPoint) {
            //Log.e("#### Receive Event Work","  GuestListener");
            if (event instanceof Invite) {
                Invite invite = (Invite) event;
                Log.e("Invite mes received ", "invite");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bezirk.sendEvent(zirkEndPoint, new Join());
                    }
                }, 500);
            } else if (event instanceof PlaylistInfo) {
                /**
                 * Get playlist info from host.
                 */
                PlaylistInfo playlistInfo = (PlaylistInfo) event;
                Log.e("Receive playlistID: ", playlistInfo.ppid);
                PartyConstant.partyPlaylistID = playlistInfo.ppid;
                PartyConstant.partyPlaylistTracksName = playlistInfo.sharedPlaylist;    // get the current playlist from host.

//                Intent intent_name = new Intent(getApplicationContext(), PlayTracksActivity.class);
//                intent_name.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                getApplicationContext().startActivity(intent_name);

                Intent intent = new Intent(PartyHome.this, PlayTracksActivity.class);
                intent.putExtra(EXTRA_MESSAGE, isGuest);
                startActivity(intent);

                /**
                 * Try to start the playing track activity.
                 */
                //Intent intent_name = new Intent(PersonalizationConstant.context, PlayTracksActivity.class);
                //intent_name.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                //PersonalizationConstant.context.startActivity(intent_name);
                if(!joined) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bezirk.sendEvent(zirkEndPoint, new SharePreferences());
                        }
                    }, 500);
                }
                joined = true;

            } else if (event instanceof PreferencesAccepted) {
                //Log.e("Invite mes received ", "invite");
                PreferencesAccepted preferencesAccepted = (PreferencesAccepted) event;
            } else if (event instanceof End) {
                End end = (End) event;
            }
        }

        @Override
        public void receiveStream(String s, Stream stream, short i, InputStream inputStream, ZirkEndPoint zirkEndPoint) {

        }

        @Override
        public void receiveStream(String s, Stream stream, short i, File file, ZirkEndPoint zirkEndPoint) {

        }

        @Override
        public void streamStatus(short i, StreamStates streamStates) {

        }

        @Override
        public void pipeGranted(Pipe pipe, PipePolicy pipePolicy, PipePolicy pipePolicy1) {

        }

        @Override
        public void pipeStatus(Pipe pipe, PipeStates pipeStates) {

        }

        @Override
        public void discovered(Set<DiscoveredZirk> set) {

        }
    }


    private class HostListener implements BezirkListener {
        Bezirk bezirk;

        HostListener(Bezirk bezirk) {
            this.bezirk = bezirk;
        }

        @Override
        public void receiveEvent(String s, Event event, final ZirkEndPoint zirkEndPoint) {
            //Log.e("#### Receive Event Work","  HostListener");
            if (event instanceof IdentifyHost) {
                Log.e("~~@@ Receive Event ~~@@","[ IdentifyHost ]");
                IdentifyHost identifyHost = (IdentifyHost) event;
                Log.e("Invite mes received ", "ada");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bezirk.sendEvent(zirkEndPoint, new Invite());
                    }
                }, 500);

            } else if (event instanceof Join) {
                Join join = (Join) event;

                Log.e("Join mes received ", "join");
                if(!joined) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            bezirk.sendEvent(zirkEndPoint, new PlaylistInfo());
                        }
                    }, 500);
                }

                joined = true;

            } else if (event instanceof SharePreferences) {

                Log.e("SharePreference done", "Host received pref from guest");

                SharePreferences sharePreferences = (SharePreferences) event;

                for (int i = 0; i < sharePreferences.trackPreference.length; i++) {
                    if (sharePreferences.trackPreference[i] != null) {
                        Log.e(i + "th element shared pref", sharePreferences.trackPreference[i]);
                    }
                }

//                UserProfile.testingIncrementUserCounter();
                UserProfile.addTracksList(sharePreferences.trackPreference);
                Log.e("trackPref size ", String.valueOf(UserProfile.tracksPreferences.size()));

                UserProfile.addArtistList(sharePreferences.artistPreference);
                Log.e("artistPref size ", String.valueOf(UserProfile.artistsPreferences.size()));


                new HostPersonalizationTask().execute();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bezirk.sendEvent(zirkEndPoint, new PreferencesAccepted());
                    }
                }, 500);




                new Timer().scheduleAtFixedRate(
                        new TimerTask() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        bezirk.sendEvent(zirkEndPoint, new PlaylistInfo());
//                                            bezirk.sendEvent(new IdentifyHost());
                                    }
                                });
                            }
                        }, 1000, 20000);



//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        bezirk.sendEvent(zirkEndPoint, new PlaylistInfo());
//                    }
//                }, 500);
            }
        }

        @Override
        public void receiveStream(String s, Stream stream, short i, InputStream inputStream, ZirkEndPoint zirkEndPoint) {

        }

        @Override
        public void receiveStream(String s, Stream stream, short i, File file, ZirkEndPoint zirkEndPoint) {

        }

        @Override
        public void streamStatus(short i, StreamStates streamStates) {

        }

        @Override
        public void pipeGranted(Pipe pipe, PipePolicy pipePolicy, PipePolicy pipePolicy1) {

        }

        @Override
        public void pipeStatus(Pipe pipe, PipeStates pipeStates) {

        }

        @Override
        public void discovered(Set<DiscoveredZirk> set) {

        }
    }
}
