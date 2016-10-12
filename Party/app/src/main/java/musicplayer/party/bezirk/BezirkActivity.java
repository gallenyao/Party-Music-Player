package musicplayer.party.bezirk;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import musicplayer.party.R;
import musicplayer.party.bezirk.events.End;
import musicplayer.party.bezirk.events.IdentifyHost;
import musicplayer.party.bezirk.events.Invite;
import musicplayer.party.bezirk.events.Join;
import musicplayer.party.bezirk.events.PlaylistInfo;
import musicplayer.party.bezirk.events.PreferencesAccepted;
import musicplayer.party.bezirk.events.SharePreferences;
import musicplayer.party.bezirk.protocols.GuestRole;
import musicplayer.party.bezirk.protocols.HostRole;

/**
 * @author Litianlong Yao
 */
public class BezirkActivity extends AppCompatActivity {
    private Button hostButton;
    private Button guestButton;

    private TextView bezirkMessagesTV;
    private static final String ZIRK_NAME = "Party Zirk";
    private static final String TAG = "Bezirk";
    private boolean isGuest = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bezirk);

        hostButton = (Button) findViewById(R.id.hostButton);
        guestButton = (Button) findViewById(R.id.guestButton);
        bezirkMessagesTV = (TextView) findViewById(R.id.bezirkTextView);

        final Bezirk bezirk = Factory.registerZirk(BezirkActivity.this, ZIRK_NAME);

        hostButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("### @@@ Button Clicked", " Host Button");
                //isGuest = false;
                bezirk.subscribe(new HostRole(), new HostListener(bezirk));
                Log.e("### @@@ Subscribe", " Host");
            }
        });

        guestButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("### @@@ Button Clicked", " Guest Button");
                bezirk.subscribe(new GuestRole(), new GuestListener(bezirk));
                Log.e("### @@@ Subscribe", " Guest");

                new Timer().scheduleAtFixedRate(
                        new TimerTask() {
                            @Override
                            public void run() {
                                //clear the text
                                Log.e("### @@@ Timer Timer "," Timer Timer @@@ ###");
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        bezirkMessagesTV.setText("");
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

            }
        });




    }

    private void printInfo(String s) {
        if (null != bezirkMessagesTV) {
            bezirkMessagesTV.append(s);
        } else {
            Log.d(TAG, s);
        }
    }

    private class GuestListener implements BezirkListener {
        Bezirk bezirk;

        GuestListener(Bezirk bezirk) {
            this.bezirk = bezirk;
        }

        @Override
        public void receiveEvent(String s, Event event, final ZirkEndPoint zirkEndPoint) {
            Log.e("#### Receive Event Work","  GuestListener");
            if (event instanceof Invite) {
                Invite invite = (Invite) event;
                printInfo("Invite Message received\n");
                Log.e("Invite mes received ", "ada");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bezirk.sendEvent(zirkEndPoint, new Join());
                    }
                }, 500);
            } else if (event instanceof PlaylistInfo) {
                PlaylistInfo playlistInfo = (PlaylistInfo) event;
                printInfo("PlaylistInfo Message received\n");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bezirk.sendEvent(zirkEndPoint, new SharePreferences());
                    }
                }, 500);
            } else if (event instanceof PreferencesAccepted) {
                PreferencesAccepted preferencesAccepted = (PreferencesAccepted) event;
                printInfo("PreferencesAccepted Message received\n");
            } else if (event instanceof End) {
                End end = (End) event;
                printInfo("End Message received\n");
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
            Log.e("#### Receive Event Work","  HostListener");
            if (event instanceof IdentifyHost) {
                Log.e("~~@@ Receive Event ~~@@","[ IdentifyHost ]");
                IdentifyHost identifyHost = (IdentifyHost) event;
                printInfo("????   IdentifyHost Message received\n");
                Log.e("Invite mes received ", "ada");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bezirk.sendEvent(zirkEndPoint, new Invite());
                    }
                }, 500);

            } else if (event instanceof Join) {
                Join join = (Join) event;
                printInfo("Join Message received\n");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bezirk.sendEvent(zirkEndPoint, new PlaylistInfo());
                    }
                }, 500);
            } else if (event instanceof SharePreferences) {
                SharePreferences sharePreferences = (SharePreferences) event;
                printInfo("SharePreferences Message received\n");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        bezirk.sendEvent(zirkEndPoint, new PreferencesAccepted());
                    }
                }, 500);
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

