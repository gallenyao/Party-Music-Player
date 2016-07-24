package musicplayer.party.bezirk.events;

import com.bezirk.middleware.messages.Event;
import com.bezirk.middleware.messages.Message;

import musicplayer.party.helper.PartyConstant;
import musicplayer.party.spotifyService.UserProfile;

/**
 * This event is part of the {@link musicplayer.party.bezirk.protocols.HostRole}. Used by guests of the party to share their music preferences with the host.
 *
 * @author Rishabh Gulati
 */
public class SharePreferences extends Event {

    public static final String TOPIC = SharePreferences.class.getCanonicalName();
    public static final String[] artistPreference = UserProfile.selfArtistsPreferences;
    public static final String[] trackPreference = UserProfile.selfTracksPreferences;

    public SharePreferences() {
        super(Message.Flag.NOTICE, TOPIC);
    }
}
